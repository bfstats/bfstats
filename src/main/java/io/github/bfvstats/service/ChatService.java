package io.github.bfvstats.service;


import io.github.bfvstats.model.ChatMessage;
import org.jooq.Record;
import org.jooq.Record5;
import org.jooq.Result;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.bfvstats.jpa.Tables.*;
import static io.github.bfvstats.util.DbUtils.getDslContext;

public class ChatService {

  public List<ChatMessage> getChatMessages() {
    Result<Record5<Short, String, Timestamp, String, Byte>> records = getDslContext()
        .select(SELECTBF_CHATLOG.PLAYER_ID, SELECTBF_CHATLOG.TEXT, SELECTBF_CHATLOG.INSERTTIME,
            SELECTBF_PLAYERS.NAME.as("player_name"),
            SELECTBF_PLAYERSTATS.TEAM.as("team"))
        .from(SELECTBF_CHATLOG)
        .join(SELECTBF_PLAYERS).on(SELECTBF_PLAYERS.ID.eq(SELECTBF_CHATLOG.PLAYER_ID.cast(Integer.class)))
        .join(SELECTBF_PLAYERSTATS).on(SELECTBF_PLAYERSTATS.ROUND_ID.eq(SELECTBF_CHATLOG.ROUND_ID)
            .and(SELECTBF_PLAYERSTATS.PLAYER_ID.eq(SELECTBF_CHATLOG.PLAYER_ID.cast(Integer.class))))
        .orderBy(SELECTBF_CHATLOG.INSERTTIME.sortAsc())
        .limit(0, 50)
        .fetch();
    return records.stream().map(this::toChatMessage).collect(Collectors.toList());
  }

  private ChatMessage toChatMessage(Record r) {

    return new ChatMessage()
        .setPlayerId(r.get("player_id", Integer.class))
        .setPlayerName(r.get("player_name", String.class))
        .setText(r.get("text", String.class))
        .setTime(r.get("inserttime", Date.class))
        .setTeam(String.valueOf(r.get("team", Integer.class)));
  }
}
