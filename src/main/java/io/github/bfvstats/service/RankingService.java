package io.github.bfvstats.service;

import io.github.bfvstats.model.PlayerStats;
import io.github.bfvstats.util.Sort;
import org.jooq.Field;
import org.jooq.Record;
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
        DSL.sum(ROUND_END_STATS_PLAYER.KILLS).div(DSL.sum(ROUND_END_STATS_PLAYER.DEATHS)).as("kdrate"), // kill/death rate
        DSL.sum(ROUND_END_STATS_PLAYER.TKS).as("tks"),
        DSL.sum(ROUND_END_STATS_PLAYER.CAPTURES).as("captures"),
        DSL.sum(ROUND_END_STATS_PLAYER.ATTACKS).as("attacks"),
        DSL.sum(ROUND_END_STATS_PLAYER.DEFENCES).as("defences"),
        DSL.sum(DSL.when(ROUND_END_STATS_PLAYER.RANK.eq(1), 1).otherwise(0)).as("gold_count"),
        DSL.sum(DSL.when(ROUND_END_STATS_PLAYER.RANK.eq(2), 1).otherwise(0)).as("silver_count"),
        DSL.sum(DSL.when(ROUND_END_STATS_PLAYER.RANK.eq(3), 1).otherwise(0)).as("bronze_count"),
        DSL.count(ROUND_PLAYER_MEDPACK.ID).as("heals_count"),
        DSL.count(ROUND_PLAYER_REPAIR.ID).as("repairs_count")
    )
        .from(ROUND_END_STATS_PLAYER)
        .join(PLAYER).on(PLAYER.ID.eq(ROUND_END_STATS_PLAYER.PLAYER_ID))
        .join(PLAYER_RANK).on(PLAYER_RANK.PLAYER_ID.eq(ROUND_END_STATS_PLAYER.PLAYER_ID))
        .leftJoin(ROUND_PLAYER_MEDPACK).on(ROUND_PLAYER_MEDPACK.PLAYER_ID.eq(ROUND_END_STATS_PLAYER.PLAYER_ID)
            .and(ROUND_PLAYER_MEDPACK.ROUND_ID.eq(ROUND_END_STATS_PLAYER.ROUND_ID)))
        .leftJoin(ROUND_PLAYER_REPAIR).on(ROUND_PLAYER_REPAIR.PLAYER_ID.eq(ROUND_END_STATS_PLAYER.PLAYER_ID)
            .and(ROUND_PLAYER_REPAIR.ROUND_ID.eq(ROUND_END_STATS_PLAYER.ROUND_ID)))
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
    String partialKeyHash = "..." + keyhash.substring(20);

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
        .setRoundsPlayed(r.get("rounds_played", Integer.class))
        .setHeals(r.get("heals_count", Integer.class))
        .setSelfHeals(0)
        .setOtherRepairs(0) // with vehicle_player
        .setRepairs(r.get("repairs_count", Integer.class)) // without vehicle_player
        .setScorePerMinute(0);
  }
}