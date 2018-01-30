package io.github.bfstats.controller;

import io.github.bfstats.service.PlayerService;
import io.github.bfstats.service.RankingService;
import io.github.bfstats.util.DateTimeUtils;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.POST;
import ro.pippo.controller.Path;
import ro.pippo.controller.extractor.Param;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
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


  @GET("/timezone")
  @POST("/timezone")
  public void updateTimezone(@Nullable @Param("zone") String zone) {
    if (zone != null) {
      ZoneId zoneId = ZoneId.of(zone);
      DateTimeUtils.setUserZone(zoneId);
    }

    sandbox();
  }

}
