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

import javax.xml.bind.JAXBException;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.InflaterInputStream;

import static io.github.bfvstats.dbfiller.DbUtil.closeDslContext;
import static io.github.bfvstats.dbfiller.DbUtil.getDslContext;
import static io.github.bfvstats.game.jooq.Tables.*;
import static java.util.Objects.requireNonNull;

public class DbFiller {
  public static void main(String[] args) throws JAXBException, FileNotFoundException {
    String logDirPath = "D:\\Projects\\bfvstats\\example\\";
    parseAllInDir(logDirPath);
  }

  public static void parseAllInDir(String logDirPath) throws FileNotFoundException, JAXBException {
    File logDir = new File(logDirPath);
    File[] dirFiles = logDir.listFiles((dir, name) -> name.endsWith(".xml") || name.endsWith(".zxml"));
    for (File fileI : dirFiles) {
      String filePath = fileI.getPath();
      if (filePath.endsWith(".xml")) {
        Path checkablePath = Paths.get(filePath.substring(0, filePath.length() - 4) + ".zxml");
        boolean hasZxmlCounterpart = Files.exists(checkablePath);
        if (hasZxmlCounterpart) {
          System.out.println(checkablePath);
          // skipping xml, as zxml also exists, so will wait for unzipping that again
          continue;
        }
      } else if (filePath.endsWith(".zxml")) {
        try {
          filePath = unzip(filePath); // replace with .xml counterpart
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

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

  private void fillDb(BfLog bfLog) {
    PlayerRecord botPlayerRecord = addPlayerOrNickName("FAKE_BOT", "FAKE_BOT");
    Integer botPlayerId = botPlayerRecord.getId();
    RoundPlayer botRoundPlayer = new RoundPlayer()
        .setRoundPlayerId(-1)
        .setPlayerId(botPlayerId)
        .setName("FAKE_BOT")
        .setKeyhash("FAKE_BOT");
    int botRoundPlayerId = botRoundPlayer.getRoundPlayerId();

    LocalDateTime logStartTime = bfLog.getTimestampAsDate();

    // actually not per round, but per log file
    Map<Integer, RoundPlayer> activePlayersByRoundPlayerId = new HashMap<>();
    activePlayersByRoundPlayerId.put(botRoundPlayer.getRoundPlayerId(), botRoundPlayer);

    for (BfRound bfRound : bfLog.getRounds()) {
      BfEvent roundInitEvent = bfRound.getEvents().stream()
          .filter(e -> e.getEventName() == EventName.roundInit)
          .findFirst().orElseThrow(() -> new IllegalStateException("roundInit event is missing"));
      RoundRecord roundRecord = addRound(logStartTime, bfRound, roundInitEvent);
      int roundId = roundRecord.getId();

      for (BfEvent e : bfRound.getEvents()) {
        if (e.getEventName() == EventName.createPlayer) {
          if (activePlayersByRoundPlayerId.containsKey(e.getPlayerId())) {
            throw new IllegalStateException("destroyPlayer event should've deleted player with id " + e.getPlayerId());
          }
          RoundPlayer roundPlayer = new RoundPlayer()
              .setRoundPlayerId(e.getPlayerId())
              .setName(e.getStringParamValueByName(CreatePlayerParams.name.name()))
              .setKeyhash(null);
          activePlayersByRoundPlayerId.put(e.getPlayerId(), roundPlayer);
          System.out.println("added round-player " + e.getPlayerId());
        } else if (e.getEventName() == EventName.playerKeyHash) {
          RoundPlayer roundPlayer = activePlayersByRoundPlayerId.get(e.getPlayerId());
          roundPlayer.setKeyhash(e.getStringParamValueByName(PlayerKeyHashParams.keyhash.name()));
          PlayerRecord playerRecord = addPlayerOrNickName(roundPlayer.getName(), roundPlayer.getKeyhash());
          roundPlayer.setPlayerId(playerRecord.getId());
        } else if (e.getEventName() == EventName.destroyPlayer) {
          activePlayersByRoundPlayerId.remove(e.getPlayerId());
          System.out.println("removed round-player " + e.getPlayerId());
        } else if (e.getEventName() == EventName.changePlayerName) {
          RoundPlayer roundPlayer = activePlayersByRoundPlayerId.get(e.getPlayerId());
          Integer playerId = requireNonNull(roundPlayer.getPlayerId());
          String newPlayerName = e.getStringParamValueByName("name");
          addNickname(playerId, newPlayerName);
        } else if (e.getEventName() == EventName.chat) {
          RoundPlayer roundPlayer = activePlayersByRoundPlayerId.get(e.getPlayerId());
          Integer playerId = requireNonNull(roundPlayer.getPlayerId());
          addChatMessage(logStartTime, roundId, playerId, e);
        } else if (e.getEventName() == EventName.scoreEvent) {
          Integer roundPlayerId = e.getPlayerId();
          if (roundPlayerId > 127) {
            // using fake player for a bot
            roundPlayerId = botRoundPlayerId;
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
          Integer playerId = requireNonNull(roundPlayer.getPlayerId());

          addRoundEndStatsPlayer(roundId, bfPlayerStat, playerId, rank);
        }
      }

    }

    closeDslContext();
  }

  private RoundPlayerScoreEventRecord addRoundPlayerScoreEvent(LocalDateTime logStartTime, int roundId, Integer playerId,
                                                               Integer victimPlayerId, BfEvent e) {
    LocalDateTime eventTime = logStartTime.plus(e.getDurationSinceLogStart());
    String scoreType = e.getStringParamValueByName(ScoreEventParams.score_type.name());

    RoundPlayerScoreEventRecord roundPlayerScoreEventRecord = getDslContext().newRecord(ROUND_PLAYER_SCORE_EVENT);
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
    RoundEndStatsPlayerRecord roundEndStatsPlayerRecord = getDslContext().newRecord(ROUND_END_STATS_PLAYER);
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

    RoundEndStatsRecord roundEndStatsRecord = getDslContext().newRecord(ROUND_END_STATS);
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

    RoundChatLogRecord roundChatLogRecord = getDslContext().newRecord(ROUND_CHAT_LOG);
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
    PlayerRecord playerRecord = getDslContext().selectFrom(PLAYER).where(PLAYER.KEYHASH.eq(keyHash)).fetchOne();

    if (playerRecord == null) {
      playerRecord = getDslContext().newRecord(PLAYER);
      playerRecord.setName(name);
      playerRecord.setKeyhash(keyHash);
      playerRecord.insert();
    } else if (!playerRecord.getName().equals(name)) {
      addNickname(playerRecord.getId(), name);
    }

    return playerRecord;
  }

  private PlayerNicknameRecord addNickname(int playerId, String nickname) {
    PlayerNicknameRecord playerNicknameRecord = getDslContext().selectFrom(PLAYER_NICKNAME)
        .where(PLAYER_NICKNAME.PLAYER_ID.eq(playerId))
        .and(PLAYER_NICKNAME.NICKNAME.eq(nickname))
        .fetchOne();

    if (playerNicknameRecord == null) {
      playerNicknameRecord = getDslContext().newRecord(PLAYER_NICKNAME);
      playerNicknameRecord.setPlayerId(playerId);
      playerNicknameRecord.setNickname(nickname);
      playerNicknameRecord.insert();
    }

    return playerNicknameRecord;
  }

  private RoundRecord addRound(LocalDateTime logStartTime, BfRound bfRound, BfEvent roundInitEvent) {
    Integer ticketsTeam1 = roundInitEvent.getIntegerParamValueByName(RoundInitParams.tickets_team1.name());
    Integer ticketsTeam2 = roundInitEvent.getIntegerParamValueByName(RoundInitParams.tickets_team2.name());

    LocalDateTime roundStartTime = logStartTime.plus(roundInitEvent.getDurationSinceLogStart());

    // Create a new record
    RoundRecord round = getDslContext().newRecord(ROUND);
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
