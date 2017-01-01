package io.github.bfvstats.dbfiller;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
  private static final String url = "jdbc:sqlite:baas.db";
  private static final String username = "";
  private static final String password = "";

  private static Connection connection;
  private static DSLContext genericDslContext;

  static {
    try {
      connection = DriverManager.getConnection(url, username, password);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static DSLContext getDslContext() {
    if (genericDslContext == null) {
      genericDslContext = createDslContext();
    }

    return genericDslContext;
  }

  public static void closeDslContext() {
    if (genericDslContext != null) {
      //connection.close();
      genericDslContext.close();
    }
  }

  private static DSLContext createDslContext() {
    return DSL.using(connection);
  }
}
