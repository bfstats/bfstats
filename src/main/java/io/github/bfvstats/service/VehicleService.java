package io.github.bfvstats.service;

import com.google.common.collect.ImmutableMap;
import io.github.bfvstats.model.VehicleUsage;

import java.util.Map;

public class VehicleService {
  private static Map<String, String> vehicleNameByCode = ImmutableMap.<String, String>builder()
      .put("Mi8", "Mi-8")
      .put("PT76", "PT-76")
      .put("F4Phantom", "F-4 Phantom")
      .build();

  public static String vehicleName(String vehicleCode) {
    return vehicleNameByCode.getOrDefault(vehicleCode, vehicleCode);
  }

  public VehicleUsage getVehicle(String vehicleCode) {
    return new VehicleUsage().setName(vehicleCode);
  }
}
