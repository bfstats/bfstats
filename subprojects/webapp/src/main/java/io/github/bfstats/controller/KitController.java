package io.github.bfstats.controller;

import io.github.bfstats.model.KitUsage;
import io.github.bfstats.service.KitService;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.Path;
import ro.pippo.controller.extractor.Param;

import javax.inject.Inject;

@Path("/kits")
public class KitController extends Controller {
  private final KitService kitService;

  @Inject
  public KitController(KitService kitService) {
    this.kitService = kitService;
  }

  @GET("/{gameCode}/{code}")
  public void details(@Param("gameCode") String gameCode, @Param("code") String kitCode) {
    KitUsage kit = kitService.getKit(gameCode, kitCode);

    getResponse()
        .bind("kit", kit)
        .render("kits/details");
  }
}
