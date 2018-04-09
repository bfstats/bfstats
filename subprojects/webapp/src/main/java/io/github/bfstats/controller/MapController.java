package io.github.bfstats.controller;

import io.github.bfstats.model.BasicMapInfo;
import io.github.bfstats.model.MapEvents;
import io.github.bfstats.model.MapUsage;
import io.github.bfstats.service.MapService;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.Path;
import ro.pippo.controller.Produces;
import ro.pippo.controller.extractor.Param;

import javax.inject.Inject;
import java.util.List;

@Path("/maps")
public class MapController extends Controller {

  private MapService mapService;

  @Inject
  public MapController(MapService mapService) {
    this.mapService = mapService;
  }

  @GET("/?")
  public void list() {
    List<MapUsage> mapUsagesForPlayer = mapService.getMapUsages();

    getResponse().bind("maps", mapUsagesForPlayer).render("maps/list");
  }

  @GET("/{mapCode}")
  public void details(@Param("mapCode") String mapCode) {
    BasicMapInfo basicMapInfo = mapService.getBasicMapInfo("bfvietnam", mapCode);

    getResponse()
        .bind("map", basicMapInfo)
        .bind("mapEventsUrlPath", "maps/json/" + mapCode + "/events")
        .render("maps/details");
  }

  @GET("json/{mapCode}/events")
  @Produces(Produces.JSON)
  public void mapEventsJson(@Param("mapCode") String mapCode) {
    MapEvents mapEvents = mapService.getMapEvents("bfvietnam", mapCode, null, null, false);
    getRouteContext().json().send(mapEvents);
  }
}
