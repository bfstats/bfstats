package io.github.bfvstats.service;

// For convenience, always static import your generated tables and jOOQ functions to decrease verbosity:

import io.github.bfvstats.Contact;
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

public class DatabaseContractService implements ContactService {
  @Override
  public List<Contact> getContacts() {

    List<Contact> contacts = new ArrayList<>();

    // Connection is the only JDBC resource that we need
    // PreparedStatement and ResultSet are handled by jOOQ, internally
    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db", "", "")) {
      DSLContext create = DSL.using(conn);
      Result<Record> result = create.select().from(SELECTBF_PLAYERS).fetch();

      for (Record r : result) {
        Integer id = r.getValue(SELECTBF_PLAYERS.ID);
        String nickname = r.getValue(SELECTBF_PLAYERS.NAME);
        String keyHash = r.getValue(SELECTBF_PLAYERS.KEYHASH);

        Contact contact = new Contact().setId(id)
            .setName(nickname)
            .setAddress(keyHash);
        contacts.add(contact);

        System.out.println("ID: " + id + " nickname: " + nickname + " key hash: " + keyHash);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return contacts;
  }

  @Override
  public Contact getContact(int id) {
    return null;
  }
}
