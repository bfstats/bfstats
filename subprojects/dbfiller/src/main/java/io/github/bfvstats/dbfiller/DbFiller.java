package io.github.bfvstats.dbfiller;

import io.github.bfvstats.game.jooq.tables.records.*;
import io.github.bfvstats.logparser.XmlParser;
import io.github.bfvstats.logparser.xml.BfEvent;
import io.github.bfvstats.logparser.xml.BfLog;
import io.github.bfvstats.logparser.xml.BfRound;
import io.github.bfvstats.logparser.xml.BfRoundStats;
import io.github.bfvstats.logparser.xml.enums.EventName;
import io.github.bfvstats.logparser.xml.enums.event.*;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.InflaterInputStream;

import static io.github.bfvstats.game.jooq.Tables.*;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

@Slf4j
public class DbFiller {
  public static final int FAKE_BOT_SLOT_ID = -1;
  public static final int FAKE_BOT_TEAM = -1;

  private static DSLContext dslContext;
  private static Connection connection;

  private DSLContext transactionDslContext;

  private final BfLog bfLog;
  private final LocalDateTime logStartTime;

  // actually not per round, but per log file
  private Map<Integer, RoundPlayer> activePlayersByRoundPlayerId = new HashMap<>();

  private Map<Integer, BfEvent> lastKillEventByVictimId = new HashMap<>();
  private Map<Integer, BfEvent> lastEnterVehicleByPlayerSlotId = new HashMap<>();

  public DbFiller(BfLog bfLog) {
    this.bfLog = bfLog;
    this.logStartTime = bfLog.getTimestampAsDate();
  }

  private DSLContext transaction() {
    return transactionDslContext;
  }

  public static void main(String[] args) throws JAXBException, FileNotFoundException, SQLException {
    System.setProperty("org.jooq.no-logo", "true");

    String logDirPath = "D:\\bflogs\\";
    //String xmlFilePath = "D:\\bflogs\\ev_15567-20170105_2340.xml";

    prepareConnection();
    try {
      //addFromXmlFile(xmlFilePath);
      parseAllInDir(logDirPath);
    } finally {
      closeConnection();
    }

  }

  public static void parseAllInDir(String logDirPath) {
    File logDir = new File(logDirPath);
    File[] dirFiles = logDir.listFiles((dir, name) -> name.endsWith(".xml") || name.endsWith(".zxml"));
    if (dirFiles == null) {
      throw new IllegalArgumentException("could not list files in " + logDirPath);
    }

    int totalNumberOfFiles = dirFiles.length;
    int numberOfFilesCompleted = 0;

    for (File fileI : dirFiles) {
      if (numberOfFilesCompleted % 30 == 0) {
        System.out.println(numberOfFilesCompleted + "/" + totalNumberOfFiles);
      }
      numberOfFilesCompleted++;
      String filePath = fileI.getPath();
      filePath = extractIfNecessary(filePath);
      if (filePath != null) {
        addFromXmlFile(filePath);
      }
    }
  }

  private static String extractIfNecessary(String filePath) {
    if (filePath.endsWith(".xml")) {
      Path checkablePath = Paths.get(filePath.substring(0, filePath.length() - 4) + ".zxml");
      boolean hasZxmlCounterpart = Files.exists(checkablePath);
      if (hasZxmlCounterpart) {
        // TODO: maybe vice-versa - should ignore zxml files instead
        log.info("ignoring " + filePath + " because zxml will be extracted again");
        // skipping xml, as zxml also exists, so will wait for unzipping that again
        return null;
      } else {
        return filePath;
      }
    } else if (filePath.endsWith(".zxml")) {
      try {
        return unzip(filePath); // replace with .xml counterpart
      } catch (IOException e) {
        log.warn("looks like " + filePath + " is not complete. " + e.getMessage());
        return null;
      }
    }
    return null;
  }

