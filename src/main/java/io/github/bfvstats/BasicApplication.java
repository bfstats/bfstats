package io.github.bfvstats;

import ro.pippo.core.Application;

import java.io.File;

public class BasicApplication extends Application {

  @Override
  protected void onInit() {
    //addPublicResourceRoute();
    //addWebjarsResourceRoute();

    // send 'Hello World' as response
    GET("/", routeContext -> routeContext.send("Hello World"));

    // send a file as response
    GET("/file", routeContext -> routeContext.send(new File("build.gradle")));

    // send a json as response
    GET("/json", routeContext -> {
      Contact contact = createContact();
      routeContext.json().send(contact);
    });

    // send xml as response
    GET("/xml", routeContext -> {
      Contact contact = createContact();
      routeContext.xml().send(contact);
    });

    // send an object and negotiate the Response content-type, default to XML
    GET("/negotiate", routeContext -> {
      Contact contact = createContact();
      routeContext.xml().negotiateContentType().send(contact);
    });

    // send a template as response
    GET("/template", routeContext -> {
      routeContext.setLocal("greeting", "Hello");
      routeContext.render("hello");
    });
  }

  private Contact createContact() {
    return new Contact()
        .setId(12345)
        .setName("John")
        .setPhone("0733434435")
        .setAddress("Sunflower Street, No. 6");
  }
}