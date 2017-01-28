package io.github.bfvstats.service;

import com.google.common.collect.ImmutableMap;
import io.github.bfvstats.model.VehicleUsage;

import java.util.Map;

public class VehicleService {
  private static Map<String, String> vehicleNameByCode = ImmutableMap.<String, String>builder()
      .put("Chinook", "ACH-47")
      .put("Cobra", "Cobra")
      .put("Corsair", "A-7 Corsair")
      .put("F4Phantom", "F-4 Phantom")
      .put("Mi8", "Mi-8")
      .put("Mi8cargo", "Mi-8 Cargo")
      .put("Mig17", "MiG-17")
      .put("Mig21", "MiG-21")
      .put("UH1Assault", "Huey Gunship")
      .put("UH1Transport", "Huey Slick")
      .put("BM21sam", "BM-21")
      .put("BTR60", "BTR-60")
      .put("Logtrap", "Logtrap")
      .put("M110", "M-110")
      .put("M113", "M-113")
      .put("M46", "M-46")
      .put("MortarNVA", "Type 63 Mortar")
      .put("MortarUS", "M1 Mortar")
      .put("Mutt", "M.U.T.T.")
      .put("O_ditch", "Tunnel Entrance")
      .put("Patton", "Patton")
      .put("PT76", "PT-76")
      .put("Sheridan", "Sheridan")
      .put("T54", "T-54")
      .put("UAZ", "UAZ-469")
      .put("USMobilespawn01", "Air Cav Crate")
      .put("USMobilespawn02", "Air Cav Crate")
      .put("Vespa", "Scooter")
      .put("ZSU", "ZSU")
      .put("PBR", "P.B.R.")
      .put("Sampan", "Sampan")
      .put("Tango", "Tango")
      .build();

  public static String vehicleName(String vehicleCode) {
    return vehicleNameByCode.getOrDefault(vehicleCode, vehicleCode);
  }

  public VehicleUsage getVehicle(String vehicleCode) {
    return new VehicleUsage().setName(vehicleCode);
  }
}
