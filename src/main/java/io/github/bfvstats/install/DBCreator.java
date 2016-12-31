package io.github.bfvstats.install;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DBCreator {

  public static void main(String[] args) throws FileNotFoundException, SQLException {
    InputStream is = new FileInputStream("D:\\Projects\\bfvstats\\src\\main\\resources\\db\\install.sql");

    DriverManager.registerDriver(new org.sqlite.JDBC());
    Connection connection = DriverManager.getConnection("jdbc:sqlite:baas.db");
    importSQL(connection, is);
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
