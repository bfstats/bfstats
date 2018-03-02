package io.github.bfstats.util;

import ro.pippo.core.Session;
import ro.pippo.core.route.RouteContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.*;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static ro.pippo.core.route.RouteDispatcher.getRouteContext;

public class DateTimeUtils {

  public static final String SESSION_ATTRIBUTE_TIMEZONE = "timezone";
  public static final String DEFAULT_ZONE_ID = "GMT";

  public static List<ZoneId> getTimezones() {
    Instant instant = Instant.now();
    Comparator<ZoneId> zoneOffsetAscending = comparing(
        (ZoneId zoneId) -> zoneId.getRules().getStandardOffset(instant)
    ).reversed();

    return ZoneId.getAvailableZoneIds().stream()
        .map(ZoneId::of)
        .sorted(zoneOffsetAscending.thenComparing(ZoneId::getId))
        .collect(toList());

  }

  @Nonnull
  public static ZoneId getUserZone() {
    RouteContext routeContext = getRouteContext();
    Session session = routeContext.getRequest().getSession();
    Object zoneIdStrMaybe = session.get(SESSION_ATTRIBUTE_TIMEZONE);
    String zoneIdStr;
    if (zoneIdStrMaybe == null) {
      zoneIdStr = DEFAULT_ZONE_ID;
      session.put(SESSION_ATTRIBUTE_TIMEZONE, zoneIdStr);
    } else {
      zoneIdStr = (String) zoneIdStrMaybe;
    }

    return ZoneId.of(zoneIdStr);
  }

  public static void setUserZone(ZoneId zoneId) {
    RouteContext routeContext = getRouteContext();
    Session session = routeContext.getRequest().getSession();
    session.put(SESSION_ATTRIBUTE_TIMEZONE, zoneId.getId());
  }


  @Nullable
  public static Instant toInstantAtUserZone(@Nullable LocalDateTime localDateTimeUTC) {
    ZonedDateTime zonedDateTime = toZonedDateTimeAtUserZone(localDateTimeUTC);
    if (zonedDateTime == null) {
      return null;
    }

    return zonedDateTime.toInstant();
  }

  @Nullable
  public static ZonedDateTime toZonedDateTimeAtUserZone(@Nullable LocalDateTime localDateTimeUTC) {
    if (localDateTimeUTC == null) {
      return null;
    }

    return localDateTimeUTC.atZone(DateTimeUtils.getUserZone());
  }

  @Nullable
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
