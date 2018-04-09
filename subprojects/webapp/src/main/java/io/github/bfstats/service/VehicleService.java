package io.github.bfstats.service;

import io.github.bfstats.model.VehicleUsage;
import io.github.bfstats.util.TranslationUtil;

public class VehicleService {

  public VehicleUsage getVehicle(String gameCode, String vehicleCode) {
    return new VehicleUsage()
        .setGameCode(gameCode)
        .setCode(vehicleCode)
        .setName(TranslationUtil.getVehicleName(gameCode, vehicleCode));
  }
}
