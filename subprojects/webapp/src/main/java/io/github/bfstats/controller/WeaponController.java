package io.github.bfstats.controller;

import io.github.bfstats.model.WeaponUsage;
import io.github.bfstats.service.WeaponService;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.Path;
import ro.pippo.controller.extractor.Param;

import javax.inject.Inject;
import java.util.List;

@Path("/weapons")
public class WeaponController extends Controller {
  private final WeaponService weaponService;

  @Inject
  public WeaponController(WeaponService weaponService) {
    this.weaponService = weaponService;
  }

  @GET("/?")
  public void list() {
    List<WeaponUsage> weapons = weaponService.getWeaponUsages();
    getResponse().bind("weapons", weapons).render("weapons/list");
  }

  @GET("/{gameCode}/{code}")
  public void details(@Param("gameCode") String gameCode, @Param("code") String weaponCode) {
    WeaponUsage weapon = weaponService.getWeapon(gameCode, weaponCode);

    getResponse()
        .bind("weapon", weapon)
        .render("weapons/details");
  }
}
