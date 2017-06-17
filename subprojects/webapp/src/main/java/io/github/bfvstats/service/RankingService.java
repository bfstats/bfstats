package io.github.bfvstats.service;

import io.github.bfvstats.model.PlayerStats;
import io.github.bfvstats.util.Sort;
import org.jooq.*;
import org.jooq.impl.DSL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.bfvstats.game.jooq.Tables.*;
import static io.github.bfvstats.util.DbUtils.getDslContext;
import static io.github.bfvstats.util.SortUtils.getJooqSortOrder;
import static io.github.bfvstats.util.SortUtils.getSortableField;

public class RankingService {

  public int getTotalPlayerCount() {
    int totalNumberOfRows = getDslContext().selectCount().from(PLAYER_RANK).fetchOne(0, int.class);
    return totalNumberOfRows;
  }

  public List<PlayerStats> getRankings(@Nonnull Sort sort, int page, @Nullable Integer playerId) {
    Field<?> sortableField = getSortableField(sort.getProperty());

    int numberOfRows = 50;
    int firstRowIndex = (page - 1) * numberOfRows;

    Table<Record4<Integer, Integer, BigDecimal, BigDecimal>> nestedHeals = getDslContext().select(
        ROUND_PLAYER_MEDPACK.PLAYER_ID,
        DSL.count(ROUND_PLAYER_MEDPACK.ID).as("heals_all_count"),
        DSL.sum(DSL.when(ROUND_PLAYER_MEDPACK.HEALED_PLAYER_ID.eq(ROUND_PLAYER_MEDPACK.PLAYER_ID), 1).otherwise(0))
            .as("heals_self_count"),
        DSL.sum(DSL.when(ROUND_PLAYER_MEDPACK.HEALED_PLAYER_ID.notEqual(ROUND_PLAYER_MEDPACK.PLAYER_ID), 1).otherwise(0))
            .as("heals_others_count")
    ).from(ROUND_PLAYER_MEDPACK)
        .groupBy(ROUND_PLAYER_MEDPACK.PLAYER_ID)
        .asTable().as("table_heals");

    Table<Record5<Integer, Integer, BigDecimal, BigDecimal, BigDecimal>> nestedRepairs = getDslContext().select(
        ROUND_PLAYER_REPAIR.PLAYER_ID,
        DSL.count(ROUND_PLAYER_REPAIR.ID).as("repairs_all_count"),
        DSL.sum(DSL.when(ROUND_PLAYER_REPAIR.VEHICLE_PLAYER_ID.eq(ROUND_PLAYER_REPAIR.PLAYER_ID), 1).otherwise(0))
            .as("repairs_self_count"),
        DSL.sum(DSL.when(ROUND_PLAYER_REPAIR.VEHICLE_PLAYER_ID.isNotNull().and(ROUND_PLAYER_REPAIR.VEHICLE_PLAYER_ID.notEqual(ROUND_PLAYER_REPAIR.PLAYER_ID)), 1).otherwise(0))
            .as("repairs_others_count"),
        DSL.sum(DSL.when(ROUND_PLAYER_REPAIR.VEHICLE_PLAYER_ID.isNull(), 1).otherwise(0))
            .as("repairs_unmanned_count")
    ).from(ROUND_PLAYER_REPAIR)
        .groupBy(ROUND_PLAYER_REPAIR.PLAYER_ID)
        .asTable().as("table_repairs");

    return getDslContext().select(
        DSL.count().as("rounds_played"),
        PLAYER.NAME.as("player_name"),
        PLAYER.KEYHASH.as("keyhash"),
        PLAYER_RANK.RANK.as("player_rank"),
        ROUND_END_STATS_PLAYER.PLAYER_ID.as("player_id"),
        DSL.sum(ROUND_END_STATS_PLAYER.SCORE).as("score"),
        DSL.sum(ROUND_END_STATS_PLAYER.SCORE).div(DSL.count()).as("average_score"),
        DSL.sum(ROUND_END_STATS_PLAYER.KILLS).as("kills"),
        DSL.sum(ROUND_END_STATS_PLAYER.DEATHS).as("deaths"),
        DSL.sum(ROUND_END_STATS_PLAYER.KILLS).cast(Float.class).div(DSL.sum(ROUND_END_STATS_PLAYER.DEATHS)).as("kdrate"), // kill/death rate
        DSL.sum(ROUND_END_STATS_PLAYER.TKS).as("tks"),
        DSL.sum(ROUND_END_STATS_PLAYER.CAPTURES).as("captures"),
        DSL.sum(ROUND_END_STATS_PLAYER.ATTACKS).as("attacks"),
        DSL.sum(ROUND_END_STATS_PLAYER.DEFENCES).as("defences"),
        DSL.sum(DSL.when(ROUND_END_STATS_PLAYER.RANK.eq(1), 1).otherwise(0)).as("gold_count"),
        DSL.sum(DSL.when(ROUND_END_STATS_PLAYER.RANK.eq(2), 1).otherwise(0)).as("silver_count"),
        DSL.sum(DSL.when(ROUND_END_STATS_PLAYER.RANK.eq(3), 1).otherwise(0)).as("bronze_count"),


        DSL.nvl(nestedHeals.field("heals_all_count"), 0).as("heals_all_count"),
        DSL.nvl(nestedHeals.field("heals_self_count"), 0).as("heals_self_count"),
        DSL.nvl(nestedHeals.field("heals_others_count"), 0).as("heals_others_count"),

        DSL.nvl(nestedRepairs.field("repairs_all_count"), 0).as("repairs_all_count"),
        DSL.nvl(nestedRepairs.field("repairs_self_count"), 0).as("repairs_self_count"),
        DSL.nvl(nestedRepairs.field("repairs_others_count"), 0).as("repairs_others_count"),
        DSL.nvl(nestedRepairs.field("repairs_unmanned_count"), 0).as("repairs_unmanned_count")
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
        .setPoints(1)
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