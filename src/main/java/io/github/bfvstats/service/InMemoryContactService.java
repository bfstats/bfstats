package io.github.bfvstats.service;

import io.github.bfvstats.Contact;

import java.util.ArrayList;
import java.util.List;

public class InMemoryContactService implements ContactService {

  @Override
  public List<Contact> getContacts() {
    List<Contact> contacts = new ArrayList<>();
    contacts.add(getContact(1));
    contacts.add(getContact(2));
    return contacts;
  }

  @Override
  public Contact getContact(int id) {
    return new Contact()
        .setId(id)
        .setName("John")
        .setPhone("0733434435")
        .setAddress("Sunflower Street, No. 6");
  }
}
