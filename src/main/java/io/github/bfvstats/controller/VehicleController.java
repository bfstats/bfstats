package io.github.bfvstats.controller;

import io.github.bfvstats.model.VehicleUsage;
import io.github.bfvstats.service.VehicleService;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.Path;
import ro.pippo.controller.extractor.Param;

import javax.inject.Inject;

@Path("/vehicles")
public class VehicleController extends Controller {
  private final VehicleService vehicleService;

  @Inject
  public VehicleController(VehicleService vehicleService) {
    this.vehicleService = vehicleService;
  }

  @GET("/{code}")
  public void details(@Param("code") String vehicleCode) {
    VehicleUsage vehicle = vehicleService.getVehicle(vehicleCode);

    getResponse()
        .bind("vehicle", vehicle)
        .render("vehicles/details");
  }
}
