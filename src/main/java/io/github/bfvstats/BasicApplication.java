package io.github.bfvstats;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.github.bfvstats.controller.ChatController;
import io.github.bfvstats.controller.PlayerController;
import io.github.bfvstats.controller.RankingController;
import io.github.bfvstats.controller.RoundController;
import io.github.bfvstats.util.DbUtils;
import ro.pippo.controller.ControllerApplication;
import ro.pippo.guice.GuiceControllerFactory;

public class BasicApplication extends ControllerApplication {

  @Override
  protected void onInit() {
    // create guice injector
    Injector injector = Guice.createInjector(new InjectionModule());

    // registering GuiceControllerFactory
    setControllerFactory(new GuiceControllerFactory(injector));

    addPublicResourceRoute();
    //addWebjarsResourceRoute();

    // send 'Hello World' as response
    GET("/", routeContext -> routeContext.send("Hello World"));
    GET("/players/json", PlayerController.class, "jsonRandom");
    GET("/players(/?)", PlayerController.class, "list");
    GET("/players/{id}", PlayerController.class, "details");
    GET("/players/{id}/map/{mapCode}", PlayerController.class, "mapStats");

    GET("/ranking(/?)", RankingController.class, "ranking");

    GET("/chat(/?)", ChatController.class, "list");

    GET("/rounds(/?)", RoundController.class, "list");
    GET("/rounds/{id}", RoundController.class, "details");

    closeDbConnections();
  }

  private void closeDbConnections() {
    ALL("/.*", (routeContext) -> DbUtils.closeDslContext())
        .runAsFinally();
  }

}