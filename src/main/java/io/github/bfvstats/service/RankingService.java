package io.github.bfvstats.service;

import com.google.common.collect.ImmutableMap;
import io.github.bfvstats.jpa.tables.records.SelectbfCacheRankingRecord;
import io.github.bfvstats.model.PlayerStats;
import io.github.bfvstats.util.Sort;
import org.jooq.Field;
import org.jooq.Result;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.bfvstats.jpa.Tables.SELECTBF_CACHE_RANKING;
import static io.github.bfvstats.util.DbUtils.getDslContext;
import static io.github.bfvstats.util.SortUtils.getJooqSortOrder;
import static io.github.bfvstats.util.SortUtils.getSortableFieldOrOverride;

public class RankingService {

  private Map<String, Field<?>> orderOverrides = ImmutableMap.<String, Field<?>>builder()
      .put("averageScore", SELECTBF_CACHE_RANKING.SCORE.div(SELECTBF_CACHE_RANKING.ROUNDS_PLAYED))
      .build();

  public List<PlayerStats> getRankings(Sort sort) {
    int minimumRounds = 0;

    if (sort == null) {
      sort = new Sort("rank", Sort.SortOrder.ASC);
    }

    Field<?> sortableField = getSortableFieldOrOverride(SELECTBF_CACHE_RANKING, sort.getProperty(), orderOverrides);

    Result<SelectbfCacheRankingRecord> records = getDslContext()
        .selectFrom(SELECTBF_CACHE_RANKING)
        .where(SELECTBF_CACHE_RANKING.ROUNDS_PLAYED.greaterOrEqual(minimumRounds))
        .orderBy(sortableField.sort(getJooqSortOrder(sort.getOrder())))
        .limit(0, 50)
        .fetch();

    return records.stream().map(this::toPlayerStats).collect(Collectors.toList());
  }

  private PlayerStats toPlayerStats(SelectbfCacheRankingRecord r) {
    return new PlayerStats()
        .setId(r.getPlayerId())
        .setRank(r.getRank())
        .setName(r.getPlayername())
        .setPoints(r.getScore())
        .setAverageScore(r.getScore() / r.getRoundsPlayed())
        .setScore(r.getScore())
        .setKills(r.getKills())
        .setDeaths(r.getDeaths())
        .setKillDeathRatio(r.getKdrate())
        .setGoldCount(r.getFirst())
        .setSilverCount(r.getSecond())
        .setBronzeCount(r.getThird())
        .setTeamKills(r.getTks())
        .setAttacks(r.getAttacks())
        .setCaptures(r.getCaptures())
        .setObjectives(r.getObjectives())
        .setRoundsPlayed(r.getRoundsPlayed())
        .setHeals(r.getHeals())
        .setOtherRepairs(r.getOtherrepairs())
        .setSelfHeals(r.getSelfheals())
        .setRepairs(r.getRepairs())
        .setScorePerMinute(r.getScorePerMinute());
  }
}