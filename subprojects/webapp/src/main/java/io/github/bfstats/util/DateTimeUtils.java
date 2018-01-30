package io.github.bfstats.util;

import ro.pippo.core.Session;
import ro.pippo.core.route.RouteContext;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static ro.pippo.core.route.RouteDispatcher.getRouteContext;

public class DateTimeUtils {

  private static ZoneId getUserZone() {
    RouteContext routeContext = getRouteContext();
    Session session = routeContext.getRequest().getSession();
    Object zoneIdStrMaybe = session.get("timezone");
    String zoneIdStr;
    if (zoneIdStrMaybe == null) {
      zoneIdStr = "GMT";
      session.put("timezone", zoneIdStr);
    } else {
      zoneIdStr = (String) zoneIdStrMaybe;
    }

    return ZoneId.of(zoneIdStr);
  }

  public static void setUserZone(ZoneId zoneId) {
    RouteContext routeContext = getRouteContext();
    Session session = routeContext.getRequest().getSession();
    session.put("timezone", zoneId.getId());
  }

  public static LocalDateTime toUserZone(@Nullable LocalDateTime localDateTimeUTC) {
    if (localDateTimeUTC == null) {
      return null;
    }
    return toZone(localDateTimeUTC, getUserZone());
  }

  public static LocalDateTime toZone(LocalDateTime localDateTimeUTC, ZoneId toZone) {
    if (localDateTimeUTC == null) {
      return null;
    }
    return localDateTimeUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(toZone).toLocalDateTime();
  }
}
