package io.github.bfvstats.util;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import ro.pippo.core.route.RouteContext;
import ro.pippo.core.route.RouteDispatcher;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtils {
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
    RouteContext routeContext = getRouteContext();
    if (routeContext == null) {
      if (genericDslContext == null) {
        genericDslContext = createDslContext();
      }

      return genericDslContext;
    }

    DSLContext db = routeContext.getLocal("db");
    if (db == null) {
      db = createDslContext();
      routeContext.setLocal("db", db);
    }

    return db;
  }

  public static void closeDslContext() {
    RouteContext routeContext = getRouteContext();
    if (routeContext == null) {
      if (genericDslContext != null) {
        //connection.close();
        genericDslContext.close();
      }
    } else {
      DSLContext dslContext = routeContext.removeLocal("db");
      if (dslContext != null) {
        dslContext.close();
      }
    }
  }

  private static RouteContext getRouteContext() {
    return RouteDispatcher.getRouteContext();
  }

  private static DSLContext createDslContext() {
    return DSL.using(connection);
  }
}