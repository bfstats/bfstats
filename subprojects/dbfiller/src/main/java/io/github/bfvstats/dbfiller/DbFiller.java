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
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static io.github.bfvstats.dbfiller.DbUtil.closeDslContext;
import static io.github.bfvstats.dbfiller.DbUtil.getDslContext;
import static io.github.bfvstats.game.jooq.Tables.*;
import static java.util.Objects.requireNonNull;

public class DbFiller {
  public static void main(String[] args) throws JAXBException, FileNotFoundException {
    String fileName = "D:\\Projects\\bfvstats\\ev_15567-20161224_0825.xml";
    File file = new File(fileName);
    BfLog bfLog = XmlParser.parseXmlLogFile(file);

    DbFiller dbFiller = new DbFiller();
    dbFiller.fillDb(bfLog);
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

    int i = 0;
    for (BfRound bfRound : bfLog.getRounds()) {
      i++;
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
        for (BfRoundStats.BfPlayerStat bfPlayerStat : roundStats.getPlayerStats()) {
          if (bfPlayerStat.isAi()) {
            // skip bot stats
            continue;
          }
          RoundPlayer roundPlayer = activePlayersByRoundPlayerId.get(bfPlayerStat.getPlayerId());
          Integer playerId = requireNonNull(roundPlayer.getPlayerId());

          addRoundEndStatsPlayer(roundId, bfPlayerStat, playerId);
        }
      }

    }

    closeDslContext();
  }

  private RoundPlayerScoreEventRecord addRoundPlayerScoreEvent(LocalDateTime logStartTime, int roundId, Integer playerId, Integer victimPlayerId, BfEvent e) {
    LocalDateTime eventTime = logStartTime.plus(e.getDurationSinceLogStart());
    String scoreType = e.getStringParamValueByName(ScoreEventParams.score_type.name());

    RoundPlayerScoreEventRecord roundPlayerScoreEventRecord = getDslContext().newRecord(ROUND_PLAYER_SCORE_EVENT);
    roundPlayerScoreEventRecord.setRoundId(roundId);
    roundPlayerScoreEventRecord.setPlayerId(playerId);
    if (e.getPlayerLocation() != null) {
      String[] playerLocation = e.getPlayerLocation();
      roundPlayerScoreEventRecord.setPlayerLocationX(Float.parseFloat(playerLocation[0]));
      roundPlayerScoreEventRecord.setPlayerLocationY(Float.parseFloat(playerLocation[1]));
      roundPlayerScoreEventRecord.setPlayerLocationZ(Float.parseFloat(playerLocation[2]));
    }
    roundPlayerScoreEventRecord.setEventTime(Timestamp.valueOf(eventTime));
    roundPlayerScoreEventRecord.setScoreType(scoreType);
    roundPlayerScoreEventRecord.setVictimId(victimPlayerId);
    roundPlayerScoreEventRecord.setWeapon(e.getStringParamValueByName(ScoreEventParams.weapon.name()));

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

  private RoundEndStatsPlayerRecord addRoundEndStatsPlayer(Integer roundId, BfRoundStats.BfPlayerStat bfPlayerStat, Integer playerId) {
    RoundEndStatsPlayerRecord roundEndStatsRecord = getDslContext().newRecord(ROUND_END_STATS_PLAYER);
    roundEndStatsRecord.setRoundId(roundId);
    roundEndStatsRecord.setPlayerId(playerId);
    roundEndStatsRecord.setPlayerName(bfPlayerStat.getPlayerName());
    roundEndStatsRecord.setIsAi(bfPlayerStat.isAi() ? 1 : 0);
    roundEndStatsRecord.setTeam(bfPlayerStat.getTeam());
    roundEndStatsRecord.setScore(bfPlayerStat.getScore());
    roundEndStatsRecord.setKills(bfPlayerStat.getKills());
    roundEndStatsRecord.setDeaths(bfPlayerStat.getDeaths());
    roundEndStatsRecord.setTks(bfPlayerStat.getTks());
    roundEndStatsRecord.setCaptures(bfPlayerStat.getCaptures());
    roundEndStatsRecord.setAttacks(bfPlayerStat.getAttacks());
    roundEndStatsRecord.setDefences(bfPlayerStat.getDefences());

    roundEndStatsRecord.insert();
    return roundEndStatsRecord;
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
      roundChatLogRecord.setPlayerLocationX(Float.parseFloat(playerLocation[0]));
      roundChatLogRecord.setPlayerLocationY(Float.parseFloat(playerLocation[1]));
      roundChatLogRecord.setPlayerLocationZ(Float.parseFloat(playerLocation[2]));
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
      // add to alternative nicknames
    }

    return playerRecord;
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
