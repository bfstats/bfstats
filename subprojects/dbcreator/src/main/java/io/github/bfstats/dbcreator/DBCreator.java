package io.github.bfstats.dbcreator;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Stream;

public class DBCreator {

  public static void main(String[] args) throws IOException, SQLException {
    Properties props = loadDbConfigProperties();
    String dbUrl = props.getProperty("databaseUrl", "jdbc:sqlite:database.db");
    DriverManager.registerDriver(new org.sqlite.JDBC());
    Connection connection = DriverManager.getConnection(dbUrl);

    boolean updatePlayerViewContents = Stream.of(args).anyMatch("updateviews"::equals);
    if (updatePlayerViewContents) {
      Statement statement = connection.createStatement();
      statement.execute("DROP VIEW IF EXISTS player_summary_no_rank;");
      statement.execute("DROP VIEW IF EXISTS player_points;");
      statement.close();
      System.out.println("Dropped views so they will be recreated");
    }

    InputStream is = DBCreator.class.getResourceAsStream("/db/install.sql");
    importSQL(connection, is);
  }

  public static Properties loadDbConfigProperties() throws IOException {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    InputStream configFileInputStream = loader.getResourceAsStream("dbconfig.properties");
    Properties props = new Properties();
    props.load(configFileInputStream);
    return props;
  }

  private static void importSQL(Connection conn, InputStream in) throws SQLException {
    Scanner s = new Scanner(in);
    //s.useDelimiter("(;(\r)?\n)|((\r)?\n)?(--)?.*(--(\r)?\n)");
    s.useDelimiter("(;(\r)?\n)|(--\n)");
    Statement st = null;
    try {
      st = conn.createStatement();
      while (s.hasNext()) {
        String line = s.next();
        if (line.startsWith("#")) {
          continue;
        }
        if (line.startsWith("/*!") && line.endsWith("*/")) {
          int i = line.indexOf(' ');
          line = line.substring(i + 1, line.length() - " */".length());
        }

        if (line.trim().length() > 0) {
          st.execute(line);
        }
      }
    } finally {
      if (st != null) st.close();
    }
  }
}
