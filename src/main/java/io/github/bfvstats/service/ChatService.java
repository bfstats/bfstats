package io.github.bfvstats.service;

import io.github.bfvstats.model.ChatMessage;
import org.jooq.Record;
import org.jooq.impl.DSL;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.bfvstats.game.jooq.Tables.*;
import static io.github.bfvstats.util.DbUtils.getDslContext;

public class ChatService {

  public List<ChatMessage> getChatMessages(Integer roundId) {
    return getDslContext()
        .select(ROUND_CHAT_LOG.PLAYER_ID, ROUND_CHAT_LOG.MESSAGE, ROUND_CHAT_LOG.EVENT_TIME, ROUND_CHAT_LOG.TO_TEAM, PLAYER.NAME, ROUND_PLAYER_TEAM.TEAM, ROUND_PLAYER_TEAM.ROUND_ID)
        .from(ROUND_CHAT_LOG)
        .join(PLAYER).on(PLAYER.ID.eq(ROUND_CHAT_LOG.PLAYER_ID))
        .leftJoin(ROUND_PLAYER_TEAM).on(ROUND_PLAYER_TEAM.ROUND_ID.eq(ROUND_CHAT_LOG.ROUND_ID)
            .and(ROUND_PLAYER_TEAM.PLAYER_ID.eq(ROUND_CHAT_LOG.PLAYER_ID))
            .and(ROUND_CHAT_LOG.EVENT_TIME.between(ROUND_PLAYER_TEAM.START_TIME, ROUND_PLAYER_TEAM.END_TIME))
        )
        .where(roundId == null ? DSL.trueCondition() : ROUND_CHAT_LOG.ROUND_ID.eq(roundId))
        .orderBy(ROUND_CHAT_LOG.EVENT_TIME.sortAsc())
        //.limit(0, 50)
        .fetch()
        .stream()
        .map(this::toChatMessage)
        .collect(Collectors.toList());
  }

  private ChatMessage toChatMessage(Record r) {
    Date eventTimeDate = r.get(ROUND_CHAT_LOG.EVENT_TIME, Date.class);

    return new ChatMessage()
        .setPlayerId(r.get(ROUND_CHAT_LOG.PLAYER_ID, Integer.class))
        .setPlayerName(r.get(PLAYER.NAME, String.class))
        .setText(r.get(ROUND_CHAT_LOG.MESSAGE, String.class))
        .setTime(toLocalDateTime(eventTimeDate))
        .setToTeam(r.get(ROUND_CHAT_LOG.TO_TEAM))
        .setPlayerTeam(r.get(ROUND_PLAYER_TEAM.TEAM))
        .setRoundId(r.get(ROUND_PLAYER_TEAM.ROUND_ID));
  }

  private static LocalDateTime toLocalDateTime(Date date) {
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }
}
