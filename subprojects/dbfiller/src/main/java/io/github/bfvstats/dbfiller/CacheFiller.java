package io.github.bfvstats.dbfiller;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static io.github.bfvstats.game.jooq.Tables.PLAYER_RANK;
import static io.github.bfvstats.game.jooq.Tables.ROUND_END_STATS_PLAYER;
import static org.jooq.impl.DSL.sum;

public class CacheFiller {
  public static void main(String[] args) throws JAXBException, FileNotFoundException, SQLException {
    fillCacheTables();
  }

  public static void fillCacheTables() throws SQLException {
    Connection connection;
    DSLContext dslContext;

    try {
      connection = DriverManager.getConnection("jdbc:sqlite:baas.db");
      dslContext = DSL.using(connection);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    dslContext.transaction(configuration -> {
      DSLContext transactionContext = DSL.using(configuration);

      transactionContext.delete(PLAYER_RANK).execute();

      transactionContext.insertInto(PLAYER_RANK, PLAYER_RANK.PLAYER_ID)
          .select(transactionContext.select(ROUND_END_STATS_PLAYER.PLAYER_ID)
              .from(ROUND_END_STATS_PLAYER)
              .groupBy(ROUND_END_STATS_PLAYER.PLAYER_ID)
              .orderBy(sum(ROUND_END_STATS_PLAYER.SCORE).desc()))
          .execute();
    });

    dslContext.close();
    connection.close();
  }
}
