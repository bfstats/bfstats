package io.github.bfvstats.controller;

import io.github.bfvstats.model.MapStatsInfo;
import io.github.bfvstats.model.MapUsage;
import io.github.bfvstats.service.MapService;
import ro.pippo.controller.Controller;
import ro.pippo.core.Param;

import javax.inject.Inject;
import java.util.List;

public class MapController extends Controller {

  private MapService mapService;

  @Inject
  public MapController(MapService mapService) {
    this.mapService = mapService;
  }

  public void list() {
    List<MapUsage> mapUsagesForPlayer = mapService.getMapUsages();

    getResponse().bind("maps", mapUsagesForPlayer).render("maps/list");
  }

  public void details(@Param("mapCode") String mapCode) {
    MapStatsInfo mapStatsInfo = mapService.getMapStatsInfoForPlayer(mapCode, null, null);

    getResponse()
        .bind("map", mapStatsInfo)
        .render("maps/details");
  }
}
