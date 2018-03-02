package io.github.bfstats.controller;

import io.github.bfstats.service.PlayerService;
import io.github.bfstats.util.DateTimeUtils;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.Path;
import ro.pippo.controller.Produces;
import ro.pippo.controller.extractor.Param;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/graphs")
public class GraphController extends Controller {
  private PlayerService playerService;

  @Inject
  public GraphController(PlayerService playerService) {
    this.playerService = playerService;
  }

  @GET("/?")
  public void graphs() {
    getResponse()
        .bind("playersOnlineUrlPath", "graphs/json/online")
        .render("graphs/all");
  }


  @GET("json/online")
  @Produces(Produces.JSON)
  public void onlinePlayersJson(@Nullable @Param("period") String period) {
    if (period != null) {
      // not implemented yet, will default to last month
    }

    LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
    Map<LocalDate, Integer> uniquePlayerCountPerDay = playerService.fetchUniquePlayerCountPerDay(lastMonth);
    String uniquePlayersCountForHighCharts = asJsonDates(uniquePlayerCountPerDay);
    getRouteContext().json().send(uniquePlayersCountForHighCharts);
  }

  private static String asJsonDates(Map<LocalDate, Integer> playersOnline) {
    return playersOnline.entrySet().stream()
        .sorted(Comparator.comparing(Map.Entry::getKey))
        .map(localDateTimeIntegerEntry -> {
          LocalDate date = localDateTimeIntegerEntry.getKey();
          Integer concurrentOnline = localDateTimeIntegerEntry.getValue();
          String javascriptDate = toJavascriptDate(date);
          return "[" + javascriptDate + ", " + concurrentOnline + "]";
        })
        .collect(Collectors.joining(",", "[", "]"));
  }

  @Nonnull
  private static String toJavascriptDate(@Nonnull LocalDate date) {
    Instant instant = DateTimeUtils.toInstantAtUserZone(date.atStartOfDay());
    long millisSinceEpoch = instant.toEpochMilli();
    return String.valueOf(millisSinceEpoch);
  }

}
