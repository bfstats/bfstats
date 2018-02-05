package io.github.bfstats;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.loader.DelegatingLoader;
import com.mitchellbosecke.pebble.loader.FileLoader;
import com.mitchellbosecke.pebble.loader.Loader;
import io.github.bfstats.exceptions.NotFoundException;
import io.github.bfstats.pebble.DurationExtension;
import io.github.bfstats.pebble.Java8Extension;
import io.github.bfstats.util.DbUtils;
import ro.pippo.controller.ControllerApplication;
import ro.pippo.core.Application;
import ro.pippo.core.PippoConstants;
import ro.pippo.core.RequestResponseFactory;
import ro.pippo.guice.GuiceControllerFactory;
import ro.pippo.pebble.PebbleTemplateEngine;
import ro.pippo.session.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BfStatsApplication extends ControllerApplication {

  public static class EnhancedPebbleTemplateEngine extends PebbleTemplateEngine {
    @Override
    protected void init(Application application, PebbleEngine.Builder builder) {
      builder.extension(new Java8Extension());
      builder.extension(new DurationExtension());

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

    addControllers("io.github.bfstats.controller");
    closeDbConnections();

    // register a custom ExceptionHandler
    getErrorHandler().setExceptionHandler(NotFoundException.class, (e, routeContext) -> {
      routeContext.setLocal("message", e.getMessage());
      // render the template associated with this http status code ("pippo/403forbidden" by default)
      getErrorHandler().handle(404, routeContext);
    });
  }

  @Override
  protected RequestResponseFactory createRequestResponseFactory() {
    CookieSessionStrategy cookieSessionStrategy1 = new CookieSessionStrategy() {
      @Override
      public void onNewSession(HttpServletRequest request, HttpServletResponse response, SessionData sessionData) {
        String sessionId = sessionData.getId();
        Cookie cookie = createSessionIdCookie(request, sessionId);
        response.addCookie(cookie);
      }

      @Override
      public void onInvalidatedSession(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = createSessionIdCookie(request, "");
        response.addCookie(cookie);
      }

      private Cookie createSessionIdCookie(HttpServletRequest request, String sessionId) {
        Cookie cookie = new Cookie("SESSIONID", sessionId);
        cookie.setHttpOnly(true);
        cookie.setSecure(request.isSecure());
        cookie.setPath(request.getContextPath() + "/");
        cookie.setMaxAge(3600 * 24 * 30);

        return cookie;
      }

    };

    MemorySessionDataStorage memorySessionDataStorage = new MemorySessionDataStorage();
    SessionManager sessionManager = new SessionManager(memorySessionDataStorage, cookieSessionStrategy1);
    return new SessionRequestResponseFactory(this, sessionManager);
  }

  private void closeDbConnections() {
    ANY("/.*", routeContext -> DbUtils.closeDslContext())
        .runAsFinally();
  }
}
