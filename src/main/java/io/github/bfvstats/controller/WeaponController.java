package io.github.bfvstats.controller;

import io.github.bfvstats.model.WeaponUsage;
import io.github.bfvstats.service.WeaponService;
import ro.pippo.controller.Controller;
import ro.pippo.core.Param;

import javax.inject.Inject;

public class WeaponController extends Controller {
  private final WeaponService weaponService;

  @Inject
  public WeaponController(WeaponService weaponService) {
    this.weaponService = weaponService;
  }

  public void details(@Param("code") String weaponCode) {
    WeaponUsage weapon = weaponService.getWeapon(weaponCode);

    getResponse()
        .bind("weapon", weapon)
        .render("weapons/details");
  }
}
