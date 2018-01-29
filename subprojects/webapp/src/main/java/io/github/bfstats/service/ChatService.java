package io.github.bfstats.service;

import io.github.bfstats.model.ChatMessage;
import io.github.bfstats.model.Location;
import org.jooq.Record;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.bfstats.dbstats.jooq.Tables.*;
import static io.github.bfstats.util.DbUtils.getDslContext;

public class ChatService {

  public List<ChatMessage> getChatMessages(Integer roundId, int page) {
    int numberOfRows = 50;
    int firstRowIndex = (page - 1) * numberOfRows;

    return getDslContext()
        .select(ROUND_CHAT_LOG.fields())
        .select(PLAYER.NAME, ROUND_PLAYER_TEAM.TEAM)
        .from(ROUND_CHAT_LOG)
        .join(PLAYER).on(PLAYER.ID.eq(ROUND_CHAT_LOG.PLAYER_ID))
        .leftJoin(ROUND_PLAYER_TEAM).on(ROUND_PLAYER_TEAM.ROUND_ID.eq(ROUND_CHAT_LOG.ROUND_ID)
            .and(ROUND_PLAYER_TEAM.PLAYER_ID.eq(ROUND_CHAT_LOG.PLAYER_ID))
            .and(ROUND_CHAT_LOG.EVENT_TIME.between(ROUND_PLAYER_TEAM.START_TIME, ROUND_PLAYER_TEAM.END_TIME))
        )
        .where(roundId == null ? DSL.trueCondition() : ROUND_CHAT_LOG.ROUND_ID.eq(roundId))
        // order by date descending, but time ascending
        .orderBy(
            DSL.date(ROUND_CHAT_LOG.EVENT_TIME).desc(),
            ROUND_CHAT_LOG.EVENT_TIME.asc()
        )
        .limit(firstRowIndex, numberOfRows)
        .fetch()
        .stream()
        .map(this::toChatMessage)
        .collect(Collectors.toList());
  }

  private ChatMessage toChatMessage(Record r) {
    Date eventTimeDate = r.get(ROUND_CHAT_LOG.EVENT_TIME, Date.class);

    BigDecimal x = r.get(ROUND_CHAT_LOG.PLAYER_LOCATION_X);
    BigDecimal y = r.get(ROUND_CHAT_LOG.PLAYER_LOCATION_Y);
    BigDecimal z = r.get(ROUND_CHAT_LOG.PLAYER_LOCATION_Z);
    Location location = new Location(x.floatValue(), y.floatValue(), z.floatValue());

    return new ChatMessage()
        .setPlayerId(r.get(ROUND_CHAT_LOG.PLAYER_ID, Integer.class))
        .setPlayerName(r.get(PLAYER.NAME, String.class))
        .setLocation(location)
        .setText(r.get(ROUND_CHAT_LOG.MESSAGE, String.class))
        .setTime(toLocalDateTime(eventTimeDate))
        .setToTeam(r.get(ROUND_CHAT_LOG.TO_TEAM))
        .setPlayerTeam(r.get(ROUND_PLAYER_TEAM.TEAM))
        .setRoundId(r.get(ROUND_CHAT_LOG.ROUND_ID));
  }

  private static LocalDateTime toLocalDateTime(Date date) {
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }

  public int getTotalMessagesCount() {
    return getDslContext().selectCount().from(ROUND_CHAT_LOG).fetchOne(0, int.class);
  }
}
