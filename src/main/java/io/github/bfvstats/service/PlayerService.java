package io.github.bfvstats.service;


import io.github.bfvstats.Player;
import io.github.bfvstats.jpa.tables.records.SelectbfPlayersRecord;
import org.jooq.Result;

import java.util.List;
import java.util.stream.Collectors;

import static io.github.bfvstats.jpa.Tables.SELECTBF_PLAYERS;
import static io.github.bfvstats.util.DbUtils.getDslContext;

public class PlayerService {
  public List<Player> getPlayers() {
    Result<SelectbfPlayersRecord> records = getDslContext().selectFrom(SELECTBF_PLAYERS).fetch();
    return records.stream().map(PlayerService::toPlayer).collect(Collectors.toList());
  }

  public Player getPlayer(int id) {
    SelectbfPlayersRecord r = getDslContext().selectFrom(SELECTBF_PLAYERS).
        where(SELECTBF_PLAYERS.ID.eq(id)).fetchOne();
    return toPlayer(r);
  }

  private static Player toPlayer(SelectbfPlayersRecord r) {
    return new Player()
        .setId(r.getId())
        .setName(r.getName())
        .setKeyHash(r.getKeyhash());
  }
}