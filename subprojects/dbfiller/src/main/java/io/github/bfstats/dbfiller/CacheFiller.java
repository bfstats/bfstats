package io.github.bfstats.dbfiller;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static io.github.bfstats.game.jooq.Tables.*;
import static org.jooq.impl.DSL.sum;

public class CacheFiller {
  public static void main(String[] args) throws IOException, SQLException {
    Properties props = DbFiller.loadDbConfigProperties();
    String dbUrl = props.getProperty("databaseUrl", "jdbc:sqlite:database.db");
    fillCacheTables(dbUrl);
  }

  public static void fillCacheTables(String url) throws SQLException {
    Connection connection;
    DSLContext dslContext;

    try {
      connection = DriverManager.getConnection(url);
      dslContext = DSL.using(connection);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    dslContext.transaction(configuration -> {
      DSLContext transactionContext = DSL.using(configuration);

      transactionContext.deleteFrom(PLAYER_RANK).execute();
      transactionContext.deleteFrom(SQLITE_SEQUENCE).where(SQLITE_SEQUENCE.NAME.eq(PLAYER_RANK.getName())).execute();

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
