package io.github.bfstats.controller;

import io.github.bfstats.service.PlayerService;
import io.github.bfstats.service.RankingService;
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
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/sandbox")
public class SandboxController extends Controller {

  private PlayerService playerService;

  @Inject
  public SandboxController(RankingService rankingService, PlayerService playerService) {
    this.playerService = playerService;
  }

  @GET("/?")
  public void sandbox() {
    LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
    Map<LocalDateTime, Integer> playersOnline = playerService.fetchPlayersOnlineTimes(lastMonth);
    String playersOnlineForHighCharts = asJson(playersOnline);

    getResponse()
        .bind("playersOnline", playersOnlineForHighCharts)
        .bind("playersOnlineUrlPath", "sandbox/json/online")
        .render("sandbox/sandbox");
  }

  private static String asJson(Map<LocalDateTime, Integer> playersOnline) {
    return playersOnline.entrySet().stream()
        .map(localDateTimeIntegerEntry -> {
          LocalDateTime dateTime = localDateTimeIntegerEntry.getKey();
          Integer concurrentOnline = localDateTimeIntegerEntry.getValue();
          String javascriptDate = toJavascriptDate(dateTime);
          return "[" + javascriptDate + ", " + concurrentOnline + "]";
        })
        .collect(Collectors.joining(",", "[", "]"));
  }

  @Nonnull
  private static String toJavascriptDate(@Nonnull LocalDateTime dateTime) {
    Instant instant = DateTimeUtils.toInstantAtUserZone(dateTime);
    long millisSinceEpoch = instant.toEpochMilli();
    return String.valueOf(millisSinceEpoch);
  }

  private static String toJsDateUtc(LocalDateTime dateTime) {
    int y = dateTime.getYear();
    int mo = dateTime.getMonthValue() - 1;
    int d = dateTime.getDayOfMonth();
    int h = dateTime.getHour();
    int mi = dateTime.getMinute();
    int s = dateTime.getSecond();

    String jsDateUtcParams = y + "," + mo + "," + d + "," + h + "," + mi + "," + s;
    return "Date.UTC(" + jsDateUtcParams + ")";
  }

  @GET("json/online")
  @Produces(Produces.JSON)
  public void onlinePlayersJson(@Nullable @Param("period") String period) {
    if (period != null) {
      // not implemented yet, will default to last month
    }
    LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
    Map<LocalDateTime, Integer> playersOnline = playerService.fetchPlayersOnlineTimes(lastMonth);
    String playersOnlineForHighCharts = asJson(playersOnline);

    getRouteContext().json().send(playersOnlineForHighCharts);
  }

}
