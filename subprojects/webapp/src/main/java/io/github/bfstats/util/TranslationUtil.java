package io.github.bfstats.util;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

import static io.github.bfstats.util.Utils.loadPropertiesFileFromResources;

@Slf4j
public class TranslationUtil {
  private static Map<String, String> vehicleNameByCode =
      loadPropertiesFileFromResources("translations/vehicles.properties");

  private static Map<String, String> mapNameByMapCode =
      loadPropertiesFileFromResources("translations/maps.properties");

  private static Map<String, String> weaponNameByCode =
      loadPropertiesFileFromResources("translations/weapons.properties");

  private static Map<String, String> modeNameByCode =
      loadPropertiesFileFromResources("translations/gamemodes.properties");

  @Nonnull
  public static String getMapName(@Nonnull String mapCode) {
    return mapNameByMapCode.getOrDefault(mapCode, mapCode);
  }

  @Nonnull
  public static String getModeName(@Nonnull String modeCode) {
    return modeNameByCode.getOrDefault(modeCode, modeCode);
  }

  @Nonnull
  public static String getVehicleName(@Nonnull String vehicleCode) {
    return vehicleNameByCode.getOrDefault(vehicleCode, vehicleCode);
  }

  @Nonnull
  public static String getVehicleModifier(@Nonnull String vehicleModifierCode) {
    switch (vehicleModifierCode) {
      case "CoPilot":
        return "co-pilot";
      case "Gunner":
        return "gunner";
    }
    return vehicleModifierCode;
  }

  @Nonnull
  public static String getWeaponName(@Nonnull String weaponCode) {
    return weaponNameByCode.getOrDefault(weaponCode, weaponCode);
  }

  @Nullable
  public static String getWeaponNameStrict(String weaponCode) {
    return weaponNameByCode.get(weaponCode);
  }

  @Nonnull
  public static String getWeaponOrVehicleName(@Nonnull String weaponOrVehicleCode) {
    String weaponName = TranslationUtil.getWeaponNameStrict(weaponOrVehicleCode);
    if (weaponName == null) {
      weaponName = TranslationUtil.getVehicleName(weaponOrVehicleCode);
    }
    return weaponName;
  }
}
