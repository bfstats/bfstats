package io.github.bfstats;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.github.bfstats.exceptions.NotFoundException;
import io.github.bfstats.util.DateTimeUtils;
import io.github.bfstats.util.DbUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import ro.pippo.controller.ControllerApplication;
import ro.pippo.core.RequestResponseFactory;
import ro.pippo.guice.GuiceControllerFactory;
import ro.pippo.session.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class BfStatsApplication extends ControllerApplication {

  @Override
  protected void onInit() {
    // create guice injector
    Injector injector = Guice.createInjector(new InjectionModule());

    // registering GuiceControllerFactory
    setControllerFactory(new GuiceControllerFactory(injector));

    setTemplateEngine(new EnhancedPebbleTemplateEngine());

    addPublicResourceRoute();

    //addWebjarsResourceRoute();

    List<ZoneId> timezones = DateTimeUtils.getTimezones();
    Instant instant = Instant.now();
    List<SelectOption> timezoneOptions = timezones.stream()
        .map(zoneId -> {
          ZoneOffset zoneOffset = zoneId.getRules().getStandardOffset(instant);
          String offset = zoneOffset.getTotalSeconds() == 0 ? " 00:00" : zoneOffset.getId();
          return new SelectOption(zoneId.getId(), "(" + offset + ") " + zoneId.getId());
        })
        .collect(toList());

    getRoutePreDispatchListeners().add((request, response) ->
        response.getLocals().put("timezones", timezoneOptions)
    );

    addControllers("io.github.bfstats.controller");
    closeDbConnections();

    // register a custom ExceptionHandler
    getErrorHandler().setExceptionHandler(NotFoundException.class, (e, routeContext) -> {
      routeContext.setLocal("message", e.getMessage());
      // render the template associated with this http status code
      getErrorHandler().handle(404, routeContext);
    });
  }

  @Data
  @AllArgsConstructor
  public static class SelectOption {
    private String value;
    private String label;
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
