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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.InflaterInputStream;

import static io.github.bfvstats.game.jooq.Tables.*;
import static java.util.Objects.requireNonNull;

@Slf4j
public class DbFiller {
  private final DSLContext dslContext;
  private final Connection connection;

  private DSLContext transactionDslContext;

  public DbFiller() {
    try {
      this.connection = DriverManager.getConnection("jdbc:sqlite:baas.db");
      this.dslContext = DSL.using(connection);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private DSLContext transaction() {
    return transactionDslContext;
  }

  public static void main(String[] args) throws JAXBException, FileNotFoundException, SQLException {
    String logDirPath = "D:\\bflogs\\";
    parseAllInDir(logDirPath);
  }

  public static void parseAllInDir(String logDirPath) throws FileNotFoundException, JAXBException, SQLException {
    File logDir = new File(logDirPath);
    File[] dirFiles = logDir.listFiles((dir, name) -> name.endsWith(".xml") || name.endsWith(".zxml"));
    for (File fileI : dirFiles) {
      String filePath = fileI.getPath();
      if (filePath.endsWith(".xml")) {
        Path checkablePath = Paths.get(filePath.substring(0, filePath.length() - 4) + ".zxml");
        boolean hasZxmlCounterpart = Files.exists(checkablePath);
        if (hasZxmlCounterpart) {
          // TODO: maybe vice-versa - should ignore zxml files instead
          log.info("ignoring " + filePath + " because zxml will be extracted again");
          // skipping xml, as zxml also exists, so will wait for unzipping that again
          continue;
        }
      } else if (filePath.endsWith(".zxml")) {
        try {
          filePath = unzip(filePath); // replace with .xml counterpart
        } catch (IOException e) {
          log.warn("looks like " + filePath + " is not complete. " + e.getMessage());
          continue;
        }
      }

      log.info("Parsing " + filePath);
      File file = new File(filePath);
      BfLog bfLog = XmlParser.parseXmlLogFile(file);
      DbFiller dbFiller = new DbFiller();
      dbFiller.fillDb(bfLog);
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

  private void fillDb(BfLog bfLog) throws SQLException {
    dslContext.transaction(configuration -> {
      this.transactionDslContext = DSL.using(configuration);

      PlayerRecord botPlayerRecord = addPlayerOrNickName("FAKE_BOT", "FAKE_BOT");
      Integer botPlayerId = botPlayerRecord.getId();
      RoundPlayer botRoundPlayer = new RoundPlayer()
          .setRoundPlayerId(-1)
          .setPlayerId(botPlayerId)
          .setName("FAKE_BOT")
          .setKeyhash("FAKE_BOT");
      int botRoundPlayerId = botRoundPlayer.getRoundPlayerId();

      parseLog(bfLog, botRoundPlayer, botRoundPlayerId);
    });

    dslContext.close();
    connection.close();
  }

  private void parseLog(BfLog bfLog, RoundPlayer botRoundPlayer, int botRoundPlayerId) {
    LocalDateTime logStartTime = bfLog.getTimestampAsDate();

    // actually not per round, but per log file
    Map<Integer, RoundPlayer> activePlayersByRoundPlayerId = new HashMap<>();
    activePlayersByRoundPlayerId.put(botRoundPlayer.getRoundPlayerId(), botRoundPlayer);

    int lastRoundId = -1;
    for (Object child : bfLog.getRootEventsAndRounds()) {
      if (child instanceof BfEvent) {
        BfEvent bfEvent = (BfEvent) child;
        parseRootEvent(logStartTime, activePlayersByRoundPlayerId, lastRoundId, bfEvent);
      } else if (child instanceof BfRound) {
        BfRound bfRound = (BfRound) child;
        int roundId = parseRound(botRoundPlayerId, logStartTime, activePlayersByRoundPlayerId, bfRound);
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

  private int parseRound(int botRoundPlayerId, LocalDateTime logStartTime,
                         Map<Integer, RoundPlayer> activePlayersByRoundPlayerId, BfRound bfRound) {
    RoundRecord roundRecord = addRound(logStartTime, bfRound);
    int roundId = roundRecord.getId();

    for (BfEvent e : bfRound.getEvents()) {
      parseRoundEvent(botRoundPlayerId, logStartTime, activePlayersByRoundPlayerId, roundId, e);
    }

    if (bfRound.getRoundStats() != null) {
      BfRoundStats roundStats = bfRound.getRoundStats();
      RoundEndStatsRecord roundEndStatsRecord = addRoundEndStats(logStartTime, roundId, roundStats);
      // in order of rank
      int rank = 1;
      for (BfRoundStats.BfPlayerStat bfPlayerStat : roundStats.getPlayerStats()) {
        rank++;
        if (bfPlayerStat.isAi()) {
          // skip bot stats
          continue;
        }
        RoundPlayer roundPlayer = activePlayersByRoundPlayerId.get(bfPlayerStat.getPlayerId());
        Integer playerId = requireNonNull(roundPlayer.getPlayerId(), "round end: cant find the player " + bfPlayerStat.getPlayerId());

        addRoundEndStatsPlayer(roundId, bfPlayerStat, playerId, rank);
      }
    }
    return roundId;
  }

  private void parseRoundEvent(int botRoundPlayerId, LocalDateTime logStartTime,
                               Map<Integer, RoundPlayer> activePlayersByRoundPlayerId,
                               int roundId, BfEvent e) {
    switch (e.getEventName()) {
      case createPlayer:
        parseEventCreatePlayer(activePlayersByRoundPlayerId, e);
        break;
      case playerKeyHash:
        parseEventPlayerKeyHash(activePlayersByRoundPlayerId, e);
        break;
      case destroyPlayer:
        parseEventDestroyPlayer(activePlayersByRoundPlayerId, e);
        break;
      case changePlayerName:
        parseEventChangePlayerName(activePlayersByRoundPlayerId, e);
        break;
      case chat:
        parseEventChat(logStartTime, activePlayersByRoundPlayerId, roundId, e);
        break;
      case scoreEvent:
        parseEventScoreEvent(botRoundPlayerId, logStartTime, activePlayersByRoundPlayerId, roundId, e);
        break;
    }
  }

  private void parseRootEvent(LocalDateTime logStartTime, Map<Integer, RoundPlayer> activePlayersByRoundPlayerId, Integer lastRoundId, BfEvent e) {
    switch (e.getEventName()) {
      case createPlayer:
        parseEventCreatePlayer(activePlayersByRoundPlayerId, e);
        break;
      case playerKeyHash:
        parseEventPlayerKeyHash(activePlayersByRoundPlayerId, e);
        break;
      case destroyPlayer:
        parseEventDestroyPlayer(activePlayersByRoundPlayerId, e);
        break;
      case changePlayerName:
        parseEventChangePlayerName(activePlayersByRoundPlayerId, e);
        break;
      case chat:
        parseEventChat(logStartTime, activePlayersByRoundPlayerId, lastRoundId, e);
        break;
      case scoreEvent:
        log.warn("score event was called out-of-round");
        break;
    }
  }

  private void parseEventScoreEvent(int botRoundPlayerId, LocalDateTime logStartTime, Map<Integer, RoundPlayer> activePlayersByRoundPlayerId, int roundId, BfEvent e) {
    Integer roundPlayerId = e.getPlayerId();
    if (roundPlayerId > 127) {
      // using fake player for a bot
      roundPlayerId = botRoundPlayerId;
    }

    if (roundPlayerId == botRoundPlayerId) {
      // skip bots killing bots
      // skip bots killing humans (should probably add killer player id for human die event)
      // skip bots dieing because of humans (should maybe add some additional info for human kill event)
      return;
    }

    RoundPlayer roundPlayer = activePlayersByRoundPlayerId.get(roundPlayerId);
    Integer playerId = requireNonNull(roundPlayer.getPlayerId());

    Integer victimPlayerId = null;
    Integer victimRoundPlayerId = e.getIntegerParamValueByName(ScoreEventParams.victim_id.name());
    if (victimRoundPlayerId != null) {
      if (victimRoundPlayerId > 127) {
        // using fake player for a bot
        victimRoundPlayerId = botRoundPlayerId;
      }
      RoundPlayer victimRoundPlayer = activePlayersByRoundPlayerId.get(victimRoundPlayerId);
      victimPlayerId = requireNonNull(victimRoundPlayer.getPlayerId());
    }

    addRoundPlayerScoreEvent(logStartTime, roundId, playerId, victimPlayerId, e);
  }

  private void parseEventChat(LocalDateTime logStartTime, Map<Integer, RoundPlayer> activePlayersByRoundPlayerId, int roundId, BfEvent e) {
    RoundPlayer roundPlayer = activePlayersByRoundPlayerId.get(e.getPlayerId());
    Integer playerId = requireNonNull(roundPlayer.getPlayerId());
    addChatMessage(logStartTime, roundId, playerId, e);
  }

  private void parseEventChangePlayerName(Map<Integer, RoundPlayer> activePlayersByRoundPlayerId, BfEvent e) {
    RoundPlayer roundPlayer = activePlayersByRoundPlayerId.get(e.getPlayerId());
    Integer playerId = requireNonNull(roundPlayer.getPlayerId());
    String newPlayerName = e.getStringParamValueByName("name");
    addNickname(playerId, newPlayerName);
  }

  private void parseEventDestroyPlayer(Map<Integer, RoundPlayer> activePlayersByRoundPlayerId, BfEvent e) {
    activePlayersByRoundPlayerId.remove(e.getPlayerId());
    log.info("removed round-player " + e.getPlayerId());
  }

  private void parseEventPlayerKeyHash(Map<Integer, RoundPlayer> activePlayersByRoundPlayerId, BfEvent e) {
    RoundPlayer roundPlayer = activePlayersByRoundPlayerId.get(e.getPlayerId());
    requireNonNull(roundPlayer, "playerKeyHash there should've been createPlayer " + e.getPlayerId());

    roundPlayer.setKeyhash(e.getStringParamValueByName(PlayerKeyHashParams.keyhash.name()));
    PlayerRecord playerRecord = addPlayerOrNickName(roundPlayer.getName(), roundPlayer.getKeyhash());
    roundPlayer.setPlayerId(playerRecord.getId());
  }

  private void parseEventCreatePlayer(Map<Integer, RoundPlayer> activePlayersByRoundPlayerId, BfEvent e) {
    if (activePlayersByRoundPlayerId.containsKey(e.getPlayerId())) {
      throw new IllegalStateException("destroyPlayer event should've deleted player with id " + e.getPlayerId());
    }
    RoundPlayer roundPlayer = new RoundPlayer()
        .setRoundPlayerId(e.getPlayerId())
        .setName(e.getStringParamValueByName(CreatePlayerParams.name.name()))
        .setKeyhash(null);
    activePlayersByRoundPlayerId.put(e.getPlayerId(), roundPlayer);
    log.info("added round-player " + e.getPlayerId());
  }

  private RoundPlayerScoreEventRecord addRoundPlayerScoreEvent(LocalDateTime logStartTime, int roundId, Integer playerId,
                                                               Integer victimPlayerId, BfEvent e) {
    LocalDateTime eventTime = logStartTime.plus(e.getDurationSinceLogStart());
    String scoreType = e.getStringParamValueByName(ScoreEventParams.score_type.name());

    RoundPlayerScoreEventRecord roundPlayerScoreEventRecord = transaction().newRecord(ROUND_PLAYER_SCORE_EVENT);
    roundPlayerScoreEventRecord.setRoundId(roundId);
    roundPlayerScoreEventRecord.setPlayerId(playerId);
    if (e.getPlayerLocation() != null) {
      String[] playerLocation = e.getPlayerLocation();
      roundPlayerScoreEventRecord.setPlayerLocationX(new BigDecimal(playerLocation[0]));
      roundPlayerScoreEventRecord.setPlayerLocationY(new BigDecimal(playerLocation[1]));
      roundPlayerScoreEventRecord.setPlayerLocationZ(new BigDecimal(playerLocation[2]));
    }
    roundPlayerScoreEventRecord.setEventTime(Timestamp.valueOf(eventTime));
    roundPlayerScoreEventRecord.setScoreType(scoreType);
    roundPlayerScoreEventRecord.setVictimId(victimPlayerId);
    String weapon = e.getStringParamValueByName(ScoreEventParams.weapon.name());
    if ("(none)".equals(weapon)) {
      weapon = null;
    }
    roundPlayerScoreEventRecord.setWeapon(weapon);

    roundPlayerScoreEventRecord.insert();
    return roundPlayerScoreEventRecord;
  }

  @Data
  @Accessors(chain = true)
  public static class RoundPlayer {
    int roundPlayerId;
    Integer playerId;
    String name;
    String keyhash;
  }

  private RoundEndStatsPlayerRecord addRoundEndStatsPlayer(Integer roundId, BfRoundStats.BfPlayerStat bfPlayerStat,
                                                           Integer playerId, int rank) {
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

  private RoundEndStatsRecord addRoundEndStats(LocalDateTime logStartTime, Integer roundId, BfRoundStats bfRoundStats) {
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

  private RoundChatLogRecord addChatMessage(LocalDateTime logStartTime, Integer roundId, Integer playerId, BfEvent bfEvent) {
    String message = bfEvent.getStringParamValueByName(ChatEventParams.text.name());
    LocalDateTime eventTime = logStartTime.plus(bfEvent.getDurationSinceLogStart());

    RoundChatLogRecord roundChatLogRecord = transaction().newRecord(ROUND_CHAT_LOG);
    roundChatLogRecord.setRoundId(roundId);
    roundChatLogRecord.setPlayerId(playerId);
    if (bfEvent.getPlayerLocation() != null) {
      String[] playerLocation = bfEvent.getPlayerLocation();
      roundChatLogRecord.setPlayerLocationX(new BigDecimal(playerLocation[0]));
      roundChatLogRecord.setPlayerLocationY(new BigDecimal(playerLocation[1]));
      roundChatLogRecord.setPlayerLocationZ(new BigDecimal(playerLocation[2]));
    }

    roundChatLogRecord.setTeam(bfEvent.getIntegerParamValueByName(ChatEventParams.team.name()));
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

  private RoundRecord addRound(LocalDateTime logStartTime, BfRound bfRound) {
    BfEvent roundInitEvent = bfRound.getEvents().stream()
        .filter(e -> e.getEventName() == EventName.roundInit)
        .findFirst().orElse(null);

    Integer ticketsTeam1;
    Integer ticketsTeam2;
    LocalDateTime roundStartTime;

    // sometimes round ends before roundInit event is even fired
    if (roundInitEvent == null) {
      // chances are that these are the same as they will be in bfRound.getRoundStats() as non of the decreases
      // but I don't think these will be useful rounds anyway, so let's just set to -1 to distinguish instead of hoping
      // that there will be a round end event
      ticketsTeam1 = -1;
      ticketsTeam2 = -1;
      // as we can't take round start time from the roundInit event, we can get a very similar time from
      // round start time + game start delay
      roundStartTime = logStartTime.plus(bfRound.getDurationSinceLogStart())
          .plusSeconds(bfRound.getGameStartDelay());
    } else {
      ticketsTeam1 = roundInitEvent.getIntegerParamValueByName(RoundInitParams.tickets_team1.name());
      ticketsTeam2 = roundInitEvent.getIntegerParamValueByName(RoundInitParams.tickets_team2.name());
      roundStartTime = logStartTime.plus(roundInitEvent.getDurationSinceLogStart());
    }

    // Create a new record
    RoundRecord round = transaction().newRecord(ROUND);
    round.setStartTime(Timestamp.valueOf(roundStartTime));
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
