package io.github.bfvstats.service;

import io.github.bfvstats.Contact;

import java.util.List;

public interface ContactService {
  List<Contact> getContacts();

  Contact getContact(int id);
}
