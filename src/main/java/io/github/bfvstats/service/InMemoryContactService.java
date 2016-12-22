package io.github.bfvstats.service;

import io.github.bfvstats.Contact;

import java.util.ArrayList;
import java.util.List;

public class InMemoryContactService implements ContactService {

  @Override
  public List<Contact> getContacts() {
    return new ArrayList<>();
  }

  @Override
  public Contact getContact(int id) {
    return new Contact()
        .setId(12345)
        .setName("John")
        .setPhone("0733434435")
        .setAddress("Sunflower Street, No. 6");
  }
}
