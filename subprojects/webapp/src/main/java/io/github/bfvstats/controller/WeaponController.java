package io.github.bfvstats.controller;

import io.github.bfvstats.model.WeaponUsage;
import io.github.bfvstats.service.WeaponService;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.Path;
import ro.pippo.controller.extractor.Param;

import javax.inject.Inject;

@Path("/weapons")
public class WeaponController extends Controller {
  private final WeaponService weaponService;

  @Inject
  public WeaponController(WeaponService weaponService) {
    this.weaponService = weaponService;
  }

  @GET("/{code}")
  public void details(@Param("code") String weaponCode) {
    WeaponUsage weapon = weaponService.getWeapon(weaponCode);

    getResponse()
        .bind("weapon", weapon)
        .render("weapons/details");
  }
}
