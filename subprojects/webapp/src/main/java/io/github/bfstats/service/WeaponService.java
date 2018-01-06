package io.github.bfstats.service;

import io.github.bfstats.model.WeaponUsage;
import io.github.bfstats.util.TranslationUtil;

public class WeaponService {
  public WeaponUsage getWeapon(String weaponCode) {
    return new WeaponUsage()
        .setCode(weaponCode)
        .setName(TranslationUtil.getWeaponName(weaponCode));
  }
}
