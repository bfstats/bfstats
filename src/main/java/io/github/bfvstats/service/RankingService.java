package io.github.bfvstats.service;

import io.github.bfvstats.model.PlayerStats;
import io.github.bfvstats.util.Sort;
import org.jooq.Record;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.bfvstats.game.jooq.Tables.*;
import static io.github.bfvstats.util.DbUtils.getDslContext;

public class RankingService {

  /*private Map<String, Field<?>> orderOverrides = ImmutableMap.<String, Field<?>>builder()
      .put("averageScore", SELECTBF_CACHE_RANKING.SCORE.div(SELECTBF_CACHE_RANKING.ROUNDS_PLAYED))
      .build();*/

  public List<PlayerStats> getRankings(Sort sort) {
    int minimumRounds = 0;

    if (sort == null) {
      sort = new Sort("rank", Sort.SortOrder.ASC);
    }

    //Field<?> sortableField = getSortableFieldOrOverride(SELECTBF_CACHE_RANKING, sort.getProperty(), orderOverrides);
    /*Result<SelectbfCacheRankingRecord> records = getDslContext()
        .selectFrom(SELECTBF_CACHE_RANKING)
        .where(SELECTBF_CACHE_RANKING.ROUNDS_PLAYED.greaterOrEqual(minimumRounds))
        .orderBy(sortableField.sort(getJooqSortOrder(sort.getOrder())))
        .limit(0, 50)
        .fetch();*/


    return getDslContext().select(
        DSL.count().as("rounds_played"),
        PLAYER.NAME.as("player_name"),
        PLAYER_RANK.RANK.as("player_rank"),
        ROUND_END_STATS_PLAYER.PLAYER_ID.as("player_id"),
        DSL.sum(ROUND_END_STATS_PLAYER.SCORE).as("score"),
        DSL.sum(ROUND_END_STATS_PLAYER.KILLS).as("kills"),
        DSL.sum(ROUND_END_STATS_PLAYER.DEATHS).as("deaths"),
        DSL.sum(ROUND_END_STATS_PLAYER.KILLS).div(DSL.sum(ROUND_END_STATS_PLAYER.DEATHS)).as("kdrate"), // kill/death rate
        DSL.sum(ROUND_END_STATS_PLAYER.TKS).as("tks"),
        DSL.sum(ROUND_END_STATS_PLAYER.CAPTURES).as("captures"),
        DSL.sum(ROUND_END_STATS_PLAYER.ATTACKS).as("attacks"),
        DSL.sum(ROUND_END_STATS_PLAYER.DEFENCES).as("defences")
    )
        .from(ROUND_END_STATS_PLAYER)
        .join(PLAYER).on(PLAYER.ID.eq(ROUND_END_STATS_PLAYER.PLAYER_ID))
        .join(PLAYER_RANK).on(PLAYER_RANK.PLAYER_ID.eq(ROUND_END_STATS_PLAYER.PLAYER_ID))
        .groupBy(ROUND_END_STATS_PLAYER.PLAYER_ID)
        .orderBy(PLAYER_RANK.RANK.asc())
        .limit(0, 50)
        .fetch()
        .stream()
        .map(this::toPlayerStats)
        .collect(Collectors.toList());
  }

  private PlayerStats toPlayerStats(Record r) {
    Integer score = r.get("score", Integer.class);
    int kills = r.get("kills", Integer.class);
    int deaths = r.get("deaths", Integer.class);

    BigDecimal kdRateBigDec = r.get("kdrate", BigDecimal.class);
    Double kdRate = kdRateBigDec == null ? 0.0 : kdRateBigDec.doubleValue();

    /*Double kdRate = 0.0;
    if (deaths != 0) {
      kdRate = (double) (kills / deaths);
    }*/

    return new PlayerStats()
        .setId(r.get("player_id", Integer.class))
        .setName(r.get("player_name", String.class))
        .setRank(r.get("player_rank", Integer.class))
        .setPoints(1)
        .setScore(score)
        .setAverageScore(score / r.get("rounds_played", Integer.class))
        .setKills(kills)
        .setDeaths(deaths)
        .setKillDeathRatio(kdRate)
        .setGoldCount(0)
        .setSilverCount(0)
        .setBronzeCount(0)
        .setTeamKills(r.get("tks", Integer.class))
        .setAttacks(r.get("attacks", Integer.class))
        .setCaptures(r.get("captures", Integer.class))
        .setObjectives(0)
        .setRoundsPlayed(r.get("rounds_played", Integer.class))
        .setHeals(0)
        .setOtherRepairs(0)
        .setSelfHeals(0)
        .setRepairs(0)
        .setScorePerMinute(0);
  }
}