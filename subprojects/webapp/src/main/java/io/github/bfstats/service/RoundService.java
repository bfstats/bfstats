package io.github.bfstats.service;

import io.github.bfstats.dbstats.jooq.tables.records.RoundEndStatsPlayerRecord;
import io.github.bfstats.dbstats.jooq.tables.records.RoundEndStatsRecord;
import io.github.bfstats.dbstats.jooq.tables.records.RoundRecord;
import io.github.bfstats.exceptions.NotFoundException;
import io.github.bfstats.model.Round;
import io.github.bfstats.model.ServerSettings;
import io.github.bfstats.util.TranslationUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jooq.Result;
import org.jooq.impl.DSL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.bfstats.dbstats.jooq.Tables.*;
import static io.github.bfstats.util.DateTimeUtils.toUserZone;
import static io.github.bfstats.util.DbUtils.getDslContext;

public class RoundService {
  public List<Round> getActiveRounds(int page) {
    Map<RoundRecord, RoundEndStatsRecord> roundWithStats = getActiveRoundRecordsWithStats(page);

    return roundWithStats.entrySet().stream()
        .map(e -> toRound(e.getKey(), e.getValue()))
        .collect(Collectors.toList());
  }

  private static Map<RoundRecord, RoundEndStatsRecord> getActiveRoundRecordsWithStats(int page) {
    int numberOfRows = 50;
    int firstRowIndex = (page - 1) * numberOfRows;

    return getDslContext()
        .select(ROUND.fields())
        .select(ROUND_END_STATS.fields())
        .from(ROUND)
        .leftJoin(ROUND_END_STATS).on(ROUND_END_STATS.ROUND_ID.eq(ROUND.ID))
        .where(DSL.exists(getDslContext().selectOne().from(ROUND_PLAYER_DEATH).where(ROUND_PLAYER_DEATH.ROUND_ID.eq(ROUND.ID))))
        .orderBy(ROUND.START_TIME.desc())
        .limit(firstRowIndex, numberOfRows)
        .fetchMap(
            r -> r.into(ROUND),
            r -> r.into(ROUND_END_STATS)
        );
  }

  public List<Round> getRoundsByGameId(int gameId) {
    Map<RoundRecord, RoundEndStatsRecord> roundWithStats = getRoundRecordsWithStatsByGameId(gameId);

    return roundWithStats.entrySet().stream()
        .map(e -> toRound(e.getKey(), e.getValue()))
        .collect(Collectors.toList());
  }

  private static Map<RoundRecord, RoundEndStatsRecord> getRoundRecordsWithStatsByGameId(int gameId) {
    return getDslContext()
        .select(ROUND.fields())
        .select(ROUND_END_STATS.fields())
        .from(ROUND)
        .leftJoin(ROUND_END_STATS).on(ROUND_END_STATS.ROUND_ID.eq(ROUND.ID))
        .where(ROUND.GAME_ID.eq(gameId))
        .orderBy(ROUND.START_TIME.asc())
        .fetchMap(
            r -> r.into(ROUND),
            r -> r.into(ROUND_END_STATS)
        );
  }

  public List<Round> getRounds(int page) {
    Map<RoundRecord, RoundEndStatsRecord> roundWithStats = getRoundRecordsWithStats(null, page);

    return roundWithStats.entrySet().stream()
        .map(e -> toRound(e.getKey(), e.getValue()))
        .collect(Collectors.toList());
  }

  public Round getRound(int roundId) {
    Map<RoundRecord, RoundEndStatsRecord> roundWithStats = getRoundRecordsWithStats(roundId, 1);
    if (roundWithStats.isEmpty()) {
      throw new NotFoundException("round with id " + roundId + " not found");
    }
    Map.Entry<RoundRecord, RoundEndStatsRecord> onlyEntry = roundWithStats.entrySet().iterator().next();
    RoundRecord roundRecord = onlyEntry.getKey();
    Round round = toRound(roundRecord, onlyEntry.getValue());
    round.setServerSettings(toServerSettings(roundRecord));
    return round;
  }

