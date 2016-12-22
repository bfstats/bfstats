package io.github.bfvstats.controller;

import io.github.bfvstats.Contact;
import io.github.bfvstats.service.ContactService;
import ro.pippo.controller.Controller;
import ro.pippo.core.Param;

import javax.inject.Inject;
import java.util.List;

public class ContactController extends Controller {

  private ContactService contactService;

  @Inject
  public ContactController(ContactService contactService) {
    this.contactService = contactService;
  }

  public void list() {
    List<Contact> contacts = contactService.getContacts();
    getResponse().bind("contacts", contacts).render("contact/list");
  }

  public void details(@Param("id") int id) {
    Contact contact = contactService.getContact(id);
    getResponse().bind("contact", contact).render("contact/details");
  }
}
