package io.github.bfstats.controller;

import io.github.bfstats.util.DateTimeUtils;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.POST;
import ro.pippo.controller.Path;
import ro.pippo.controller.extractor.Param;

import javax.annotation.Nullable;
import java.time.ZoneId;

@Path("/settings")
public class SettingsController extends Controller {

  @GET("/timezone")
  @POST("/timezone")
  public void timezone(@Nullable @Param("zone") String zone) {
    if (zone != null) {
      ZoneId zoneId = ZoneId.of(zone);
      DateTimeUtils.setUserZone(zoneId);
    }

    getResponse()
        .render("settings/timezone");
  }
}
