package io.github.bfvstats.service;

import io.github.bfvstats.model.VehicleUsage;
import io.github.bfvstats.util.TranslationUtil;

public class VehicleService {

  public VehicleUsage getVehicle(String vehicleCode) {
    return new VehicleUsage()
        .setCode(vehicleCode)
        .setName(TranslationUtil.getVehicleName(vehicleCode));
  }
}
