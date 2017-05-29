package io.github.bfvstats.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class TranslationUtil {
  private static Map<String, String> vehicleNameByCode;
  private static Map<String, String> mapNameByMapCode;
  private static Map<String, String> weaponNameByCode;
  private static Map<String, String> modeNameByCode;

  static {
    mapNameByMapCode = loadPropertiesFileFromResources("translations/maps.properties");
    vehicleNameByCode = loadPropertiesFileFromResources("translations/vehicles.properties");
    weaponNameByCode = loadPropertiesFileFromResources("translations/weapons.properties");

    modeNameByCode = ImmutableMap.<String, String>builder()
        .put("GPM_COOP", "CO-OP")
        .put("GPM_CQ", "Conquest")
        .build();
  }

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

  private static Map<String, String> loadPropertiesFileFromResources(String filePath) {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    Properties props = new Properties();
    try (InputStream resourceStream = loader.getResourceAsStream(filePath)) {
      props.load(resourceStream);
    } catch (IOException e) {
      log.warn("Could not load properties for path " + filePath, e);
    }
    return Maps.fromProperties(props);
  }
}
