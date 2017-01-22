package io.github.bfvstats.service;

import io.github.bfvstats.model.WeaponUsage;

public class WeaponService {
  public WeaponUsage getWeapon(String weaponCode) {
    return new WeaponUsage().setName(weaponCode);
  }
}
