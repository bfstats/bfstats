package io.github.bfvstats.controller;

import io.github.bfvstats.model.KitUsage;
import io.github.bfvstats.service.KitService;
import ro.pippo.controller.Controller;
import ro.pippo.core.Param;

import javax.inject.Inject;

public class KitController extends Controller {
  private final KitService kitService;

  @Inject
  public KitController(KitService kitService) {
    this.kitService = kitService;
  }

  public void details(@Param("code") String kitCode) {
    KitUsage kit = kitService.getKit(kitCode);

    getResponse()
        .bind("kit", kit)
        .render("kits/details");
  }
}
