package io.github.bfvstats.service;

import com.google.common.collect.ImmutableMap;
import io.github.bfvstats.model.WeaponUsage;

import java.util.Map;

public class WeaponService {
  private static Map<String, String> weaponNameByCode = ImmutableMap.<String, String>builder()
      .put("Car15", "CAR-15")
      .put("XMGrenadelauncher", "XM148 grenade launcher")
      .build();

  public static String weaponName(String weaponCode) {
    return weaponNameByCode.getOrDefault(weaponCode, weaponCode);
  }

  public WeaponUsage getWeapon(String weaponCode) {
    return new WeaponUsage().setName(weaponCode);
  }
}
