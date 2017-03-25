package io.github.bfvstats.service;

import io.github.bfvstats.model.WeaponUsage;
import io.github.bfvstats.util.TranslationUtil;

public class WeaponService {
  public WeaponUsage getWeapon(String weaponCode) {
    return new WeaponUsage()
        .setCode(weaponCode)
        .setName(TranslationUtil.getWeaponName(weaponCode));
  }
}
