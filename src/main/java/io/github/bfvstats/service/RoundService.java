package io.github.bfvstats.service;

import io.github.bfvstats.game.jooq.tables.records.RoundEndStatsPlayerRecord;
import io.github.bfvstats.game.jooq.tables.records.RoundEndStatsRecord;
import io.github.bfvstats.game.jooq.tables.records.RoundRecord;
import io.github.bfvstats.model.Round;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jooq.Result;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.bfvstats.game.jooq.Tables.*;
import static io.github.bfvstats.util.DbUtils.getDslContext;

public class RoundService {
  public List<Round> getRounds() {
    Result<RoundRecord> records = getDslContext().select().from(ROUND)
        .join(ROUND_END_STATS).on(ROUND_END_STATS.ROUND_ID.eq(ROUND.ID)) // skips rounds which don't have end result
        .orderBy(ROUND.START_TIME.desc())
        .fetch()
        .into(ROUND);
    return records.stream().map(RoundService::toRound).collect(Collectors.toList());
  }

  private static Round toRound(RoundRecord roundRecord) {
    String mapCode = roundRecord.getMapCode();
    String mapName = MapService.mapName(mapCode);

    return new Round()
        .setId(roundRecord.getId())
        .setMapCode(mapCode)
        .setMapName(mapName)
        .setStartTime(roundRecord.getStartTime().toLocalDateTime());
  }

  public Round getRound(int roundId) {
    RoundRecord roundRecord = getDslContext().selectFrom(ROUND).where(ROUND.ID.eq(roundId)).fetchOne();

    return toRound(roundRecord);
  }

  public RoundEndStatistics getRoundEndStats(int roundId) {
    RoundEndStatsRecord roundEndStatsRecord = getDslContext().selectFrom(ROUND_END_STATS)
        .where(ROUND_END_STATS.ROUND_ID.eq(roundId)).fetchOne();

    return toRoundEndStats(roundEndStatsRecord);
  }

  private RoundEndStatistics toRoundEndStats(RoundEndStatsRecord r) {
    return new RoundEndStatistics()
        .setRoundId(r.getRoundId())
        .setEndTime(r.getEndTime().toLocalDateTime())
        .setWinningTeam(r.getWinningTeam())
        .setVictoryType(r.getVictoryType())
        .setEndTicketsTeam1(r.getEndTicketsTeam_1())
        .setEndTicketsTeam2(r.getEndTicketsTeam_2());
  }

  @Data
  @Accessors(chain = true)
  public static class RoundEndStatistics {
    private int roundId;
    private LocalDateTime endTime;
    private int winningTeam;
    private int victoryType;
    private int endTicketsTeam1;
    private int endTicketsTeam2;
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
