package io.github.bfvstats;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.github.bfvstats.controller.PlayerController;
import ro.pippo.controller.ControllerApplication;
import ro.pippo.guice.GuiceControllerFactory;

import java.io.File;

public class BasicApplication extends ControllerApplication {

  @Override
  protected void onInit() {
    // create guice injector
    Injector injector = Guice.createInjector(new InjectionModule());

    // registering GuiceControllerFactory
    setControllerFactory(new GuiceControllerFactory(injector));

    addPublicResourceRoute();
    //addWebjarsResourceRoute();

    GET("/players(/?)", PlayerController.class, "list");
    GET("/players/{id}", PlayerController.class, "details");

    // send 'Hello World' as response
    GET("/", routeContext -> routeContext.send("Hello World"));

    // send a file as response
    GET("/file", routeContext -> routeContext.send(new File("build.gradle")));

    // send a json as response
    GET("/json", routeContext -> {
      Player player = createPlayer();
      routeContext.json().send(player);
    });

    // send xml as response
    GET("/xml", routeContext -> {
      Player player = createPlayer();
      routeContext.xml().send(player);
    });

    // send an object and negotiate the Response content-type, default to XML
    GET("/negotiate", routeContext -> {
      Player player = createPlayer();
      routeContext.xml().negotiateContentType().send(player);
    });

    // send a template as response
    GET("/template", routeContext -> {
      routeContext.setLocal("greeting", "Hello");
      routeContext.render("hello");
    });
  }

  private Player createPlayer() {
    return new Player()
        .setId(12345)
        .setName("John")
        .setKeyHash("Sunflower Street, No. 6");
  }
}