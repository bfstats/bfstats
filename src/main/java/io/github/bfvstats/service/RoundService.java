package io.github.bfvstats.service;

import io.github.bfvstats.game.jooq.tables.records.RoundRecord;
import io.github.bfvstats.model.Round;
import org.jooq.Result;

import java.util.List;
import java.util.stream.Collectors;

import static io.github.bfvstats.game.jooq.Tables.ROUND;
import static io.github.bfvstats.game.jooq.Tables.ROUND_END_STATS;
import static io.github.bfvstats.util.DbUtils.getDslContext;

public class RoundService {
  public List<Round> getRounds() {
    Result<RoundRecord> records = getDslContext().select().from(ROUND)
        .join(ROUND_END_STATS).on(ROUND_END_STATS.ROUND_ID.eq(ROUND.ID)) // skips rounds which don't have end result
        .fetch()
        .into(ROUND);
    return records.stream().map(RoundService::toRound).collect(Collectors.toList());
  }

  private static Round toRound(RoundRecord roundRecord) {
    return new Round()
        .setId(roundRecord.getId())
        .setMapCode(roundRecord.getMapCode())
        .setStartTime(roundRecord.getStartTime().toLocalDateTime());
  }

  public Round getRound(int roundId) {
    RoundRecord roundRecord = getDslContext().selectFrom(ROUND).where(ROUND.ID.eq(roundId)).fetchOne();
    return toRound(roundRecord);
  }
}
