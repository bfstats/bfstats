package io.github.bfvstats.service;

import io.github.bfvstats.model.ChatMessage;
import org.jooq.Record;
import org.jooq.Record4;
import org.jooq.Result;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.bfvstats.game.jooq.Tables.PLAYER;
import static io.github.bfvstats.game.jooq.Tables.ROUND_CHAT_LOG;
import static io.github.bfvstats.util.DbUtils.getDslContext;

public class ChatService {

  public List<ChatMessage> getChatMessages() {
    Result<Record4<Integer, String, Timestamp, String>> records = getDslContext().select(ROUND_CHAT_LOG.PLAYER_ID, ROUND_CHAT_LOG.MESSAGE, ROUND_CHAT_LOG.EVENT_TIME, PLAYER.NAME)
        .from(ROUND_CHAT_LOG)
        .join(PLAYER).on(PLAYER.ID.eq(ROUND_CHAT_LOG.PLAYER_ID))
        .orderBy(ROUND_CHAT_LOG.EVENT_TIME.sortAsc())
        .limit(0, 50)
        .fetch();

    return records.stream().map(this::toChatMessage).collect(Collectors.toList());
  }

  private ChatMessage toChatMessage(Record r) {

    return new ChatMessage()
        .setPlayerId(r.get(ROUND_CHAT_LOG.PLAYER_ID, Integer.class))
        .setPlayerName(r.get(PLAYER.NAME, String.class))
        .setText(r.get(ROUND_CHAT_LOG.MESSAGE, String.class))
        .setTime(r.get(ROUND_CHAT_LOG.EVENT_TIME, Date.class))
        .setTeam(String.valueOf(r.get(ROUND_CHAT_LOG.TEAM, Integer.class)));
  }
}
