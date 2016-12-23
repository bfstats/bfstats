package io.github.bfvstats.service;

import io.github.bfvstats.jpa.tables.records.SelectbfCacheRankingRecord;
import io.github.bfvstats.model.PlayerStats;
import org.jooq.Result;

import java.util.ArrayList;
import java.util.List;

import static io.github.bfvstats.jpa.Tables.SELECTBF_CACHE_RANKING;
import static io.github.bfvstats.util.DbUtils.getDslContext;

public class RankingService {

  public List<PlayerStats> getRankings() {
    List<PlayerStats> players = new ArrayList<>();
    int minimumRounds = 0;
    Result<SelectbfCacheRankingRecord> records = getDslContext().selectFrom(SELECTBF_CACHE_RANKING)
        .where(SELECTBF_CACHE_RANKING.ROUNDS_PLAYED.greaterOrEqual(minimumRounds))
        .orderBy(SELECTBF_CACHE_RANKING.RANK.asc())
        .limit(0, 50)
        .fetch();
    records.forEach(r -> players.add(toPlayerStats(r)));
    return players;
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