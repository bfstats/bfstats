package io.github.bfvstats.service;

// For convenience, always static import your generated tables and jOOQ functions to decrease verbosity:

import io.github.bfvstats.Player;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static io.github.bfvstats.jpa.tables.SelectbfPlayers.SELECTBF_PLAYERS;

public class DatabaseContractService implements PlayerService {
  @Override
  public List<Player> getPlayers() {

    List<Player> players = new ArrayList<>();

    // Connection is the only JDBC resource that we need
    // PreparedStatement and ResultSet are handled by jOOQ, internally
    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db", "", "")) {
      DSLContext create = DSL.using(conn);
      Result<Record> result = create.select().from(SELECTBF_PLAYERS).fetch();

      for (Record r : result) {
        Integer id = r.getValue(SELECTBF_PLAYERS.ID);
        String nickname = r.getValue(SELECTBF_PLAYERS.NAME);
        String keyHash = r.getValue(SELECTBF_PLAYERS.KEYHASH);

        Player player = new Player().setId(id)
            .setName(nickname)
            .setKeyHash(keyHash);
        players.add(player);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return players;
  }

  @Override
  public Player getPlayer(int id) {
    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db", "", "")) {
      DSLContext create = DSL.using(conn);
      Record r = create.select().from(SELECTBF_PLAYERS).
          where(SELECTBF_PLAYERS.ID.equal(id)).fetchOne();

      String nickname = r.getValue(SELECTBF_PLAYERS.NAME);
      String keyHash = r.getValue(SELECTBF_PLAYERS.KEYHASH);

      return new Player().setId(id)
          .setName(nickname)
          .setKeyHash(keyHash);

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }
}