  private static Map<RoundRecord, RoundEndStatsRecord> getRoundRecordsWithStats(@Nullable Integer roundId, int page) {
    int numberOfRows = 50;
    int firstRowIndex = (page - 1) * numberOfRows;

    return getDslContext()
        .select(ROUND.fields())
        .select(ROUND_END_STATS.fields())
        .from(ROUND)
        .leftJoin(ROUND_END_STATS).on(ROUND_END_STATS.ROUND_ID.eq(ROUND.ID))
        .where(roundId == null ? DSL.trueCondition() : ROUND.ID.eq(roundId))
        .orderBy(ROUND.START_TIME.desc())
        .limit(firstRowIndex, numberOfRows)
        .fetchMap(
            r -> r.into(ROUND),
            r -> r.into(ROUND_END_STATS)
        );
  }

  public List<Round> getRoundsForPlayer(int playerId) {
    Map<RoundRecord, RoundEndStatsRecord> roundWithStats = getRoundRecordsWithStatsForPlayer(playerId);

    return roundWithStats.entrySet().stream()
        .map(e -> toRound(e.getKey(), e.getValue()))
        .collect(Collectors.toList());
  }

  private static Map<RoundRecord, RoundEndStatsRecord> getRoundRecordsWithStatsForPlayer(int playerId) {
    int numberOfRows = 10;
    int firstRowIndex = 0;

    return getDslContext()
        .select(ROUND.fields())
        .select(ROUND_END_STATS.fields())
        .from(ROUND)
        .leftJoin(ROUND_END_STATS).on(ROUND_END_STATS.ROUND_ID.eq(ROUND.ID))
        .where(ROUND.ID.in(getDslContext().selectDistinct(ROUND_PLAYER_TEAM.ROUND_ID).from(ROUND_PLAYER_TEAM).where(ROUND_PLAYER_TEAM.PLAYER_ID.eq(playerId))))
        .orderBy(ROUND.START_TIME.desc())
        .limit(firstRowIndex, numberOfRows)
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
        .setGameModeCode(roundRecord.getGameMode())
        .setGameModeName(TranslationUtil.getModeName(roundRecord.getGameMode()))
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

  private static Round toRound(@Nonnull RoundRecord roundRecord, @Nonnull RoundEndStatsRecord roundEndStatsRecord) {
    String mapCode = roundRecord.getMapCode();
    String mapName = TranslationUtil.getMapName(mapCode);
    String modeName = TranslationUtil.getModeName(roundRecord.getGameMode());

    LocalDateTime startTime = toUserZone(roundRecord.getStartTime().toLocalDateTime());

    Round round = new Round()
        .setId(roundRecord.getId())
        .setMapCode(mapCode)
        .setMapName(mapName)
        .setModeName(modeName)
        .setStartTime(startTime)
        .setGameId(roundRecord.getGameId());

    // though roundEndStatsRecord is not null, it might be empty
    if (roundEndStatsRecord.getRoundId() != null) {
      LocalDateTime endTime = toUserZone(roundEndStatsRecord.getEndTime().toLocalDateTime());
      long durationMinutes = startTime.until(endTime, ChronoUnit.MINUTES);

      round.setEndTime(endTime)
          .setDurationInMinutes(durationMinutes)
          .setWinningTeam(roundEndStatsRecord.getWinningTeam())
          .setVictoryType(roundEndStatsRecord.getVictoryType())
          .setEndTicketsTeam1(roundEndStatsRecord.getEndTicketsTeam_1())
          .setEndTicketsTeam2(roundEndStatsRecord.getEndTicketsTeam_2());
    }

    round.setMapEventsUrlPath("rounds/json/" + round.getId() + "/events");

    return round;
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

  public int getTotalActiveRoundsCount() {
    return getDslContext().selectCount().from(ROUND)
        .where(DSL.exists(getDslContext().selectOne().from(ROUND_PLAYER_DEATH).where(ROUND_PLAYER_DEATH.ROUND_ID.eq(ROUND.ID))))
        .fetchOne(0, int.class);
  }

  public int getTotalRoundsCount() {
    return getDslContext().selectCount().from(ROUND).fetchOne(0, int.class);
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
