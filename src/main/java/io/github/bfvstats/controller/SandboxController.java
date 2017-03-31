package io.github.bfvstats.controller;

import io.github.bfvstats.service.PlayerService;
import io.github.bfvstats.service.RankingService;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.Path;

import javax.inject.Inject;
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
    Map<LocalDateTime, Integer> playersOnline = playerService.fetchPlayersOnlineTimes();

    //DateTimeFormatter jsDateUtcParamsFormatter = DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm");

    String playersOnlineForHighCharts = playersOnline.entrySet().stream()
        .map(localDateTimeIntegerEntry -> {
          LocalDateTime dateTime = localDateTimeIntegerEntry.getKey();
          Integer concurrentOnline = localDateTimeIntegerEntry.getValue();

          int y = dateTime.getYear();
          int mo = dateTime.getMonthValue() - 1;
          int d = dateTime.getDayOfMonth();
          int h = dateTime.getHour();
          int mi = dateTime.getMinute();
          int s = dateTime.getSecond();

          String jsDateUtcParams = y + "," + mo + "," + d + "," + h + "," + mi + "," + s;

          //String jsDateUtcParams = dateTime.format(jsDateUtcParamsFormatter);
          return "[Date.UTC(" + jsDateUtcParams + "), " + concurrentOnline + "]";
        })
        .collect(Collectors.joining(","));

    getResponse()
        .bind("playersOnline", playersOnlineForHighCharts)
        .render("sandbox/sandbox");
  }
}
