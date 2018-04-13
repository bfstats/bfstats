package io.github.bfstats.service;

import io.github.bfstats.dbstats.jooq.tables.records.PlayerSummaryRecord;
import io.github.bfstats.dbstats.jooq.tables.records.PlayerTopStatsRecord;
import io.github.bfstats.model.PlayerAchievements;
import io.github.bfstats.model.PlayerStats;
import io.github.bfstats.util.Sort;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.bfstats.dbstats.jooq.Tables.PLAYER_SUMMARY;
import static io.github.bfstats.dbstats.jooq.Tables.PLAYER_TOP_STATS;
import static io.github.bfstats.util.DbUtils.getDslContext;
import static io.github.bfstats.util.SortUtils.getJooqSortOrder;
import static io.github.bfstats.util.SortUtils.getSortableField;

public class RankingService {

  public int getTotalPlayerCount() {
    int totalNumberOfRows = getDslContext().selectCount().from(PLAYER_SUMMARY).fetchOne(0, int.class);
    return totalNumberOfRows;
  }

  public List<PlayerStats> getRankings(@Nonnull Sort sort, int page, @Nullable Integer playerId) {
    Field<?> sortableField = getSortableField(sort.getProperty());

    int numberOfRows = 50;
    int firstRowIndex = (page - 1) * numberOfRows;

    Result<PlayerSummaryRecord> playerSummaryRecords = getDslContext().selectFrom(PLAYER_SUMMARY)
        .where(playerId == null ? DSL.trueCondition() : PLAYER_SUMMARY.PLAYER_ID.eq(playerId))
        .orderBy(sortableField.sort(getJooqSortOrder(sort.getOrder())))
        .limit(firstRowIndex, numberOfRows)
        .fetch();

    return playerSummaryRecords.stream()
        .map(RankingService::toPlayerStats)
        .collect(Collectors.toList());
  }

  public PlayerAchievements getPlayerAchievements(int playerId) {
    PlayerTopStatsRecord playerTopStatsRecord = getDslContext().selectFrom(PLAYER_TOP_STATS)
        .where(PLAYER_TOP_STATS.PLAYER_ID.eq(playerId))
        .fetchOne();

    return toPlayerAchievements(playerTopStatsRecord);
  }

  private static boolean getAsBoolean(Record record, String columnName) {
    Integer value = record.get(columnName, Integer.class);
    return value != null && value.equals(1);
  }

  private static PlayerAchievements toPlayerAchievements(PlayerTopStatsRecord r) {
    Map<String, Object> achievementsMap = r.intoMap();
    achievementsMap.remove("player_id");
    long achievementsCount = achievementsMap.values().stream()
        .filter(value -> {
          Integer valueInt = (Integer) value;
          return valueInt != null && valueInt.equals(1);
        })
        .count();

    return new PlayerAchievements()
        .setAchievementsCount(achievementsCount)
        .setMaxPoints(getAsBoolean(r, "is_max_points"))
        .setMaxScore(getAsBoolean(r, "is_max_score"))
        .setMaxGoldCount(getAsBoolean(r, "is_max_gold_count"))
        .setMaxSilverCount(getAsBoolean(r, "is_max_silver_count"))
        .setMaxBronzeCount(getAsBoolean(r, "is_max_bronze_count"))
        .setMaxHealsAllCount(getAsBoolean(r, "is_max_heals_all_count"))
        .setMaxRepairsAllCount(getAsBoolean(r, "is_max_repairs_all_count"))
        .setMaxCaptures(getAsBoolean(r, "is_max_captures"));
  }

  private static PlayerStats toPlayerStats(PlayerSummaryRecord r) {
    BigDecimal kdRateBigDec = r.get("kdrate", BigDecimal.class);
    Double kdRate = kdRateBigDec == null ? 0.0 : kdRateBigDec.doubleValue();

    return new PlayerStats()
        .setPlayerId(r.get("player_id", Integer.class))
        .setName(r.get("player_name", String.class))
        .setRank(r.get("player_rank", Integer.class))
        .setPoints(r.get("points", Integer.class))
        .setScore(r.get("score", Integer.class))
        .setAverageScore(r.get("average_score", Integer.class))
        .setKills(r.get("kills", Integer.class))
        .setDeaths(r.get("deaths", Integer.class))
        .setKillDeathRatio(kdRate)
        .setGoldCount(r.get("gold_count", Integer.class))
        .setSilverCount(r.get("silver_count", Integer.class))
        .setBronzeCount(r.get("bronze_count", Integer.class))
        .setTeamKills(r.get("tks", Integer.class))
        .setCaptures(r.get("captures", Integer.class))
        .setDefences(r.get("defences", Integer.class))
        .setRoundsPlayed(r.get("rounds_played", Integer.class))

        .setHeals(r.get("heals_all_count", Integer.class))
        .setSelfHeals(r.get("heals_self_count", Integer.class))
        .setOtherHeals(r.get("heals_others_count", Integer.class))

        .setRepairs(r.get("repairs_all_count", Integer.class))
        .setSelfRepairs(r.get("repairs_self_count", Integer.class))
        .setOtherRepairs(r.get("repairs_others_count", Integer.class))
        .setUnmannedRepairs(r.get("repairs_unmanned_count", Integer.class))

        .setScorePerMinute(0);
  }
}
