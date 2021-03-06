package io.github.bfstats.dbfiller;

import io.github.bfstats.dbstats.jooq.tables.records.*;
import io.github.bfstats.logparser.XmlParser;
import io.github.bfstats.logparser.xml.BfEvent;
import io.github.bfstats.logparser.xml.BfLog;
import io.github.bfstats.logparser.xml.BfRound;
import io.github.bfstats.logparser.xml.BfRoundStats;
import io.github.bfstats.logparser.xml.enums.EventName;
import io.github.bfstats.logparser.xml.enums.event.*;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.xml.sax.SAXException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.InflaterInputStream;

import static io.github.bfstats.dbstats.jooq.Tables.*;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

@ParametersAreNonnullByDefault
@Slf4j
public class DbFiller {
  // English, French, Italian, Spanish, German
  public static final Set<String> STANDARD_NAMES = Stream.of("Player", "Joueur", "Giocatore", "Jugador", "Spieler")
      .collect(Collectors.toSet());

  public static final int FAKE_BOT_SLOT_ID = -1;
  public static final int FAKE_BOT_TEAM = -1;

  public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
  public static final PathMatcher XML_PATH_MATCHER = FileSystems.getDefault().getPathMatcher("glob:*.xml");
  public static final PathMatcher ZXML_PATH_MATCHER = FileSystems.getDefault().getPathMatcher("glob:*.zxml");

  private static DSLContext dslContext;
  private static Connection connection;

  private DSLContext transactionDslContext;

  private final BfLog bfLog;
  private final String gameServerAddress;
  private final ZoneId logFileZoneId;
  private final LocalDateTime logStartTime; // in UTC
  private Duration logEndDuration; // in UTC
  private final String engine;

  private List<BfEvent> eventQueueBeforeFirstRound = new ArrayList<>();

  // actually not per round, but per log file
  private Map<Integer, GamePlayer> activePlayersByRoundPlayerId = new HashMap<>();

  private Map<Integer, BfEvent> lastKillEventByVictimId = new HashMap<>();
  private Map<Integer, BfEvent> lastEnterVehicleByPlayerSlotId = new HashMap<>();
  private Map<Integer, BfEvent> lastBeginRepairByPlayerSlotId = new HashMap<>();
  private Map<Integer, BfEvent> lastBeginMedPackByPlayerSlotId = new HashMap<>();
  private int gameRecordId;

  public DbFiller(BfLog bfLog, String gameServerAddress, ZoneId logFileZoneId) {
    this.bfLog = bfLog;
    this.gameServerAddress = gameServerAddress;
    this.logFileZoneId = logFileZoneId;
    LocalDateTime logStartTimeInServerTimezone = bfLog.getTimestampAsDate();
    this.logStartTime = logStartTimeInServerTimezone.atZone(logFileZoneId)
        .withZoneSameInstant(ZoneOffset.UTC)
        .toLocalDateTime();
    this.engine = bfLog.getEngine();
  }

  private DSLContext transaction() {
    return transactionDslContext;
  }

  public static Properties loadDbConfigProperties() throws IOException {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    InputStream configFileInputStream = loader.getResourceAsStream("dbconfig.properties");
    Properties props = new Properties();
    props.load(configFileInputStream);
    return props;
  }

  public static void main(String[] args) throws IOException {
    System.setProperty("org.jooq.no-logo", "true");

    Properties dbConfigProperties = loadDbConfigProperties();
    String dbUrl = dbConfigProperties.getProperty("databaseUrl", "jdbc:sqlite:database.db");

    List<FtpDownloader.ConnectDetails> connectDetailsList = FtpDownloader.loadConfigProperties();
    for (FtpDownloader.ConnectDetails connectDetails : connectDetailsList) {
      FtpDownloader.GameServerDetails gameServerDetails = connectDetails.getGameServerDetails();
      System.out.println("Adding for " + gameServerDetails.getGameServerAddress() + ":" + gameServerDetails.getGameServerPort());
      if (connectDetails.isDownload()) {
        FtpDownloader.downloadFiles(connectDetails);
      }
      String logDirPath = connectDetails.getLocalDirectory();
      prepareConnection(dbUrl);
      try {
        parseAllInDir(logDirPath, gameServerDetails);
      } finally {
        closeConnection();
      }
    }
  }

  private static void parseAllInDir(String logDirPath, FtpDownloader.GameServerDetails gameServerDetails) {
    String gameServerAddress = gameServerDetails.getGameServerAddress();
    int gameServerPort = gameServerDetails.getGameServerPort();

    String gameServerTimezone = gameServerDetails.getGameServerTimezone();
    ZoneId logFileZoneId = ZoneId.of(gameServerTimezone);

    File logDir = new File(logDirPath);
    File[] dirFiles = logDir.listFiles((dir, name) -> name.endsWith(".xml") || name.endsWith(".zxml"));
    if (dirFiles == null) {
      throw new IllegalArgumentException("could not list files in " + logDirPath);
    }

    // order by filename, which assumes the resulting order will be by logfile creation date ascending
    // so we can skip parsing the last file.
    Arrays.sort(dirFiles, Comparator.comparing(File::getName));

    int totalNumberOfFiles = dirFiles.length;
    int numberOfFilesCompleted = 0;

    LocalDateTime latestAdded = findLatestAddedLogFileTime(gameServerAddress, gameServerPort);

    String lastValidDateTimeStr = null;
    for (File fileI : dirFiles) {
      if (numberOfFilesCompleted % 30 == 0) {
        System.out.println(numberOfFilesCompleted + "/" + totalNumberOfFiles);
      }
      numberOfFilesCompleted++;

      // true if last file
      // Btw, xml counterpart will come before zxml,
      // but if both are present, xml is anyway skipped and hope is on zxml which usually comes on next iteration
      // and will have probablyLiveFile as true then
      boolean probablyLiveFile = numberOfFilesCompleted == totalNumberOfFiles;

      String dtsAndExtStr = fileI.getName().split("-")[1];
      String dateTimeStr = dtsAndExtStr.split("\\.")[0];
      LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);

      if (latestAdded != null && !dateTime.isAfter(latestAdded)) {
        continue;
      }

      Path xmlFilePath = extractIfNecessary(fileI.toPath());
      if (xmlFilePath != null) {
        try {
          addFromXmlFile(xmlFilePath, !probablyLiveFile, gameServerAddress, logFileZoneId);
          lastValidDateTimeStr = dateTimeStr;
          // xml files waste a lot of space, let's keep only the zxml variant
          if (hasZxmlCounterpart(xmlFilePath)) {
            deleteXmlFile(xmlFilePath);
          }
        } catch (RuntimeException e) {
          log.warn("Could not parse " + xmlFilePath, e);
        }
      }
    }

