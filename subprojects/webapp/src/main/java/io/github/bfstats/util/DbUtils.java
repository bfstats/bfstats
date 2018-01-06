package io.github.bfstats.util;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import ro.pippo.core.route.RouteContext;
import ro.pippo.core.route.RouteDispatcher;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbUtils {
  private static final String username = "";
  private static final String password = "";

  private static Connection connection;
  private static DSLContext genericDslContext;


  static {
    try {
      Properties props = loadConfigProperties();
      String dbUrl = props.getProperty("databaseUrl", "jdbc:sqlite:baas.db");
      connection = DriverManager.getConnection(dbUrl, username, password);
    } catch (SQLException | IOException e) {
      throw new RuntimeException(e);
    }

  }

  public static Properties loadConfigProperties() throws IOException {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    InputStream configFileInputStream = loader.getResourceAsStream("ftpconfig.properties");
    Properties props = new Properties();
    props.load(configFileInputStream);
    return props;
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
