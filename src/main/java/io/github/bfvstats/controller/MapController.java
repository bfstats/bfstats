package io.github.bfvstats.controller;

import io.github.bfvstats.model.BasicMapInfo;
import io.github.bfvstats.model.MapEvents;
import io.github.bfvstats.model.MapUsage;
import io.github.bfvstats.service.MapService;
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
    BasicMapInfo basicMapInfo = mapService.getBasicMapInfo(mapCode);

    getResponse()
        .bind("map", basicMapInfo)
        .bind("mapEventsUrlPath", "maps/json/" + mapCode + "/events")
        .render("maps/details");
  }

  @GET("json/{mapCode}/events")
  @Produces(Produces.JSON)
  public void mapEventsJson(@Param("mapCode") String mapCode) {
    MapEvents mapEvents = mapService.getMapEvents(mapCode, null, null);
    getRouteContext().json().send(mapEvents);
  }
}
