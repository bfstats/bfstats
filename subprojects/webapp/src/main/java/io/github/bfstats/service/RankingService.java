package io.github.bfstats.service;

import io.github.bfstats.model.PlayerStats;
import io.github.bfstats.util.Sort;
import org.jooq.*;
import org.jooq.impl.DSL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.bfstats.dbstats.jooq.Tables.*;
import static io.github.bfstats.util.DbUtils.getDslContext;
import static io.github.bfstats.util.SortUtils.getJooqSortOrder;
import static io.github.bfstats.util.SortUtils.getSortableField;
import static org.jooq.impl.DSL.*;

public class RankingService {

  public int getTotalPlayerCount() {
    int totalNumberOfRows = getDslContext().selectCount().from(PLAYER_RANK).fetchOne(0, int.class);
    return totalNumberOfRows;
  }

  public List<PlayerStats> getRankings(@Nonnull Sort sort, int page, @Nullable Integer playerId) {
    Field<?> sortableField = getSortableField(sort.getProperty());

    int numberOfRows = 50;
    int firstRowIndex = (page - 1) * numberOfRows;

    Field<Integer> heals_all_count = count(ROUND_PLAYER_MEDPACK.ID).as("heals_all_count");
    Field<BigDecimal> heals_self_count = sum(when(ROUND_PLAYER_MEDPACK.HEALED_PLAYER_ID.eq(ROUND_PLAYER_MEDPACK.PLAYER_ID), 1).otherwise(0))
        .as("heals_self_count");
    Field<BigDecimal> heals_others_count = sum(when(ROUND_PLAYER_MEDPACK.HEALED_PLAYER_ID.isNotNull().and(ROUND_PLAYER_MEDPACK.HEALED_PLAYER_ID.notEqual(ROUND_PLAYER_MEDPACK.PLAYER_ID)), 1).otherwise(0))
        .as("heals_others_count");

    Table<Record4<Integer, Integer, BigDecimal, BigDecimal>> nestedHeals = getDslContext().select(
        ROUND_PLAYER_MEDPACK.PLAYER_ID,
        heals_all_count,
        heals_self_count,
        heals_others_count
    ).from(ROUND_PLAYER_MEDPACK)
        .groupBy(ROUND_PLAYER_MEDPACK.PLAYER_ID)
        .asTable().as("table_heals");

    Field<Integer> repairs_all_count = count(ROUND_PLAYER_REPAIR.ID).as("repairs_all_count");
    Field<BigDecimal> repairs_self_count = sum(when(ROUND_PLAYER_REPAIR.VEHICLE_PLAYER_ID.eq(ROUND_PLAYER_REPAIR.PLAYER_ID), 1).otherwise(0))
        .as("repairs_self_count");
    Field<BigDecimal> repairs_others_count = sum(when(ROUND_PLAYER_REPAIR.VEHICLE_PLAYER_ID.isNotNull().and(ROUND_PLAYER_REPAIR.VEHICLE_PLAYER_ID.notEqual(ROUND_PLAYER_REPAIR.PLAYER_ID)), 1).otherwise(0))
        .as("repairs_others_count");
    Field<BigDecimal> repairs_unmanned_count = sum(when(ROUND_PLAYER_REPAIR.VEHICLE_PLAYER_ID.isNull(), 1).otherwise(0))
        .as("repairs_unmanned_count");

    Table<Record5<Integer, Integer, BigDecimal, BigDecimal, BigDecimal>> nestedRepairs = getDslContext().select(
        ROUND_PLAYER_REPAIR.PLAYER_ID,
        repairs_all_count,
        repairs_self_count,
        repairs_others_count,
        repairs_unmanned_count
    ).from(ROUND_PLAYER_REPAIR)
        .groupBy(ROUND_PLAYER_REPAIR.PLAYER_ID)
        .asTable().as("table_repairs");

    AggregateFunction<Integer> totalRoundsPlayed = count();
    AggregateFunction<BigDecimal> totalScore = sum(ROUND_END_STATS_PLAYER.SCORE);
    AggregateFunction<BigDecimal> totalKills = sum(ROUND_END_STATS_PLAYER.KILLS);
    AggregateFunction<BigDecimal> totalDeaths = sum(ROUND_END_STATS_PLAYER.DEATHS);
    Field<Float> totalKillDeathRate = totalKills.cast(Float.class).div(totalDeaths);
    AggregateFunction<BigDecimal> totalTeamkills = sum(ROUND_END_STATS_PLAYER.TKS);
    AggregateFunction<BigDecimal> totalCaptures = sum(ROUND_END_STATS_PLAYER.CAPTURES);
    AggregateFunction<BigDecimal> totalAttacks = sum(ROUND_END_STATS_PLAYER.ATTACKS);
    AggregateFunction<BigDecimal> totalDefences = sum(ROUND_END_STATS_PLAYER.DEFENCES);
    AggregateFunction<BigDecimal> totalGoldCount = sum(when(ROUND_END_STATS_PLAYER.RANK.eq(1), 1).otherwise(0));
    AggregateFunction<BigDecimal> totalSilverCount = sum(when(ROUND_END_STATS_PLAYER.RANK.eq(2), 1).otherwise(0));
    AggregateFunction<BigDecimal> totalBronzeCount = sum(when(ROUND_END_STATS_PLAYER.RANK.eq(3), 1).otherwise(0));
    Field<Integer> totalAllHealsCount = nvl(nestedHeals.field(heals_all_count), 0);
    Field<Serializable> totalSelfHealsCount = nvl(nestedHeals.field(heals_self_count), 0);
    Field<Serializable> totalOthersHealsCount = nvl(nestedHeals.field(heals_others_count), 0);
    Field<Integer> totalAllRepairsCount = nvl(nestedRepairs.field(repairs_all_count), 0);
    Field<Serializable> totalSelfRepairsCount = nvl(nestedRepairs.field(repairs_self_count), 0);
    Field<Serializable> totalOthersRepairsCount = nvl(nestedRepairs.field(repairs_others_count), 0);
    Field<Serializable> totalUnmannedRepairsCount = nvl(nestedRepairs.field(repairs_unmanned_count), 0);

    return getDslContext().select(
        totalRoundsPlayed.as("rounds_played"),
        PLAYER.NAME.as("player_name"),
        PLAYER.KEYHASH.as("keyhash"),
        PLAYER_RANK.RANK.as("player_rank"),
        ROUND_END_STATS_PLAYER.PLAYER_ID.as("player_id"),
        ((totalCaptures.minus(totalTeamkills).plus(totalAllHealsCount.div(10)).plus(totalOthersRepairsCount.div(100))).div(totalRoundsPlayed).plus(totalKillDeathRate)).as("points"),
        totalScore.as("score"),
        totalScore.div(totalRoundsPlayed).as("average_score"),
        totalKills.as("kills"),
        totalDeaths.as("deaths"),
        totalKillDeathRate.as("kdrate"), // kill/death rate
        totalTeamkills.as("tks"),
        totalCaptures.as("captures"),
        totalAttacks.as("attacks"),
        totalDefences.as("defences"),
        totalGoldCount.as("gold_count"),
        totalSilverCount.as("silver_count"),
        totalBronzeCount.as("bronze_count"),

        totalAllHealsCount.as("heals_all_count"),
        totalSelfHealsCount.as("heals_self_count"),
        totalOthersHealsCount.as("heals_others_count"),

        totalAllRepairsCount.as("repairs_all_count"),
        totalSelfRepairsCount.as("repairs_self_count"),
        totalOthersRepairsCount.as("repairs_others_count"),
        totalUnmannedRepairsCount.as("repairs_unmanned_count")
    )
        .from(ROUND_END_STATS_PLAYER)
        .join(PLAYER).on(PLAYER.ID.eq(ROUND_END_STATS_PLAYER.PLAYER_ID))
        .join(PLAYER_RANK).on(PLAYER_RANK.PLAYER_ID.eq(ROUND_END_STATS_PLAYER.PLAYER_ID))

        .leftJoin(nestedHeals)
        .on(nestedHeals.field(ROUND_PLAYER_MEDPACK.PLAYER_ID).eq(ROUND_END_STATS_PLAYER.PLAYER_ID))

        .leftJoin(nestedRepairs)
        .on(nestedRepairs.field(ROUND_PLAYER_REPAIR.PLAYER_ID).eq(ROUND_END_STATS_PLAYER.PLAYER_ID))

        .where(playerId == null ? DSL.trueCondition() : ROUND_END_STATS_PLAYER.PLAYER_ID.eq(playerId))
        .groupBy(ROUND_END_STATS_PLAYER.PLAYER_ID)
        .orderBy(sortableField.sort(getJooqSortOrder(sort.getOrder())))
        .limit(firstRowIndex, numberOfRows)
        .fetch()
        .stream()
        .map(this::toPlayerStats)
        .collect(Collectors.toList());
  }

  private PlayerStats toPlayerStats(Record r) {
    BigDecimal kdRateBigDec = r.get("kdrate", BigDecimal.class);
    Double kdRate = kdRateBigDec == null ? 0.0 : kdRateBigDec.doubleValue();

    String keyhash = r.get("keyhash", String.class);
    String partialKeyHash = keyhash.substring(25);

    return new PlayerStats()
        .setPlayerId(r.get("player_id", Integer.class))
        .setName(r.get("player_name", String.class))
        .setPartialKeyHash(partialKeyHash)
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