  private static void prepareConnection() {
    try {
      connection = DriverManager.getConnection("jdbc:sqlite:baas.db");
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

  private static void addFromXmlFile(String xmlFilePath) {
    try {
      log.info("Parsing " + xmlFilePath);
      File file = new File(xmlFilePath);
      BfLog bfLog = XmlParser.parseXmlLogFile(file);
      DbFiller dbFiller = new DbFiller(bfLog);
      dbFiller.fillDb();
    } catch (FileNotFoundException | SQLException | JAXBException e) {
      throw new RuntimeException(e);
    }
  }

  public static String unzip(String filepath) throws IOException {
    String xmlFilePath = filepath.substring(0, filepath.length() - 5) + ".xml";

    int blockSize = 8192;

    try (InflaterInputStream zipin = new InflaterInputStream(new FileInputStream(filepath));
         FileOutputStream out = new FileOutputStream(xmlFilePath)
    ) {
      byte[] buffer = new byte[blockSize];
      int length;
      while ((length = zipin.read(buffer, 0, blockSize)) > 0) {
        out.write(buffer, 0, length);
      }
    }

    return xmlFilePath;
  }

  private void fillDb() throws SQLException {
    dslContext.transaction(configuration -> {
      this.transactionDslContext = DSL.using(configuration);

      PlayerRecord botPlayerRecord = addPlayerOrNickName("FAKE_BOT", "FAKE_BOT");
      Integer botPlayerId = botPlayerRecord.getId();
      RoundPlayer botRoundPlayer = new RoundPlayer()
          .setRoundPlayerSlotId(FAKE_BOT_SLOT_ID)
          .setPlayerId(botPlayerId)
          .setName("FAKE_BOT")
          .setKeyhash("FAKE_BOT")
          .setTeam(FAKE_BOT_TEAM);
      activePlayersByRoundPlayerId.put(botRoundPlayer.getRoundPlayerSlotId(), botRoundPlayer);

      parseLog();
    });

    if (!lastKillEventByVictimId.isEmpty()) {
      log.warn("last kill events is not empty in the end of the round " + lastKillEventByVictimId);
    }
  }

  private void parseLog() {
    int lastRoundId = -1;
    for (Object child : bfLog.getRootEventsAndRounds()) {
      if (child instanceof BfEvent) {
        BfEvent bfEvent = (BfEvent) child;
        parseEvent(lastRoundId, bfEvent);
      } else if (child instanceof BfRound) {
        BfRound bfRound = (BfRound) child;
        int roundId = parseRound(bfRound, lastRoundId);
        lastRoundId = roundId;
      } else if (child instanceof String) {
        if (!child.toString().equals("\n")) {
          log.warn("got non-newline string child " + child.toString());
        }
      } else {
        log.warn("unhandled child type" + child.toString());
      }
    }
  }

  private void forceEndAllVehicleEvents(int roundId, Duration endTimeDurationSinceLogStart) {
    lastEnterVehicleByPlayerSlotId.forEach((playerSlotId, enterVehicle) ->
        addVehicleUsage(roundId, enterVehicle, endTimeDurationSinceLogStart)
    );
    lastEnterVehicleByPlayerSlotId.clear();
  }

  private int parseRound(BfRound bfRound, int previousRoundId) {
    RoundRecord roundRecord = addRound(bfRound);
    int roundId = requireNonNull(roundRecord.getId());

    if (previousRoundId != -1) {
      // put team between two rounds into the first round
      // kind of waste of db - should merge this and previous round team usage
      recordEndGame(previousRoundId, bfRound.getDurationSinceLogStart());
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
      recordEndGame(roundId, teamEndDur);
    }

    return roundId;
  }

  private void recordEndGame(int endingRoundId, Duration endingRoundEndTime) {
    for (RoundPlayer roundPlayer : activePlayersByRoundPlayerId.values()) {
      if (isSlotIdBot(roundPlayer.getRoundPlayerSlotId())) {
        continue;
      }
      if (roundPlayer.getTeam() == null || roundPlayer.getPlayerId() == null) {
        continue;
      }
      addRoundPlayerTeamUsage(endingRoundId, roundPlayer, endingRoundEndTime);
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
        break;
      case setTeam:
        parseSetTeamEvent(roundId, e);
        break;
      case spawnEvent:
        if (!isSlotIdBot(e.getPlayerSlotId())) {
          RoundPlayer roundPlayer = getRoundPlayerFromSlotId(e.getPlayerSlotId());
          Integer spawnTeam = e.getIntegerParamValueByName("team");
          if (!roundPlayer.getTeam().equals(spawnTeam)) {
            addRoundPlayerTeamUsage(roundId, roundPlayer, e.getDurationSinceLogStart());
            roundPlayer.setTeam(spawnTeam);
          }
        }
        break;
    }
  }

  // stores previous team interval
  private void parseSetTeamEvent(int roundId, BfEvent e) {
    RoundPlayer roundPlayer = getRoundPlayerFromSlotId(e.getPlayerSlotId());

    addRoundPlayerTeamUsage(roundId, roundPlayer, e.getDurationSinceLogStart());
    Integer team = e.getIntegerParamValueByName(SetTeamParams.team.name());
    roundPlayer.setTeam(team);
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
  private RoundPlayerTeamRecord addRoundPlayerTeamUsage(int roundId, RoundPlayer roundPlayer, Duration teamEnd) {
    int playerId = roundPlayer.getPlayerId();
    int team = roundPlayer.getTeam();
    Duration teamStart = roundPlayer.getTeamStart();
    roundPlayer.setTeamStart(teamEnd);

    log.info("end team usage in round " + roundId + " for player slot " + roundPlayer.getRoundPlayerSlotId() + " for team " + team + " start: " + teamStart.getSeconds() + " end: " + teamEnd.getSeconds());
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
    Integer victimId = e.getIntegerParamValueByName("victim_id");

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

  private RoundPlayer getRoundPlayerFromSlotId(int slotId) {
    if (isSlotIdBot(slotId)) {
      slotId = FAKE_BOT_SLOT_ID; // treat bots as a single fake player
    }
    return activePlayersByRoundPlayerId.get(slotId);
  }

  private int getPlayerIdFromSlotId(int slotId) {
    RoundPlayer roundPlayer = getRoundPlayerFromSlotId(slotId);
    return requireNonNull(roundPlayer.getPlayerId(), "cant find player id for slot " + slotId);
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

  private void parseEventChangePlayerName(BfEvent e) {
    int playerId = getPlayerIdFromSlotId(e.getPlayerSlotId());
    String newPlayerName = e.getStringParamValueByName("name");
    addNickname(playerId, newPlayerName);
  }

  private void parseEventDestroyPlayer(int roundId, BfEvent destroyPlayerEvent) {
    if (isSlotIdBot(destroyPlayerEvent.getPlayerSlotId())) {
      return;
    }

    RoundPlayer roundPlayer = activePlayersByRoundPlayerId.remove(destroyPlayerEvent.getPlayerSlotId());
    log.info("removed round-player " + destroyPlayerEvent.getPlayerSlotId());

    if (roundPlayer.getPlayerId() == null) {
      log.info("Player " + roundPlayer.getName() + " has disconnected before playerKeyHash");
      return;
    }

    addRoundPlayerTeamUsage(roundId, roundPlayer, destroyPlayerEvent.getDurationSinceLogStart());
    addRoundPlayerJoinEndDates(roundId, roundPlayer, destroyPlayerEvent.getDurationSinceLogStart());
  }

  private void parseEventPlayerKeyHash(BfEvent e) {
    RoundPlayer roundPlayer = activePlayersByRoundPlayerId.get(e.getPlayerSlotId());
    requireNonNull(roundPlayer, "playerKeyHash there should've been createPlayer " + e.getPlayerSlotId());

    roundPlayer.setKeyhash(e.getStringParamValueByName(PlayerKeyHashParams.keyhash.name()));
    PlayerRecord playerRecord = addPlayerOrNickName(roundPlayer.getName(), roundPlayer.getKeyhash());
    roundPlayer.setPlayerId(playerRecord.getId());
  }

  private void parseEventCreatePlayer(Integer roundId, BfEvent e) {
    if (activePlayersByRoundPlayerId.containsKey(e.getPlayerSlotId())) {
      throw new IllegalStateException("destroyPlayer event should've deleted player with id " + e.getPlayerSlotId());
    }
    RoundPlayer roundPlayer = new RoundPlayer()
        .setRoundPlayerSlotId(e.getPlayerSlotId())
        .setName(e.getStringParamValueByName(CreatePlayerParams.name.name()))
        .setKeyhash(null)
        .setJoined(e.getDurationSinceLogStart())
        .setTeam(e.getIntegerParamValueByName(CreatePlayerParams.team.name()))
        .setTeamStart(e.getDurationSinceLogStart())
        .setJoinedRoundId(roundId);
    activePlayersByRoundPlayerId.put(e.getPlayerSlotId(), roundPlayer);
    log.info("added round-player " + e.getPlayerSlotId());
  }

  /**
   * @param deathEvent "DeathNoMsg" or "Death" score event type, should not be null, unless I'm wrong
   * @param killEvent  nullable, "Kill" or "TK" score event type, can be null if player just died
   */
  private RoundPlayerDeathRecord addPlayerDeath(int roundId, BfEvent deathEvent, BfEvent killEvent) {
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

  private RoundPlayerRecord addRoundPlayerJoinEndDates(int endRoundId, RoundPlayer roundPlayer, Duration endTime) {
    LocalDateTime joined = logStartTime.plus(roundPlayer.getJoined());
    LocalDateTime ended = logStartTime.plus(endTime);
    Integer joinedRoundId = roundPlayer.getJoinedRoundId();

    RoundPlayerRecord roundPlayerRecord = transaction().newRecord(ROUND_PLAYER);
    roundPlayerRecord.setJoinedRoundId(joinedRoundId); // TODO: there can be 2 rounds, so not really correct to have one round id
    roundPlayerRecord.setEndRoundId(endRoundId); // TODO: there can be 2 rounds, so not really correct to have one round id
    roundPlayerRecord.setPlayerId(roundPlayer.getPlayerId());
    roundPlayerRecord.setStartTime(Timestamp.valueOf(joined));
    roundPlayerRecord.setEndTime(Timestamp.valueOf(ended));

    roundPlayerRecord.insert();
    return roundPlayerRecord;
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
  public static class RoundPlayer {
    int roundPlayerSlotId;
    Integer playerId;
    String name;
    String keyhash;
    Duration joined;
    Duration teamStart;
    Integer team;
    Integer joinedRoundId;

    public RoundPlayer setTeamStart(Duration teamStart) {
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

  private PlayerRecord addPlayerOrNickName(String name, String keyHash) {
    PlayerRecord playerRecord = transaction().selectFrom(PLAYER).where(PLAYER.KEYHASH.eq(keyHash)).fetchOne();

    if (playerRecord == null) {
      playerRecord = transaction().newRecord(PLAYER);
      playerRecord.setName(name);
      playerRecord.setKeyhash(keyHash);
      playerRecord.insert();
    } else if (!playerRecord.getName().equals(name)) {
      addNickname(playerRecord.getId(), name);
    }

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
      playerNicknameRecord.insert();
    }

    return playerNicknameRecord;
  }

  private RoundRecord addRound(BfRound bfRound) {
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

    // Create a new record
    RoundRecord round = transaction().newRecord(ROUND);
    round.setStartTime(Timestamp.valueOf(logStartTime.plus(roundStartSinceLogStart)));
    round.setStartTicketsTeam_1(ticketsTeam1);
    round.setStartTicketsTeam_2(ticketsTeam2);
    round.setServerName(bfRound.getServerName());
    round.setServerPort(bfRound.getPort());
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
    round.setSoldierFf(bfRound.getSoldierFriendlyFire());
    round.setVehicleFf(bfRound.getVehicleFriendlyFire());
    round.setTicketRatio(bfRound.getTicketRatio());
    round.setTeamKillPunish(bfRound.isTeamKillPunished() ? 1 : 0);
    round.setPunkbusterEnabled(bfRound.isPunkBusterEnabled() ? 1 : 0);

    // inserts
    round.insert();
    return round;
  }
}
