package io.github.bfvstats;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.loader.DelegatingLoader;
import com.mitchellbosecke.pebble.loader.FileLoader;
import com.mitchellbosecke.pebble.loader.Loader;
import io.github.bfvstats.controller.*;
import io.github.bfvstats.pebble.Java8Extension;
import io.github.bfvstats.util.DbUtils;
import ro.pippo.controller.ControllerApplication;
import ro.pippo.core.Application;
import ro.pippo.core.PippoConstants;
import ro.pippo.guice.GuiceControllerFactory;
import ro.pippo.pebble.PebbleTemplateEngine;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BasicApplication extends ControllerApplication {

  public static class EnhancedPebbleTemplateEngine extends PebbleTemplateEngine {
    @Override
    protected void init(Application application, PebbleEngine.Builder builder) {
      builder.extension(new Java8Extension());

      FileLoader fileLoader = new FileLoader();
      fileLoader.setCharset(PippoConstants.UTF8);

      if (application.getPippoSettings().isDev()) {
        List<Loader<?>> loaders = new ArrayList<>();

        String templatesAbsolutePath = Paths.get(".").toAbsolutePath().normalize()
            .resolve("src\\main\\resources\\templates")
            .normalize()
            .toString();
        fileLoader.setPrefix(templatesAbsolutePath);
        fileLoader.setSuffix("." + "peb");
        loaders.add(fileLoader);

        ClasspathLoader classpathLoader = new ClasspathLoader();
        classpathLoader.setCharset(PippoConstants.UTF8);
        classpathLoader.setPrefix("templates/");
        classpathLoader.setSuffix(".peb");
        loaders.add(classpathLoader);

        builder.loader(new DelegatingLoader(loaders));
      }
    }
  }

  @Override
  protected void onInit() {
    // create guice injector
    Injector injector = Guice.createInjector(new InjectionModule());

    // registering GuiceControllerFactory
    setControllerFactory(new GuiceControllerFactory(injector));

    EnhancedPebbleTemplateEngine enhancedPebbleTemplateEngine = new EnhancedPebbleTemplateEngine();
    setTemplateEngine(enhancedPebbleTemplateEngine);

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

    GET("/maps(/?)", MapController.class, "list");
    GET("/maps/{mapCode}", MapController.class, "details");

    closeDbConnections();
  }

  private void closeDbConnections() {
    ALL("/.*", (routeContext) -> DbUtils.closeDslContext())
        .runAsFinally();
  }

}