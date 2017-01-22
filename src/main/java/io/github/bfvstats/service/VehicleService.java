package io.github.bfvstats.service;

import io.github.bfvstats.model.VehicleUsage;

public class VehicleService {
  public VehicleUsage getVehicle(String vehicleCode) {
    return new VehicleUsage().setName(vehicleCode);
  }
}
