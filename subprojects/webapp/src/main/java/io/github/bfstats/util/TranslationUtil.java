package io.github.bfstats.util;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

import static io.github.bfstats.util.Utils.loadPropertiesFileFromResources;

@Slf4j
public class TranslationUtil {
  private static Map<String, Map<String, String>> vehicleNameByCodeByGameCode = loadTranslations("vehicles.properties");
  private static Map<String, Map<String, String>> mapNameByMapCodeByGameCode = loadTranslations("maps.properties");
  private static Map<String, Map<String, String>> weaponNameByCodeByGameCode = loadTranslations("weapons.properties");
  private static Map<String, Map<String, String>> modeNameByCodeByGameCode = loadTranslations("gamemodes.properties");

  private static Map<String, Map<String, String>> loadTranslations(String filename) {
    return ImmutableMap.<String, Map<String, String>>builder()
        .put("bf1942", loadPropertiesFileFromResources("translations/bf1942/" + filename))
        .put("bfvietnam", loadPropertiesFileFromResources("translations/bfvietnam/" + filename))
        .build();
  }

  @Nonnull
  public static String getMapName(@Nonnull String gameCode, @Nonnull String mapCode) {
    return mapNameByMapCodeByGameCode.get(gameCode).getOrDefault(mapCode, mapCode);
  }

  @Nonnull
  public static String getMapName(@Nonnull String mapCode) {
    return getMapName("bfvietnam", mapCode);
  }

  @Nonnull
  public static String getModeName(@Nonnull String gameCode, @Nonnull String modeCode) {
    return modeNameByCodeByGameCode.get(gameCode).getOrDefault(modeCode, modeCode);
  }

  @Nonnull
  public static String getModeName(@Nonnull String modeCode) {
    return getModeName("bfvietnam", modeCode);
  }

  @Nonnull
  public static String getVehicleName(@Nonnull String gameCode, @Nonnull String vehicleCode) {
    return vehicleNameByCodeByGameCode.get(gameCode).getOrDefault(vehicleCode, vehicleCode);
  }

  @Nonnull
  public static String getVehicleName(@Nonnull String vehicleCode) {
    return getVehicleName("bfvietnam", vehicleCode);
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
  public static String getWeaponName(@Nonnull String gameCode, @Nonnull String weaponCode) {
    return weaponNameByCodeByGameCode.get(gameCode).getOrDefault(weaponCode, weaponCode);
  }

  @Nonnull
  public static String getWeaponName(@Nonnull String weaponCode) {
    return getWeaponName("bfvietnam", weaponCode);
  }

  @Nullable
  public static String getWeaponNameStrict(@Nonnull String gameCode, String weaponCode) {
    return weaponNameByCodeByGameCode.get(gameCode).get(weaponCode);
  }

  @Nonnull
  public static String getWeaponOrVehicleName(@Nonnull String gameCode, @Nonnull String weaponOrVehicleCode) {
    String weaponName = TranslationUtil.getWeaponNameStrict(gameCode, weaponOrVehicleCode);
    if (weaponName == null) {
      weaponName = TranslationUtil.getVehicleName(gameCode, weaponOrVehicleCode);
    }
    return weaponName;
  }

  @Nonnull
  public static String getWeaponOrVehicleName(@Nonnull String weaponOrVehicleCode) {
    return getWeaponOrVehicleName("bfvietnam", weaponOrVehicleCode);
  }
}