    if (lastValidDateTimeStr != null) {
      updateLatestAddedLogFileTime(gameServerAddress, gameServerPort, lastValidDateTimeStr);
    }
  }

  private static void deleteXmlFile(Path xmlFilePath) {
    if (!XML_PATH_MATCHER.matches(xmlFilePath.getFileName())) {
      throw new IllegalArgumentException("deleteXmlFile should be be given only .xml files, you gave: " + xmlFilePath);
    }
    try {
      Files.delete(xmlFilePath);
    } catch (IOException e) {
      log.warn("Could not delete xml file " + xmlFilePath, e);
    }
  }

  @Nullable
  private static LocalDateTime findLatestAddedLogFileTime(String gameServerAddress, int gameServerPort) {
    ServerRecord server = findServer(gameServerAddress, gameServerPort);
    if (server == null) {
      return null;
    }

    String lastParsedDateTime = server.getLastParsedDatetime();
    if (lastParsedDateTime == null) {
      return null;
    }

    return LocalDateTime.parse(lastParsedDateTime, DATE_TIME_FORMATTER);
  }

  private static void updateLatestAddedLogFileTime(String gameServerAddress, int gameServerPort, String lastValidDateTimeStr) {
    ServerRecord server = findServer(gameServerAddress, gameServerPort);
    if (server == null) {
      throw new IllegalStateException("server does not exist for IP " + gameServerAddress + " and port " + gameServerPort);
    }
    server.setLastParsedDatetime(lastValidDateTimeStr);
    server.update();
  }

  private static Path extractIfNecessary(Path filePath) {
    if (XML_PATH_MATCHER.matches(filePath.getFileName())) {
      boolean hasZxmlCounterpart = hasZxmlCounterpart(filePath);
      if (hasZxmlCounterpart) {
        // TODO: maybe vice-versa - should ignore zxml files instead
        log.info("ignoring " + filePath + " because zxml will be extracted again");
        // skipping xml, as zxml also exists, so will wait for unzipping that again
        return null;
      } else {
        return filePath;
      }
    } else if (ZXML_PATH_MATCHER.matches(filePath.getFileName())) {
      Path xmlFilePath = changeFileExtension(filePath, "zxml", "xml");

      try {
        decompress(filePath, xmlFilePath);
        return xmlFilePath;
      } catch (IOException e) {
        log.warn("looks like " + filePath + " is not complete. " + e.getMessage());
        File xmlFile = xmlFilePath.toFile();
        if (xmlFile.length() == 0) {
          return null;
        }
        return xmlFilePath;
      }
    }
    return null;
  }

  private static Path changeFileExtension(Path oldFilePath, String oldExtension, String newExtension) {
    if (oldExtension.startsWith(".")) {
      throw new IllegalArgumentException("please specify oldExtension without preceding dot for given " + oldExtension);
    }
    if (newExtension.startsWith(".")) {
      throw new IllegalArgumentException("please specify oldExtension without preceding dot for given " + newExtension);
    }
    String oldFileName = oldFilePath.getFileName().toString();
    String newFileName = oldFileName.substring(0, oldFileName.length() - (oldExtension.length())) + newExtension;

    return oldFilePath.getParent().resolve(newFileName);
  }

  private static boolean hasZxmlCounterpart(Path xmlFilePath) {
    Path checkablePath = changeFileExtension(xmlFilePath, "xml", "zxml");
    return Files.exists(checkablePath);
  }

  private static void prepareConnection(String url) {
    try {
      connection = DriverManager.getConnection(url);
      dslContext = DSL.using(connection);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static void closeConnection() {
    dslContext.close();
    try {
      connection.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static void addFromXmlFile(Path xmlFilePath, boolean tryFixing, String gameServerAddress, ZoneId logFileZoneId) {
    try {
      log.info("Parsing " + xmlFilePath);
      File xmlFile = xmlFilePath.toFile();
      BfLog bfLog = XmlParser.parseXmlLogFile(xmlFile, tryFixing);
      DbFiller dbFiller = new DbFiller(bfLog, gameServerAddress, logFileZoneId);
      dbFiller.fillDb();
    } catch (JAXBException | IOException | SAXException e) {
      throw new RuntimeException(e);
    }
  }

  public static void decompress(Path compressedFilePath, Path outputFilePath) throws IOException {
    File compressedFile = compressedFilePath.toFile();
    File outputFile = outputFilePath.toFile();

    // power of 2 recommended, 8192 = 2^13
    int blockSize = 8192;
    try (InflaterInputStream zipin = new InflaterInputStream(new FileInputStream(compressedFile));
         OutputStream out = new FileOutputStream(outputFile)
    ) {
      byte[] buffer = new byte[blockSize];
      int length;
      while ((length = zipin.read(buffer, 0, blockSize)) > 0) {
        out.write(buffer, 0, length);
      }
    }
  }

  private void fillDb() {
    dslContext.transaction(configuration -> {
      this.transactionDslContext = DSL.using(configuration);

      PlayerRecord botPlayerRecord = addPlayerOrNickName("FAKE_BOT", "FAKE_BOT");
      Integer botPlayerId = botPlayerRecord.getId();
      GamePlayer botGamePlayer = new GamePlayer()
          .setRoundPlayerSlotId(FAKE_BOT_SLOT_ID)
          .setPlayerId(botPlayerId)
          .setName("FAKE_BOT")
          .setKeyhash("FAKE_BOT")
          .setTeam(FAKE_BOT_TEAM);
      activePlayersByRoundPlayerId.put(botGamePlayer.getRoundPlayerSlotId(), botGamePlayer);

      this.gameRecordId = createGame();
      parseLog();
    });

    if (!lastKillEventByVictimId.isEmpty()) {
      log.warn("last kill events is not empty in the end of the round " + lastKillEventByVictimId);
    }
  }

  private int createGame() {
    BfRound firstRound = bfLog.getFirstRound();
    ServerRecord serverRecord = addOrGetServer(gameServerAddress, firstRound.getPort(), firstRound.getServerName());
    GameRecord gameRecord = createGameRecord(serverRecord.getId(), firstRound);
    return gameRecord.getId();
  }


  private void parseLog() {
    // Events can be out-of-round, so mixing out-of-round events and rounds.
    // For chat using the last round id, as chances was that it belongs to that more than to the new round
    // (because during long round initialization you probably cant chat anymore).
    int lastRoundId = -1;
    for (Object child : bfLog.getRootEventsAndRounds()) {
      if (child instanceof BfEvent) {
        BfEvent bfEvent = (BfEvent) child;
        if (lastRoundId == -1) {
          log.info("round has not started yet, so adding event to queue " + child);
          eventQueueBeforeFirstRound.add(bfEvent);
        } else {
          parseEvent(lastRoundId, bfEvent);
        }
      } else if (child instanceof BfRound) {
        BfRound bfRound = (BfRound) child;
        lastRoundId = parseRound(bfRound, lastRoundId);
      } else if (child instanceof String) {
        if (!child.toString().equals("\n")) {
          log.warn("got non-newline string child " + child.toString());
        }
      } else {
        log.warn("unhandled child type" + child.toString());
      }
    }

    if (lastRoundId != -1 && logEndDuration != null) {
      recordEndGame(lastRoundId, logEndDuration);
    }
  }

  private void processEventQueueFromBeforeFirstRound(int firstRoundId) {
    for (BfEvent bfEvent : eventQueueBeforeFirstRound) {
      parseEvent(firstRoundId, bfEvent);
    }
    eventQueueBeforeFirstRound.clear();
  }

  private void forceEndAllVehicleEvents(int roundId, Duration endTimeDurationSinceLogStart) {
    lastEnterVehicleByPlayerSlotId.forEach((playerSlotId, enterVehicle) ->
        addVehicleUsage(roundId, enterVehicle, endTimeDurationSinceLogStart)
    );
    lastEnterVehicleByPlayerSlotId.clear();
  }

  @Nullable
  private static ServerRecord findServer(String ip, int port) {
    ServerRecord server = dslContext
        .selectFrom(SERVER)
        .where(SERVER.IP.eq(ip).and(SERVER.PORT.eq(port)))
        .fetchOne();

    return server;
  }

  private ServerRecord addOrGetServer(String ip, int port, String name) {
    ServerRecord server = findServer(ip, port);

    if (server == null) {
      server = transaction().newRecord(SERVER);
      server.setIp(ip);
      server.setPort(port);
      server.setName(name);
      server.setTimezoneName(logFileZoneId.getId());

      server.insert();
    } else {
      server.setName(name);
      server.update();
    }

    return server;
  }

  private int parseRound(BfRound bfRound, int previousRoundId) {
    RoundRecord roundRecord = addRound(bfRound, gameRecordId);
    int roundId = requireNonNull(roundRecord.getId());

    // if this is the first round of a game, then process events written before the round element as if they belong
    // inside this round
    if (previousRoundId == -1) {
      processEventQueueFromBeforeFirstRound(roundId);
    }

    // if this is not the first round of a game
    if (previousRoundId != -1) {
      // put team between two rounds into the previous round
      // kind of waste of db - should merge this to existing previous round team usage?
      recordEndRound(previousRoundId, bfRound.getDurationSinceLogStart());
    }

    BfEvent lastEvent = null;
    for (BfEvent e : bfRound.getEvents()) {
      parseEvent(roundId, e);
      lastEvent = e;
    }

    if (bfRound.getRoundStats() != null) {
      BfRoundStats roundStats = bfRound.getRoundStats();
      forceEndAllVehicleEvents(roundId, roundStats.getDurationSinceLogStart());
      RoundEndStatsRecord roundEndStatsRecord = addRoundEndStats(roundId, roundStats);
      // higher score to lower score
      List<BfRoundStats.BfPlayerStat> sortedPlayerStats = roundStats.getPlayerStats().stream()
          .sorted(Comparator.comparingInt(BfRoundStats.BfPlayerStat::getScore).reversed())
          .collect(Collectors.toList());

      // in order of rank
      int rank = 0;
      for (BfRoundStats.BfPlayerStat bfPlayerStat : sortedPlayerStats) {
        rank++;
        if (bfPlayerStat.isAi()) {
          // skip bot stats
          continue;
        }
        addRoundEndStatsPlayer(roundId, bfPlayerStat, rank);
      }
    }

    if (lastEvent != null) {
      Duration teamEndDur = ofNullable(bfRound.getRoundStats())
          .map(BfRoundStats::getDurationSinceLogStart)
          .orElse(lastEvent.getDurationSinceLogStart());
      recordEndRound(roundId, teamEndDur);
    }

    if (lastEvent != null) {
      logEndDuration = lastEvent.getDurationSinceLogStart();
    } else {
      if (bfRound.getRoundStats() != null) {
        log.warn(logStartTime + " there were no events in round, so using round stats time as end time");
        logEndDuration = bfRound.getRoundStats().getDurationSinceLogStart();
      } else {
        log.warn(logStartTime + " there were no events in round, so using round start time as end time");
        logEndDuration = Duration.ZERO;
      }
    }

    return roundId;
  }

  private void recordEndGame(int endingRoundId, Duration endingRoundEndTime) {
    for (GamePlayer gamePlayer : activePlayersByRoundPlayerId.values()) {
      if (!isSlotIdBot(gamePlayer.getRoundPlayerSlotId())) {
        addGamePlayerJoinEndDates(endingRoundId, gamePlayer, endingRoundEndTime);
      }
    }
  }

  private void recordEndRound(int endingRoundId, Duration endingRoundEndTime) {
    for (GamePlayer gamePlayer : activePlayersByRoundPlayerId.values()) {
      if (isSlotIdBot(gamePlayer.getRoundPlayerSlotId())) {
        continue;
      }
      if (gamePlayer.getTeam() == null || gamePlayer.getPlayerId() == null) {
        continue;
      }
      addRoundPlayerTeamUsage(endingRoundId, gamePlayer, endingRoundEndTime);
    }
  }

  private void parseEvent(Integer roundId, BfEvent e) {
    switch (e.getEventName()) {
      case createPlayer:
        parseEventCreatePlayer(roundId, e);
        break;
      case playerKeyHash:
        parseEventPlayerKeyHash(e);
        break;
      case destroyPlayer:
        if (isSlotIdBot(e.getPlayerSlotId())) {
          // skipping bot vehicle events
          return;
        }

        if (getRoundPlayerFromSlotId(e.getPlayerSlotId()).getPlayerId() != null) {
          endVehicleUsageIfNeeded(roundId, e);
          endRepairIfNeeded(roundId, e);
          endMedPackIfNeeded(roundId, e);
        }

        parseEventDestroyPlayer(roundId, e);
        break;
      case changePlayerName:
        parseEventChangePlayerName(e);
        break;
      case chat:
        parseEventChat(roundId, e);
        break;
      case scoreEvent:
        String scoreType = e.getStringParamValueByName(ScoreEventParams.score_type.name());

        // add kill event to queue until death event picks it up
        if (scoreType.equals(ScoreType.TK.name()) || scoreType.equals(ScoreType.Kill.name())) {
          setLastKillEvent(e);
        } else if (scoreType.equals(ScoreType.Death.name()) || scoreType.equals(ScoreType.DeathNoMsg.name())) {
          endVehicleUsageIfNeeded(roundId, e);
          endRepairIfNeeded(roundId, e);
          endMedPackIfNeeded(roundId, e);
          parseDeathEvent(roundId, e);
        } else {
          parseEventScoreEvent(roundId, e);
        }
        break;
      case enterVehicle:
        setLastEnterVehicle(e);
        break;
      case exitVehicle:
        endVehicleUsageIfNeeded(roundId, e);
        break;
      case createVehicle:
        break;
      case destroyVehicle:
        // handled already in exitVehicle?
        break;
      case beginMedPack:
        addLastBeginMedPackEvent(e);
        break;
      case endMedPack:
        endMedPackIfNeeded(roundId, e);
        break;
      case beginRepair:
        addLastBeginRepairEvent(e);
        break;
      case endRepair:
        endRepairIfNeeded(roundId, e);
        break;
      case setTeam:
        parseSetTeamEvent(roundId, e);
        break;
      case spawnEvent:
        if (!isSlotIdBot(e.getPlayerSlotId())) {
          GamePlayer gamePlayer = getRoundPlayerFromSlotId(e.getPlayerSlotId());
          Integer spawnTeam = e.getIntegerParamValueByName(SpawnEventParams.team.name());
          if (!gamePlayer.getTeam().equals(spawnTeam)) {
            // adds team-usage for previous team
            addRoundPlayerTeamUsage(roundId, gamePlayer, e.getDurationSinceLogStart());
            gamePlayer.setTeam(spawnTeam);
          }
        }
        break;
      case pickupKit:
        parsePickupKit(roundId, e);
        break;
      case deployObject:
        parseDeployObject(roundId, e);
        break;
      case restartMap:
        parseRestartMap(roundId, e);
        break;
      case endGame:
        parseEndGameEvent(roundId, e);
        break;
      case radioMessage:
        parseRadioMessage(roundId, e);
        break;
      case sayAll:
        String msg = e.getStringParamValueByName(SayAllParams.text.name());
        break;
      case gamePaused:
        parseGamePaused(roundId, e);
        break;
      case gameUnpaused:
        parseGameUnpaused(roundId, e);
        break;
    }
  }

  /*
  <bf:event name="beginRepair" timestamp="1085.03">
      <bf:param type="int" name="player_id">1</bf:param>
      <bf:param type="vec3" name="player_location">1188.66/35.9301/1294.51</bf:param>
      <bf:param type="int" name="repair_status">1000</bf:param>
      <bf:param type="string" name="vehicle_type">T54</bf:param>
  </bf:event>
   */

  /*
  <bf:event name="beginRepair" timestamp="1091.91">
    <bf:param type="int" name="player_id">1</bf:param>
    <bf:param type="vec3" name="player_location">1195.77/35.6102/1285.18</bf:param>
    <bf:param type="int" name="repair_status">1000</bf:param>
    <bf:param type="string" name="vehicle_type">VietcongA2</bf:param>

    <bf:param type="int" name="vehicle_player">1</bf:param>
  </bf:event>

  beginHeal
if player_id == healed_player selfheal; number of self-heals
else otherheal; number of heals


beginRepair
if vehicle_player: repairplayer; number of repairs to others
else: repair; number of repairs
   */


  private void addLastBeginRepairEvent(BfEvent e) {
    if (isSlotIdBot(e.getPlayerSlotId())) {
      // skipping bot
      return;
    }

    Integer playerSlotId = e.getPlayerSlotId();

    String newVehicleType = e.getStringParamValueByName(BeginRepairParams.vehicle_type.name());
    if (lastBeginRepairByPlayerSlotId.containsKey(playerSlotId)) {
      BfEvent existingEnterEvent = lastBeginRepairByPlayerSlotId.get(playerSlotId);
      String oldVehicle = existingEnterEvent.getStringParamValueByName(BeginRepairParams.vehicle_type.name());
      log.warn(logStartTime + " last begin repair event already exists for player slot " + playerSlotId + " (" + existingEnterEvent.getTimestamp() + "= new vehicle: " +
          newVehicleType + "(" + e.getTimestamp() + "), old vehicle: " + oldVehicle);
    }

    lastBeginRepairByPlayerSlotId.put(playerSlotId, e);
  }

  private RoundPlayerRepairRecord endRepairIfNeeded(int roundId, BfEvent someEndEvent) {
    if (isSlotIdBot(someEndEvent.getPlayerSlotId())) {
      // skipping bot vehicle events
      return null;
    }

    BfEvent beginRepairEvent = lastBeginRepairByPlayerSlotId.remove(someEndEvent.getPlayerSlotId());
    if (beginRepairEvent != null) {
      return addRepairUsage(roundId, beginRepairEvent, someEndEvent);
    }
    return null;
  }

  /*
  <bf:event name="beginMedPack" timestamp="728.543">
    <bf:param type="int" name="player_id">0</bf:param>
    <bf:param type="vec3" name="player_location">477.402/39.2381/519.162</bf:param>
    <bf:param type="int" name="medpack_status">300</bf:param>
    <bf:param type="int" name="healed_player">201</bf:param>
  </bf:event>

  <bf:event name="endMedPack" timestamp="304.624">
      <bf:param type="int" name="player_id">0</bf:param>
      <bf:param type="vec3" name="player_location">958.119/21.4942/742.521</bf:param>
      <bf:param type="int" name="medpack_status">61</bf:param>
  </bf:event>
   */
  private void addLastBeginMedPackEvent(BfEvent e) {
    if (isSlotIdBot(e.getPlayerSlotId())) {
      // skipping bot
      return;
    }

    Integer playerSlotId = e.getPlayerSlotId();

    Integer newHealedPlayer = e.getIntegerParamValueByName(BeginMedPackParams.healed_player.name());
    if (lastBeginMedPackByPlayerSlotId.containsKey(playerSlotId)) {
      BfEvent existingBeginMedPackEvent = lastBeginMedPackByPlayerSlotId.get(playerSlotId);
      Integer oldHealedPlayer = existingBeginMedPackEvent.getIntegerParamValueByName(BeginMedPackParams.healed_player.name());
      log.warn(logStartTime + " last begin med pack event already exists for player slot " + playerSlotId + " (" + existingBeginMedPackEvent.getTimestamp() + "= new vehicle: " +
          newHealedPlayer + "(" + e.getTimestamp() + "), old healed player: " + oldHealedPlayer);
    }

    lastBeginMedPackByPlayerSlotId.put(playerSlotId, e);
  }


  private RoundPlayerMedpackRecord endMedPackIfNeeded(int roundId, BfEvent someEndEvent) {
    if (isSlotIdBot(someEndEvent.getPlayerSlotId())) {
      // skipping bot events
      return null;
    }

    BfEvent beginMedPackEvent = lastBeginMedPackByPlayerSlotId.remove(someEndEvent.getPlayerSlotId());
    if (beginMedPackEvent != null) {
      return addMedPackUsage(roundId, beginMedPackEvent, someEndEvent);
    }
    return null;
  }

  private RoundPlayerMedpackRecord addMedPackUsage(int roundId, BfEvent beginMedPackEvent, BfEvent endMedPackEvent) {
    RoundPlayerMedpackRecord roundPlayerMedpackRecord = transaction().newRecord(ROUND_PLAYER_MEDPACK);

    int playerId = getPlayerIdFromSlotId(beginMedPackEvent.getPlayerSlotId());

    Integer healedPlayerSlotId = beginMedPackEvent.getIntegerParamValueByName(BeginMedPackParams.healed_player.name());
    int healedPlayerId = getPlayerIdFromSlotId(healedPlayerSlotId);

    LocalDateTime startTime = logStartTime.plus(beginMedPackEvent.getDurationSinceLogStart());
    LocalDateTime endTime = logStartTime.plus(endMedPackEvent.getDurationSinceLogStart());
    int durationSeconds = Long.valueOf(ChronoUnit.SECONDS.between(startTime, endTime)).intValue();

    Integer beginMedPackStatus = beginMedPackEvent.getIntegerParamValueByName(BeginMedPackParams.medpack_status.name());
    Integer endMedPackStatus = endMedPackEvent.getIntegerParamValueByName(EndMedPackParams.medpack_status.name());

    roundPlayerMedpackRecord.setRoundId(roundId);
    roundPlayerMedpackRecord.setPlayerId(playerId);
    String[] playerLocation = beginMedPackEvent.getPlayerLocation();
    roundPlayerMedpackRecord.setPlayerLocationX(new BigDecimal(playerLocation[0]));
    roundPlayerMedpackRecord.setPlayerLocationY(new BigDecimal(playerLocation[1]));
    roundPlayerMedpackRecord.setPlayerLocationZ(new BigDecimal(playerLocation[2]));
    roundPlayerMedpackRecord.setStartTime(Timestamp.valueOf(startTime));
    roundPlayerMedpackRecord.setEndTime(Timestamp.valueOf(endTime));
    roundPlayerMedpackRecord.setDurationSeconds(durationSeconds);
    roundPlayerMedpackRecord.setStartMedpackStatus(beginMedPackStatus);
    roundPlayerMedpackRecord.setEndMedpackStatus(endMedPackStatus);
    roundPlayerMedpackRecord.setHealedPlayerId(healedPlayerId);
    String[] endPlayerLocation = endMedPackEvent.getPlayerLocation();
    roundPlayerMedpackRecord.setEndPlayerLocationX(new BigDecimal(endPlayerLocation[0]));
    roundPlayerMedpackRecord.setEndPlayerLocationY(new BigDecimal(endPlayerLocation[1]));
    roundPlayerMedpackRecord.setEndPlayerLocationZ(new BigDecimal(endPlayerLocation[2]));

    roundPlayerMedpackRecord.insert();
    return roundPlayerMedpackRecord;
  }

  private RoundPlayerRepairRecord addRepairUsage(int roundId, BfEvent beginRepairEvent, BfEvent someEndEvent) {
    RoundPlayerRepairRecord roundPlayerRepairRecord = transaction().newRecord(ROUND_PLAYER_REPAIR);

    int playerId = getPlayerIdFromSlotId(beginRepairEvent.getPlayerSlotId());

    String vehicleType = beginRepairEvent.getStringParamValueByName(BeginRepairParams.vehicle_type.name());

    Integer vehiclePlayerId = null;
    Integer vehiclePlayerSlotId = beginRepairEvent.getIntegerParamValueByName(BeginRepairParams.vehicle_player.name());
    if (vehiclePlayerSlotId != null) {
      vehiclePlayerId = getPlayerIdFromSlotId(vehiclePlayerSlotId);
    }

    LocalDateTime startTime = logStartTime.plus(beginRepairEvent.getDurationSinceLogStart());
    LocalDateTime endTime = logStartTime.plus(someEndEvent.getDurationSinceLogStart());
    int durationSeconds = Long.valueOf(ChronoUnit.SECONDS.between(startTime, endTime)).intValue();

    Integer beginRepairStatus = beginRepairEvent.getIntegerParamValueByName(BeginRepairParams.repair_status.name());
    Integer endRepairStatus = someEndEvent.getIntegerParamValueByName(EndRepairParams.repair_status.name());

    roundPlayerRepairRecord.setRoundId(roundId);
    roundPlayerRepairRecord.setPlayerId(playerId);
    String[] playerLocation = beginRepairEvent.getPlayerLocation();
    roundPlayerRepairRecord.setPlayerLocationX(new BigDecimal(playerLocation[0]));
    roundPlayerRepairRecord.setPlayerLocationY(new BigDecimal(playerLocation[1]));
    roundPlayerRepairRecord.setPlayerLocationZ(new BigDecimal(playerLocation[2]));
    roundPlayerRepairRecord.setStartTime(Timestamp.valueOf(startTime));
    roundPlayerRepairRecord.setEndTime(Timestamp.valueOf(endTime));
    roundPlayerRepairRecord.setDurationSeconds(durationSeconds);
    roundPlayerRepairRecord.setStartRepairStatus(beginRepairStatus);
    roundPlayerRepairRecord.setEndRepairStatus(endRepairStatus);
    roundPlayerRepairRecord.setVehicleType(vehicleType);
    roundPlayerRepairRecord.setVehiclePlayerId(vehiclePlayerId);
    String[] endPlayerLocation = someEndEvent.getPlayerLocation();
    roundPlayerRepairRecord.setEndPlayerLocationX(new BigDecimal(endPlayerLocation[0]));
    roundPlayerRepairRecord.setEndPlayerLocationY(new BigDecimal(endPlayerLocation[1]));
    roundPlayerRepairRecord.setEndPlayerLocationZ(new BigDecimal(endPlayerLocation[2]));

    roundPlayerRepairRecord.insert();
    return roundPlayerRepairRecord;
  }

  // stores previous team interval
  private void parseSetTeamEvent(int roundId, BfEvent e) {
    GamePlayer gamePlayer = getRoundPlayerFromSlotId(e.getPlayerSlotId());

    // adds team-usage for previous team
    addRoundPlayerTeamUsage(roundId, gamePlayer, e.getDurationSinceLogStart());

    Integer team = e.getIntegerParamValueByName(SetTeamParams.team.name());
    gamePlayer.setTeam(team);
  }

  private void setLastEnterVehicle(BfEvent e) {
    if (isSlotIdBot(e.getPlayerSlotId())) {
      // skipping bot vehicle events
      return;
    }

    Integer playerSlotId = e.getPlayerSlotId();

    String newVehicle = e.getStringParamValueByName(EnterVehicleParams.vehicle.name());
    if (lastEnterVehicleByPlayerSlotId.containsKey(playerSlotId)) {
      BfEvent existingEnterEvent = lastEnterVehicleByPlayerSlotId.get(playerSlotId);
      String oldVehicle = existingEnterEvent.getStringParamValueByName(EnterVehicleParams.vehicle.name());
      log.warn(logStartTime + " last enter vehicle event already exists for player slot " + playerSlotId + " (" + existingEnterEvent.getTimestamp() + "= new vehicle: " +
          newVehicle + "(" + e.getTimestamp() + "), old vehicle: " + oldVehicle);
    }

    lastEnterVehicleByPlayerSlotId.put(playerSlotId, e);
  }

  private RoundPlayerVehicleRecord endVehicleUsageIfNeeded(int roundId, BfEvent someEndEvent) {
    if (isSlotIdBot(someEndEvent.getPlayerSlotId())) {
      // skipping bot vehicle events
      return null;
    }

    BfEvent enterVehicle = lastEnterVehicleByPlayerSlotId.remove(someEndEvent.getPlayerSlotId());
    if (enterVehicle != null) {
      return addVehicleUsage(roundId, enterVehicle, someEndEvent.getDurationSinceLogStart());
    }
    return null;
  }

  // todo: rename parent method to endTeamUsage?
  private RoundPlayerTeamRecord addRoundPlayerTeamUsage(int roundId, GamePlayer gamePlayer, Duration teamEnd) {
    int playerId = gamePlayer.getPlayerId();
    int team = gamePlayer.getTeam();
    Duration teamStart = gamePlayer.getTeamStart();
    gamePlayer.setTeamStart(teamEnd);

    log.info("end team usage in round " + roundId + " for player slot " + gamePlayer.getRoundPlayerSlotId() + " for team " + team + " start: " + teamStart.getSeconds() + " end: " + teamEnd.getSeconds());
    LocalDateTime teamStartDate = logStartTime.plus(teamStart);
    LocalDateTime teamEndDate = logStartTime.plus(teamEnd);

    RoundPlayerTeamRecord roundPlayerTeamRecord = transaction().newRecord(ROUND_PLAYER_TEAM);
    roundPlayerTeamRecord.setRoundId(roundId);
    roundPlayerTeamRecord.setPlayerId(playerId);
    roundPlayerTeamRecord.setTeam(team);
    roundPlayerTeamRecord.setStartTime(Timestamp.valueOf(teamStartDate));
    roundPlayerTeamRecord.setEndTime(Timestamp.valueOf(teamEndDate));

    roundPlayerTeamRecord.insert();
    return roundPlayerTeamRecord;
  }

  private RoundPlayerVehicleRecord addVehicleUsage(int roundId, BfEvent enterVehicleEvent, Duration endTimeDurationSinceLogStart) {
    LocalDateTime startTime = logStartTime.plus(enterVehicleEvent.getDurationSinceLogStart());
    LocalDateTime endTime = logStartTime.plus(endTimeDurationSinceLogStart);
    int durationSeconds = Long.valueOf(ChronoUnit.SECONDS.between(startTime, endTime)).intValue();
    String vehicle = enterVehicleEvent.getStringParamValueByName(EnterVehicleParams.vehicle.name());
    int playerId = getPlayerIdFromSlotId(enterVehicleEvent.getPlayerSlotId());

    // might be able to use <bf:param type="int" name="pco_id">0</bf:param> 0 if driver??
    // maybe non-driver is reflected in vehicle name as well
    // also it's probably possible that changing seats does not trigger some events, so lets skip this feature

    RoundPlayerVehicleRecord roundPlayerVehicleRecord = transaction().newRecord(ROUND_PLAYER_VEHICLE);
    roundPlayerVehicleRecord.setRoundId(roundId);
    roundPlayerVehicleRecord.setPlayerId(playerId);
    String[] playerLocation = enterVehicleEvent.getPlayerLocation();
    roundPlayerVehicleRecord.setPlayerLocationX(new BigDecimal(playerLocation[0]));
    roundPlayerVehicleRecord.setPlayerLocationY(new BigDecimal(playerLocation[1]));
    roundPlayerVehicleRecord.setPlayerLocationZ(new BigDecimal(playerLocation[2]));
    roundPlayerVehicleRecord.setStartTime(Timestamp.valueOf(startTime));
    roundPlayerVehicleRecord.setEndTime(Timestamp.valueOf(endTime));
    roundPlayerVehicleRecord.setDurationSeconds(durationSeconds);
    roundPlayerVehicleRecord.setVehicle(vehicle);

    roundPlayerVehicleRecord.insert();

    return roundPlayerVehicleRecord;
  }

  private void setLastKillEvent(BfEvent e) {
    Integer victimId = e.getIntegerParamValueByName(ScoreEventParams.victim_id.name());

    if (lastKillEventByVictimId.containsKey(victimId)) {
      Integer oldKillerId = lastKillEventByVictimId.get(victimId).getPlayerSlotId();
      log.warn("last kill event already exists for victim " + victimId + " new killer: " +
          e.getPlayerSlotId() + ", old killer: " + oldKillerId);
    }

    lastKillEventByVictimId.put(victimId, e);
  }

  private BfEvent getLastKillEventForVictim(int wantedVictimSlotId) {
    // kill event was for specific victim_id, so safe to remove it now
    BfEvent killEvent = lastKillEventByVictimId.remove(wantedVictimSlotId);
    if (killEvent == null) {
      //if (!lastKillEventByVictimId.isEmpty()) {
      // this could happen when you kill somebody, but die yourself as well
      // or theoretically I guess also if killing many players at once and kill-death events are somewhat out of order
      //log.warn(logStartTime + " could not find last kill event for " + wantedVictimSlotId + ", but there are kill events in queue " + lastKillEventByVictimId);
      //}
      return null;
    }

    return killEvent;
  }

  private void parseDeathEvent(int roundId, BfEvent e) {
    BfEvent killOrTkEvent = getLastKillEventForVictim(e.getPlayerSlotId());

    // skip dying bot if killer is not known or is also a bot
    if (isSlotIdBot(e.getPlayerSlotId())) {
      if (killOrTkEvent == null || isSlotIdBot(killOrTkEvent.getPlayerSlotId())) {
        return;
      }
    }

    addPlayerDeath(roundId, e, killOrTkEvent);
  }

  private static boolean isSlotIdBot(int slotId) {
    return slotId > 127 || slotId == FAKE_BOT_SLOT_ID;
  }

  private GamePlayer getRoundPlayerFromSlotId(int slotId) {
    if (isSlotIdBot(slotId)) {
      slotId = FAKE_BOT_SLOT_ID; // treat bots as a single fake player
    }
    return activePlayersByRoundPlayerId.get(slotId);
  }

  private int getPlayerIdFromSlotId(int slotId) {
    GamePlayer gamePlayer = getRoundPlayerFromSlotId(slotId);
    return requireNonNull(gamePlayer.getPlayerId(), "cant find player id for slot " + slotId);
  }

  private void parseEventScoreEvent(int roundId, BfEvent e) {
    if (isSlotIdBot(e.getPlayerSlotId())) {
      return;
    }
    addRoundPlayerScoreEvent(roundId, e);
  }

  private void parseEventChat(int roundId, BfEvent e) {
    addChatMessage(roundId, e);
  }

  private void parsePickupKit(int roundId, BfEvent e) {
    if (isSlotIdBot(e.getPlayerSlotId())) {
      return;
    }

    addPickupKit(roundId, e);
  }

  private void parseDeployObject(int roundId, BfEvent e) {
    addDeployObject(roundId, e);
  }

  // not used
  private void parseRestartMap(int roundId, BfEvent e) {
    int ticketsTeam1 = e.getIntegerParamValueByName(RestartMapParams.tickets_team1.name());
    int ticketsTeam2 = e.getIntegerParamValueByName(RestartMapParams.tickets_team2.name());
  }

  // not used
  private void parseEndGameEvent(int roundId, BfEvent e) {
    // reason is "timeLimit" or "tickets"
    String reason = e.getStringParamValueByName(EndGameParams.reason.name());
    Integer winner = e.getIntegerParamValueByName(EndGameParams.winner.name());
    int winnerScore = e.getIntegerParamValueByName(EndGameParams.winnerScore.name());
    int loserScore = e.getIntegerParamValueByName(EndGameParams.loserScore.name());
  }

  // not used
  private void parseRadioMessage(int roundId, BfEvent e) {
    int playerId = getPlayerIdFromSlotId(e.getPlayerSlotId());
    int messageId = e.getIntegerParamValueByName(RadioMessageParams.message.name());
    int broadcast = e.getIntegerParamValueByName(RadioMessageParams.broadcast.name());
  }

  // not used
  private void parseGamePaused(int roundId, BfEvent e) {
    LocalDateTime eventTime = logStartTime.plus(e.getDurationSinceLogStart());
  }

  // not used
  private void parseGameUnpaused(int roundId, BfEvent e) {
    LocalDateTime eventTime = logStartTime.plus(e.getDurationSinceLogStart());
  }

  private void parseEventChangePlayerName(BfEvent e) {
    int playerId = getPlayerIdFromSlotId(e.getPlayerSlotId());
    String newPlayerName = e.getStringParamValueByName(ChangePlayerNameParams.name.name());
    addNickname(playerId, newPlayerName);
  }

  private void parseEventDestroyPlayer(int roundId, BfEvent destroyPlayerEvent) {
    if (isSlotIdBot(destroyPlayerEvent.getPlayerSlotId())) {
      return;
    }

    GamePlayer gamePlayer = activePlayersByRoundPlayerId.remove(destroyPlayerEvent.getPlayerSlotId());
    log.info("removed round-player " + destroyPlayerEvent.getPlayerSlotId());

    if (gamePlayer.getPlayerId() == null) {
      log.info("Player " + gamePlayer.getName() + " has disconnected before playerKeyHash");
      return;
    }

    addRoundPlayerTeamUsage(roundId, gamePlayer, destroyPlayerEvent.getDurationSinceLogStart());
    addGamePlayerJoinEndDates(roundId, gamePlayer, destroyPlayerEvent.getDurationSinceLogStart());
  }

  private void parseEventPlayerKeyHash(BfEvent e) {
    GamePlayer gamePlayer = activePlayersByRoundPlayerId.get(e.getPlayerSlotId());
    requireNonNull(gamePlayer, "playerKeyHash there should've been createPlayer " + e.getPlayerSlotId());

    gamePlayer.setKeyhash(e.getStringParamValueByName(PlayerKeyHashParams.keyhash.name()));
    PlayerRecord playerRecord = addPlayerOrNickName(gamePlayer.getName(), gamePlayer.getKeyhash());
    gamePlayer.setPlayerId(playerRecord.getId());
  }

  private void parseEventCreatePlayer(Integer roundId, BfEvent e) {
    if (activePlayersByRoundPlayerId.containsKey(e.getPlayerSlotId())) {
      throw new IllegalStateException("destroyPlayer event should've deleted player with id " + e.getPlayerSlotId());
    }
    GamePlayer gamePlayer = new GamePlayer()
        .setRoundPlayerSlotId(e.getPlayerSlotId())
        .setName(e.getStringParamValueByName(CreatePlayerParams.name.name()))
        .setKeyhash(null)
        .setJoined(e.getDurationSinceLogStart())
        .setTeam(e.getIntegerParamValueByName(CreatePlayerParams.team.name()))
        .setTeamStart(e.getDurationSinceLogStart())
        .setJoinedRoundId(roundId);
    activePlayersByRoundPlayerId.put(e.getPlayerSlotId(), gamePlayer);
    log.info("added round-player " + e.getPlayerSlotId());
  }

  /**
   * @param deathEvent "DeathNoMsg" or "Death" score event type, should not be null, unless I'm wrong
   * @param killEvent  nullable, "Kill" or "TK" score event type, can be null if player just died
   */
  private RoundPlayerDeathRecord addPlayerDeath(int roundId, BfEvent deathEvent, @Nullable BfEvent killEvent) {
    LocalDateTime eventTime = logStartTime.plus(deathEvent.getDurationSinceLogStart());

    RoundPlayerDeathRecord roundPlayerDeathRecord = transaction().newRecord(ROUND_PLAYER_DEATH);
    roundPlayerDeathRecord.setRoundId(roundId);
    int deathPlayerId = getPlayerIdFromSlotId(deathEvent.getPlayerSlotId());
    roundPlayerDeathRecord.setPlayerId(deathPlayerId);

    String[] victimLocation = deathEvent.getPlayerLocation();
    roundPlayerDeathRecord.setPlayerLocationX(new BigDecimal(victimLocation[0]));
    roundPlayerDeathRecord.setPlayerLocationY(new BigDecimal(victimLocation[1]));
    roundPlayerDeathRecord.setPlayerLocationZ(new BigDecimal(victimLocation[2]));
    roundPlayerDeathRecord.setEventTime(Timestamp.valueOf(eventTime));

    if (killEvent != null) {
      int killerPlayerId = getPlayerIdFromSlotId(killEvent.getPlayerSlotId());
      roundPlayerDeathRecord.setKillerPlayerId(killerPlayerId);
      String[] killerLocation = killEvent.getPlayerLocation();
      roundPlayerDeathRecord.setKillerLocationX(new BigDecimal(killerLocation[0]));
      roundPlayerDeathRecord.setKillerLocationY(new BigDecimal(killerLocation[1]));
      roundPlayerDeathRecord.setKillerLocationZ(new BigDecimal(killerLocation[2]));

      String scoreType = killEvent.getStringParamValueByName(ScoreEventParams.score_type.name());
      roundPlayerDeathRecord.setKillType(scoreType); // Kill or TK

      String weapon = killEvent.getStringParamValueByName(ScoreEventParams.weapon.name());
      if ("(none)".equals(weapon)) {
        weapon = null;
      }
      roundPlayerDeathRecord.setKillWeapon(weapon);
    }

    roundPlayerDeathRecord.insert();
    return roundPlayerDeathRecord;
  }

  private GamePlayerRecord addGamePlayerJoinEndDates(int endRoundId, GamePlayer gamePlayer, Duration endTime) {
    LocalDateTime joined = logStartTime.plus(gamePlayer.getJoined());
    LocalDateTime ended = logStartTime.plus(endTime);
    Integer joinedRoundId = gamePlayer.getJoinedRoundId();

    GamePlayerRecord gamePlayerRecord = transaction().newRecord(GAME_PLAYER);
    gamePlayerRecord.setGameId(gameRecordId);
    gamePlayerRecord.setJoinedRoundId(joinedRoundId);
    gamePlayerRecord.setEndRoundId(endRoundId);
    gamePlayerRecord.setPlayerId(gamePlayer.getPlayerId());
    gamePlayerRecord.setStartTime(Timestamp.valueOf(joined));
    gamePlayerRecord.setEndTime(Timestamp.valueOf(ended));

    gamePlayerRecord.insert();
    return gamePlayerRecord;
  }

  private RoundPlayerScoreEventRecord addRoundPlayerScoreEvent(int roundId, BfEvent e) {
    int playerId = getPlayerIdFromSlotId(e.getPlayerSlotId());
    LocalDateTime eventTime = logStartTime.plus(e.getDurationSinceLogStart());
    String scoreType = e.getStringParamValueByName(ScoreEventParams.score_type.name());

    RoundPlayerScoreEventRecord roundPlayerScoreEventRecord = transaction().newRecord(ROUND_PLAYER_SCORE_EVENT);
    roundPlayerScoreEventRecord.setRoundId(roundId);
    roundPlayerScoreEventRecord.setPlayerId(playerId);
    String[] playerLocation = e.getPlayerLocation();
    roundPlayerScoreEventRecord.setPlayerLocationX(new BigDecimal(playerLocation[0]));
    roundPlayerScoreEventRecord.setPlayerLocationY(new BigDecimal(playerLocation[1]));
    roundPlayerScoreEventRecord.setPlayerLocationZ(new BigDecimal(playerLocation[2]));
    roundPlayerScoreEventRecord.setEventTime(Timestamp.valueOf(eventTime));
    roundPlayerScoreEventRecord.setScoreType(scoreType);

    roundPlayerScoreEventRecord.insert();
    return roundPlayerScoreEventRecord;
  }

  @Data
  @Accessors(chain = true)
  public static class GamePlayer {
    int roundPlayerSlotId;
    Integer playerId;
    String name;
    String keyhash;
    Duration joined;
    Duration teamStart;
    Integer team;
    Integer joinedRoundId;

    public GamePlayer setTeamStart(Duration teamStart) {
      this.teamStart = teamStart;
      log.info("was setting for player slot " + roundPlayerSlotId + " team start to " + teamStart.getSeconds());
      return this;
    }
  }

  private RoundEndStatsPlayerRecord addRoundEndStatsPlayer(Integer roundId, BfRoundStats.BfPlayerStat bfPlayerStat,
                                                           int rank) {
    int playerId = getPlayerIdFromSlotId(bfPlayerStat.getPlayerSlotId());

    RoundEndStatsPlayerRecord roundEndStatsPlayerRecord = transaction().newRecord(ROUND_END_STATS_PLAYER);
    roundEndStatsPlayerRecord.setRoundId(roundId);
    roundEndStatsPlayerRecord.setPlayerId(playerId);
    roundEndStatsPlayerRecord.setPlayerName(bfPlayerStat.getPlayerName());
    roundEndStatsPlayerRecord.setIsAi(bfPlayerStat.isAi() ? 1 : 0);
    roundEndStatsPlayerRecord.setRank(rank);
    roundEndStatsPlayerRecord.setTeam(bfPlayerStat.getTeam());
    roundEndStatsPlayerRecord.setScore(bfPlayerStat.getScore());
    roundEndStatsPlayerRecord.setKills(bfPlayerStat.getKills());
    roundEndStatsPlayerRecord.setDeaths(bfPlayerStat.getDeaths());
    roundEndStatsPlayerRecord.setTks(bfPlayerStat.getTks());
    roundEndStatsPlayerRecord.setCaptures(bfPlayerStat.getCaptures());
    roundEndStatsPlayerRecord.setAttacks(bfPlayerStat.getAttacks());
    roundEndStatsPlayerRecord.setDefences(bfPlayerStat.getDefences());

    roundEndStatsPlayerRecord.insert();
    return roundEndStatsPlayerRecord;
  }

  private RoundEndStatsRecord addRoundEndStats(Integer roundId, BfRoundStats bfRoundStats) {
    LocalDateTime roundEndTime = logStartTime.plus(bfRoundStats.getDurationSinceLogStart());

    RoundEndStatsRecord roundEndStatsRecord = transaction().newRecord(ROUND_END_STATS);
    roundEndStatsRecord.setRoundId(roundId);
    roundEndStatsRecord.setEndTime(Timestamp.valueOf(roundEndTime));
    roundEndStatsRecord.setWinningTeam(bfRoundStats.getWinningTeam());
    roundEndStatsRecord.setVictoryType(bfRoundStats.getVictoryType());
    roundEndStatsRecord.setEndTicketsTeam_1(bfRoundStats.getTicketsForTeam1());
    roundEndStatsRecord.setEndTicketsTeam_2(bfRoundStats.getTicketsForTeam2());

    roundEndStatsRecord.insert();
    return roundEndStatsRecord;
  }

  private RoundPlayerPickupKitRecord addPickupKit(Integer roundId, BfEvent bfEvent) {
    int playerId = getPlayerIdFromSlotId(bfEvent.getPlayerSlotId());
    LocalDateTime eventTime = logStartTime.plus(bfEvent.getDurationSinceLogStart());

    String kit = bfEvent.getStringParamValueByName(PickupKitParams.kit.name());

    RoundPlayerPickupKitRecord roundPlayerPickupKitRecord = transaction().newRecord(ROUND_PLAYER_PICKUP_KIT);
    roundPlayerPickupKitRecord.setRoundId(roundId);
    roundPlayerPickupKitRecord.setPlayerId(playerId);
    if (bfEvent.getPlayerLocation() != null) {
      String[] playerLocation = bfEvent.getPlayerLocation();
      roundPlayerPickupKitRecord.setPlayerLocationX(new BigDecimal(playerLocation[0]));
      roundPlayerPickupKitRecord.setPlayerLocationY(new BigDecimal(playerLocation[1]));
      roundPlayerPickupKitRecord.setPlayerLocationZ(new BigDecimal(playerLocation[2]));
    }
    roundPlayerPickupKitRecord.setEventTime(Timestamp.valueOf(eventTime));
    roundPlayerPickupKitRecord.setKit(kit);

    roundPlayerPickupKitRecord.insert();
    return roundPlayerPickupKitRecord;
  }

  private RoundPlayerDeployObjectRecord addDeployObject(int roundId, BfEvent bfEvent) {
    int playerId = getPlayerIdFromSlotId(bfEvent.getPlayerSlotId());
    LocalDateTime eventTime = logStartTime.plus(bfEvent.getDurationSinceLogStart());
    String objectType = bfEvent.getStringParamValueByName(DeployObjectParams.type.name());

    RoundPlayerDeployObjectRecord roundPlayerDeployObjectRecord = transaction().newRecord(ROUND_PLAYER_DEPLOY_OBJECT);
    roundPlayerDeployObjectRecord.setRoundId(roundId);
    roundPlayerDeployObjectRecord.setPlayerId(playerId);
    if (bfEvent.getPlayerLocation() != null) {
      String[] playerLocation = bfEvent.getPlayerLocation();
      roundPlayerDeployObjectRecord.setPlayerLocationX(new BigDecimal(playerLocation[0]));
      roundPlayerDeployObjectRecord.setPlayerLocationY(new BigDecimal(playerLocation[1]));
      roundPlayerDeployObjectRecord.setPlayerLocationZ(new BigDecimal(playerLocation[2]));
    }
    roundPlayerDeployObjectRecord.setEventTime(Timestamp.valueOf(eventTime));
    roundPlayerDeployObjectRecord.setObject(objectType);

    roundPlayerDeployObjectRecord.insert();
    return roundPlayerDeployObjectRecord;
  }

  private RoundChatLogRecord addChatMessage(Integer roundId, BfEvent bfEvent) {
    int playerId = getPlayerIdFromSlotId(bfEvent.getPlayerSlotId());
    String message = bfEvent.getStringParamValueByName(ChatEventParams.text.name());
    LocalDateTime eventTime = logStartTime.plus(bfEvent.getDurationSinceLogStart());

    log.info("Adding chat to round " + roundId + " player slot " + bfEvent.getPlayerSlotId() + " @ " + bfEvent.getDurationSinceLogStart().getSeconds()
        + " " + message
    );

    RoundChatLogRecord roundChatLogRecord = transaction().newRecord(ROUND_CHAT_LOG);
    roundChatLogRecord.setRoundId(roundId);
    roundChatLogRecord.setPlayerId(playerId);
    if (bfEvent.getPlayerLocation() != null) {
      String[] playerLocation = bfEvent.getPlayerLocation();
      roundChatLogRecord.setPlayerLocationX(new BigDecimal(playerLocation[0]));
      roundChatLogRecord.setPlayerLocationY(new BigDecimal(playerLocation[1]));
      roundChatLogRecord.setPlayerLocationZ(new BigDecimal(playerLocation[2]));
    }

    roundChatLogRecord.setToTeam(bfEvent.getIntegerParamValueByName(ChatEventParams.team.name()));
    roundChatLogRecord.setMessage(message);
    roundChatLogRecord.setEventTime(Timestamp.valueOf(eventTime));

    roundChatLogRecord.insert();
    return roundChatLogRecord;
  }

  private PlayerRecord addPlayerOrNickName(@Nonnull String name, String keyHash) {
    PlayerRecord playerRecord = transaction().selectFrom(PLAYER).where(PLAYER.KEYHASH.eq(keyHash)).fetchOne();

    if (playerRecord == null) {
      playerRecord = transaction().newRecord(PLAYER);
      playerRecord.setName(name);
      playerRecord.setKeyhash(keyHash);
      playerRecord.insert();
    } else {
      if (STANDARD_NAMES.contains(name)) {
        // don't prioritize default names, stay on previous active one
      } else {
        // update the "active" nickname
        playerRecord.setName(name);
        playerRecord.update();
      }
    }

    addNickname(playerRecord.getId(), name);

    return playerRecord;
  }

  private PlayerNicknameRecord addNickname(int playerId, String nickname) {
    PlayerNicknameRecord playerNicknameRecord = transaction().selectFrom(PLAYER_NICKNAME)
        .where(PLAYER_NICKNAME.PLAYER_ID.eq(playerId))
        .and(PLAYER_NICKNAME.NICKNAME.eq(nickname))
        .fetchOne();

    if (playerNicknameRecord == null) {
      playerNicknameRecord = transaction().newRecord(PLAYER_NICKNAME);
      playerNicknameRecord.setPlayerId(playerId);
      playerNicknameRecord.setNickname(nickname);
      playerNicknameRecord.setTimesUsed(1);
      playerNicknameRecord.insert();
    } else {
      playerNicknameRecord.setTimesUsed(playerNicknameRecord.getTimesUsed() + 1);
      playerNicknameRecord.update();
    }

    return playerNicknameRecord;
  }

  private GameRecord createGameRecord(int serverId, BfRound firstRound) {
    GameRecord gameRecord = transaction().newRecord(GAME);

    gameRecord.setServerId(serverId);
    gameRecord.setStartTime(Timestamp.valueOf(logStartTime));
    gameRecord.setGameCode(engine.startsWith("BFVietnam") ? "bfvietnam" : "bf1942"); // could also use firstRound.getGame()
    gameRecord.setServerName(firstRound.getServerName());
    gameRecord.setServerPort(firstRound.getPort());
    gameRecord.setModId(firstRound.getModId());
    gameRecord.setMapCode(firstRound.getMap());
    gameRecord.setGameMode(firstRound.getGameMode());
    gameRecord.setMaxGameTime(firstRound.getMaxGameTime());
    gameRecord.setMaxPlayers(firstRound.getMaxPlayers());
    gameRecord.setScoreLimit(firstRound.getScoreLimit());
    gameRecord.setNoOfRounds(firstRound.getNumberOfRoundsPerMap());
    gameRecord.setSpawnTime(firstRound.getSpawnTime());
    gameRecord.setSpawnDelay(firstRound.getSpawnDelay());
    gameRecord.setGameStartDelay(firstRound.getGameStartDelay());
    gameRecord.setRoundStartDelay(firstRound.getRoundStartDelay());
    gameRecord.setSoldierFf(firstRound.getSoldierFriendlyFire());
    gameRecord.setSoldierFfOnSplash(firstRound.getSoldierFriendlyFireOnSplash());
    gameRecord.setVehicleFf(firstRound.getVehicleFriendlyFire());
    gameRecord.setVehicleFfOnSplash(firstRound.getVehicleFriendlyFireOnSplash());
    gameRecord.setFfKickback(firstRound.getFriendlyFireKickback());
    gameRecord.setFfKickbackOnSplash(firstRound.getFriendlyFireKickbackOnSplash());
    gameRecord.setTicketRatio(firstRound.getTicketRatio());
    gameRecord.setTeamKillPunish(firstRound.isTeamKillPunished() ? 1 : 0);
    gameRecord.setPunkbusterEnabled(firstRound.isPunkBusterEnabled() ? 1 : 0);
    gameRecord.setAutoBalanceEnabled(firstRound.isAutoBalanceEnabled() ? 1 : 0);
    gameRecord.setTagDistance(firstRound.getTagDistance());
    gameRecord.setTagDistanceScope(firstRound.getTagDistanceScope());
    gameRecord.setNoseCameraAllowed(firstRound.isNoseCameraAllowed() ? 1 : 0);
    gameRecord.setFreeCameraAllowed(firstRound.isFreeCameraAllowed() ? 1 : 0);
    gameRecord.setExternalViewsAllowed(firstRound.isExternalViewsAllowed() ? 1 : 0);
    gameRecord.setHitIndicationEnabled(firstRound.isHitIndicationEnabled() ? 1 : 0);
    gameRecord.setInternet(firstRound.isInternet() ? 1 : 0);
    gameRecord.setCoopCpu(firstRound.getCoopCpu());
    gameRecord.setCoopSkill(firstRound.getCoopSkill());
    gameRecord.setAlliedPlayerCountRatio(firstRound.getAlliedPlayerCountRatio());
    gameRecord.setAxisPlayerCountRatio(firstRound.getAxisPlayerCountRatio());

    gameRecord.insert();
    return gameRecord;
  }

  private RoundRecord addRound(BfRound bfRound, int gameId) {
    BfEvent roundInitEvent = bfRound.getEvents().stream()
        .filter(e -> e.getEventName() == EventName.roundInit)
        .findFirst().orElse(null);

    Integer ticketsTeam1;
    Integer ticketsTeam2;
    Duration roundStartSinceLogStart;

    // sometimes round ends before roundInit event is even fired
    if (roundInitEvent == null) {
      // chances are that these are the same as they will be in bfRound.getRoundStats() as non of the decreases
      // but I don't think these will be useful rounds anyway, so let's just set to -1 to distinguish instead of hoping
      // that there will be a round end event
      ticketsTeam1 = -1;
      ticketsTeam2 = -1;
      // as we can't take round start time from the roundInit event, we can get a very similar time from
      // round start time + game start delay
      roundStartSinceLogStart = bfRound.getDurationSinceLogStart().plusSeconds(bfRound.getGameStartDelay());
    } else {
      ticketsTeam1 = roundInitEvent.getIntegerParamValueByName(RoundInitParams.tickets_team1.name());
      ticketsTeam2 = roundInitEvent.getIntegerParamValueByName(RoundInitParams.tickets_team2.name());
      roundStartSinceLogStart = roundInitEvent.getDurationSinceLogStart();
    }

    // Create a new record (TODO: remove attributes from round that are already present in game record?)
    RoundRecord round = transaction().newRecord(ROUND);
    round.setGameId(gameId);
    round.setStartTime(Timestamp.valueOf(logStartTime.plus(roundStartSinceLogStart)));
    round.setStartTicketsTeam_1(ticketsTeam1);
    round.setStartTicketsTeam_2(ticketsTeam2);
    round.setServerName(bfRound.getServerName());
    round.setServerPort(bfRound.getPort());
    round.setGameCode(engine.startsWith("BFVietnam") ? "bfvietnam" : "bf1942"); // could also use firstRound.getGame()
    round.setModId(bfRound.getModId());
    round.setMapCode(bfRound.getMap());
    round.setGameMode(bfRound.getGameMode());
    round.setMaxGameTime(bfRound.getMaxGameTime());
    round.setMaxPlayers(bfRound.getMaxPlayers());
    round.setScoreLimit(bfRound.getScoreLimit());
    round.setNoOfRounds(bfRound.getNumberOfRoundsPerMap());
    round.setSpawnTime(bfRound.getSpawnTime());
    round.setSpawnDelay(bfRound.getSpawnDelay());
    round.setGameStartDelay(bfRound.getGameStartDelay());
    round.setRoundStartDelay(bfRound.getRoundStartDelay());
    round.setSoldierFf(bfRound.getSoldierFriendlyFire());
    round.setSoldierFfOnSplash(bfRound.getSoldierFriendlyFireOnSplash());
    round.setVehicleFf(bfRound.getVehicleFriendlyFire());
    round.setVehicleFfOnSplash(bfRound.getVehicleFriendlyFireOnSplash());
    round.setFfKickback(bfRound.getFriendlyFireKickback());
    round.setFfKickbackOnSplash(bfRound.getFriendlyFireKickbackOnSplash());
    round.setTicketRatio(bfRound.getTicketRatio());
    round.setTeamKillPunish(bfRound.isTeamKillPunished() ? 1 : 0);
    round.setPunkbusterEnabled(bfRound.isPunkBusterEnabled() ? 1 : 0);
    round.setAutoBalanceEnabled(bfRound.isAutoBalanceEnabled() ? 1 : 0);
    round.setTagDistance(bfRound.getTagDistance());
    round.setTagDistanceScope(bfRound.getTagDistanceScope());
    round.setNoseCameraAllowed(bfRound.isNoseCameraAllowed() ? 1 : 0);
    round.setFreeCameraAllowed(bfRound.isFreeCameraAllowed() ? 1 : 0);
    round.setExternalViewsAllowed(bfRound.isExternalViewsAllowed() ? 1 : 0);
    round.setHitIndicationEnabled(bfRound.isHitIndicationEnabled() ? 1 : 0);
    round.setInternet(bfRound.isInternet() ? 1 : 0);
    round.setCoopCpu(bfRound.getCoopCpu());
    round.setCoopSkill(bfRound.getCoopSkill());
    round.setAlliedPlayerCountRatio(bfRound.getAlliedPlayerCountRatio());
    round.setAxisPlayerCountRatio(bfRound.getAxisPlayerCountRatio());

    round.insert();
    return round;
  }
}
