package io.github.bfvstats.service;

import io.github.bfvstats.game.jooq.tables.records.RoundEndStatsPlayerRecord;
import io.github.bfvstats.game.jooq.tables.records.RoundEndStatsRecord;
import io.github.bfvstats.game.jooq.tables.records.RoundRecord;
import io.github.bfvstats.model.Round;
import io.github.bfvstats.model.ServerSettings;
import io.github.bfvstats.util.TranslationUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jooq.Result;
import org.jooq.impl.DSL;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.bfvstats.game.jooq.Tables.*;
import static io.github.bfvstats.util.DbUtils.getDslContext;

public class RoundService {
  public List<Round> getRounds() {
    Map<RoundRecord, RoundEndStatsRecord> roundWithStats = getRoundRecordsWithStats(null);

    return roundWithStats.entrySet().stream()
        .map(e -> toRound(e.getKey(), e.getValue()))
        .collect(Collectors.toList());
  }

  public Round getRound(int roundId) {
    Map<RoundRecord, RoundEndStatsRecord> roundWithStats = getRoundRecordsWithStats(roundId);
    Map.Entry<RoundRecord, RoundEndStatsRecord> onlyEntry = roundWithStats.entrySet().iterator().next();
    RoundRecord roundRecord = onlyEntry.getKey();
    Round round = toRound(roundRecord, onlyEntry.getValue());
    round.setServerSettings(toServerSettings(roundRecord));
    return round;
  }

  private static Map<RoundRecord, RoundEndStatsRecord> getRoundRecordsWithStats(@Nullable Integer roundId) {
    return getDslContext()
        .select(ROUND.fields())
        .select(ROUND_END_STATS.fields())
        .from(ROUND)
        .join(ROUND_END_STATS).on(ROUND_END_STATS.ROUND_ID.eq(ROUND.ID)) // also skips rounds which don't have end result
        .where(roundId == null ? DSL.trueCondition() : ROUND.ID.eq(roundId))
        .orderBy(ROUND.START_TIME.desc())
        .fetchMap(
            r -> r.into(ROUND),
            r -> r.into(ROUND_END_STATS)
        );
  }

  private static ServerSettings toServerSettings(RoundRecord roundRecord) {
    return new ServerSettings()
        .setServerName(roundRecord.getServerName())
        .setServerPort(roundRecord.getServerPort())
        .setModId(roundRecord.getModId())
        .setGameMode(roundRecord.getGameMode())
        .setMaxGameTime(roundRecord.getMaxGameTime())
        .setMaxPlayers(roundRecord.getMaxPlayers())
        .setScoreLimit(roundRecord.getScoreLimit())
        .setNumberOfRoundsPerMap(roundRecord.getNoOfRounds())
        .setSpawnTime(roundRecord.getSpawnTime())
        .setSpawnDelay(roundRecord.getSpawnDelay())
        .setGameStartDelay(roundRecord.getGameStartDelay())
        .setRoundStartDelay(roundRecord.getRoundStartDelay())
        .setSoldierFriendlyFire(roundRecord.getSoldierFf())
        .setVehicleFriendlyFire(roundRecord.getVehicleFf())
        .setTicketRatio(roundRecord.getTicketRatio())
        .setTeamKillPunish(roundRecord.getTeamKillPunish() == 1)
        .setPunkbusterEnabled(roundRecord.getPunkbusterEnabled() == 1);
  }

  private static Round toRound(RoundRecord roundRecord, RoundEndStatsRecord roundEndStatsRecord) {
    String mapCode = roundRecord.getMapCode();
    String mapName = TranslationUtil.getMapName(mapCode);

    LocalDateTime startTime = roundRecord.getStartTime().toLocalDateTime();
    LocalDateTime endTime = roundEndStatsRecord.getEndTime().toLocalDateTime();
    long durationMinutes = startTime.until(endTime, ChronoUnit.MINUTES);
    //long durationMinutes = ChronoUnit.MINUTES.between(startTime, endTime);

    return new Round()
        .setId(roundRecord.getId())
        .setMapCode(mapCode)
        .setMapName(mapName)
        .setStartTime(startTime)
        .setEndTime(endTime)
        .setDurationInMinutes(durationMinutes)
        .setWinningTeam(roundEndStatsRecord.getWinningTeam())
        .setVictoryType(roundEndStatsRecord.getVictoryType())
        .setEndTicketsTeam1(roundEndStatsRecord.getEndTicketsTeam_1())
        .setEndTicketsTeam2(roundEndStatsRecord.getEndTicketsTeam_2());
  }

  public List<RoundPlayerStats> getRoundPlayerStats(int roundId) {
    Result<RoundEndStatsPlayerRecord> roundEndStatsRecords = getDslContext()
        .selectFrom(ROUND_END_STATS_PLAYER)
        .where(ROUND_END_STATS_PLAYER.ROUND_ID.eq(roundId))
        .fetch();

    return roundEndStatsRecords.stream().map(RoundService::toRoundPlayerStats)
        .collect(Collectors.toList());
  }

  private static RoundPlayerStats toRoundPlayerStats(RoundEndStatsPlayerRecord r) {
    return new RoundPlayerStats()
        .setRoundId(r.getRoundId())
        .setPlayerId(r.getPlayerId())
        .setPlayerName(r.getPlayerName())
        .setRank(r.getRank())
        .setBot(r.getIsAi() == 1)
        .setTeam(r.getTeam())
        .setScore(r.getScore())
        .setKills(r.getKills())
        .setDeaths(r.getDeaths())
        .setTks(r.getTks())
        .setCaptures(r.getCaptures())
        .setAttacks(r.getAttacks())
        .setDefences(r.getDefences());

  }

  @Data
  @Accessors(chain = true)
  public static class RoundPlayerStats {
    private int roundId;
    private int playerId;
    private String playerName;
    private int rank;
    private boolean bot;
    private int team;
    private int score;
    private int kills;
    private int deaths;
    private int tks;
    private int captures;
    private int attacks;
    private int defences;
  }
}
