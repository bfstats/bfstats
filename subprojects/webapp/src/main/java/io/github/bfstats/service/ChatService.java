package io.github.bfstats.service;

import io.github.bfstats.model.ChatMessage;
import io.github.bfstats.model.Location;
import org.jooq.Record;
import org.jooq.impl.DSL;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.bfstats.dbstats.jooq.Tables.*;
import static io.github.bfstats.util.DateTimeUtils.toUserZone;
import static io.github.bfstats.util.DbUtils.getDslContext;

public class ChatService {

  public List<ChatMessage> getChatMessages(Integer roundId, int page) {
    int numberOfRows = 50;
    int firstRowIndex = (page - 1) * numberOfRows;

    return getDslContext()
        .select(ROUND_CHAT_LOG.fields())
        .select(PLAYER.NAME, ROUND_PLAYER_TEAM.TEAM, ROUND.GAME_CODE, ROUND.MAP_CODE)
        .from(ROUND_CHAT_LOG)
        .join(ROUND).on(ROUND.ID.eq(ROUND_CHAT_LOG.ROUND_ID))
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
        .map(r -> toChatMessage(r))
        .collect(Collectors.toList());
  }

  private static ChatMessage toChatMessage(Record r) {
    String gameCode = r.get(ROUND.GAME_CODE);
    String mapCode = r.get(ROUND.MAP_CODE);
    Date eventTimeDate = r.get(ROUND_CHAT_LOG.EVENT_TIME, Date.class);

    BigDecimal x = r.get(ROUND_CHAT_LOG.PLAYER_LOCATION_X);
    BigDecimal y = r.get(ROUND_CHAT_LOG.PLAYER_LOCATION_Y);
    BigDecimal z = r.get(ROUND_CHAT_LOG.PLAYER_LOCATION_Z);
    Location location = new Location(x.floatValue(), y.floatValue(), z.floatValue());

    Integer playerTeam = r.get(ROUND_PLAYER_TEAM.TEAM);
    String playerTeamCode = KitService.getTeamCode(gameCode, mapCode, playerTeam);

    Integer targetTeamId = r.get(ROUND_CHAT_LOG.TO_TEAM); // is 0 if for all;
    String targetTeamName = toTeamName(targetTeamId); // nullable

    return new ChatMessage()
        .setPlayerId(r.get(ROUND_CHAT_LOG.PLAYER_ID, Integer.class))
        .setPlayerName(r.get(PLAYER.NAME, String.class))
        .setLocation(location)
        .setText(r.get(ROUND_CHAT_LOG.MESSAGE, String.class))
        .setTime(toUserZone(toLocalDateTime(eventTimeDate)))
        .setTargetTeamId(targetTeamId)
        .setTargetTeamName(targetTeamName)
        .setPlayerTeam(playerTeam)
        .setPlayerTeamCode(playerTeamCode)
        .setRoundId(r.get(ROUND_CHAT_LOG.ROUND_ID));
  }

  @Nullable
  private static String toTeamName(int teamId) {
    if (teamId == 0) {
      return null;
    }
    switch (teamId) {
      case 1:
        return "NVA";
      case 2:
        return "USA";
      case 3:
        return "SPECTATORS";
    }
    return "UNKNOWNTEAM" + teamId;
  }

  private static LocalDateTime toLocalDateTime(Date date) {
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }

  public int getTotalMessagesCount() {
    return getDslContext().selectCount().from(ROUND_CHAT_LOG).fetchOne(0, int.class);
  }
}
