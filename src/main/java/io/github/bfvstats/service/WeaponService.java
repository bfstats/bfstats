package io.github.bfvstats.service;

import com.google.common.collect.ImmutableMap;
import io.github.bfvstats.model.WeaponUsage;

import javax.annotation.Nullable;
import java.util.Map;

public class WeaponService {
  private static Map<String, String> weaponNameByCode = ImmutableMap.<String, String>builder()
      .put("44Magnum", "M-19")
      .put("Ak47", "AK-47")
      .put("AkMS", "AKMS")
      .put("AVMine", "Land Mine")
      .put("Binoculars", "Binoculars")
      .put("BouncingBetty", "Bouncing Betty")
      .put("Btorch", "Blow Torch")
      .put("C4", "C4")
      .put("Caltrops", "Caltrops")
      .put("Car15", "CAR-15")
      .put("Claymore", "Claymore")
      .put("CommandoKnife", "Knife")
      .put("CommandoKnifeThrow", "Knife")
      .put("DPM", "Type-53")
      .put("ExpPack", "TNT")
      .put("Flaregun", "Mod Flaregun")
      .put("GrenadeAllies", "Mk 2 Grenade")
      .put("GrenadeAxis", "Type 67 Stick Grenade")
      .put("KnifeAllies", "Knife")
      .put("KnifeAxis", "Machete")
      .put("Landmine", "Landmine")
      .put("LAW", "L.A.W.")
      .put("M14", "M14")
      .put("M16", "M16")
      .put("M16S", "M16 with Scope")
      .put("M19", "M1911")
      .put("M91", "M91/30")
      .put("Mat49", "MAT-49")
      .put("Mberg500", "Mossberg 500")
      .put("NVAbinoculars", "Binoculars")
      .put("Plantationknife", "Plantation Knife")
      .put("Pungistick", "Pungi Stick")
      .put("RPD", "RPD")
      .put("RPG2", "RPG-2")
      .put("RPG7V", "RPG-7V")
      .put("Sa7", "SA-7")
      .put("SKS56", "Type-56")
      .put("Timebomb", "Time Bomb")
      .put("tt33", "TT-33")
      .put("USLandmine", "Landmine")
      .put("Wirecutters", "Booby Trap")
      .put("XM148", "CAR15/XM148")
      .put("XMGrenadelauncher", "CAR15/XM148")
      .put("M60", "M60")
      .put("M79", "M79")
      .put("M21", "M21")
      .put("M40", "M40")
      .build();

  @Nullable
  public static String weaponNameStrict(String weaponCode) {
    return weaponNameByCode.get(weaponCode);
  }

  public static String weaponName(String weaponCode) {
    return weaponNameByCode.getOrDefault(weaponCode, weaponCode);
  }

  public WeaponUsage getWeapon(String weaponCode) {
    return new WeaponUsage().setName(weaponCode);
  }
}
