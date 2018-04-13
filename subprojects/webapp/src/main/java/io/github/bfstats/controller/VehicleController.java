package io.github.bfstats.controller;

import io.github.bfstats.model.VehicleUsage;
import io.github.bfstats.service.VehicleService;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.Path;
import ro.pippo.controller.extractor.Param;

import javax.inject.Inject;
import java.util.List;

@Path("/vehicles")
public class VehicleController extends Controller {
  private final VehicleService vehicleService;

  @Inject
  public VehicleController(VehicleService vehicleService) {
    this.vehicleService = vehicleService;
  }

  @GET("/?")
  public void list() {
    List<VehicleUsage> vehicles = vehicleService.getVehicleUsages();
    getResponse().bind("vehicles", vehicles).render("vehicles/list");
  }

  @GET("/{gameCode}/{code}")
  public void details(@Param("gameCode") String gameCode, @Param("code") String vehicleCode) {
    VehicleUsage vehicle = vehicleService.getVehicle(gameCode, vehicleCode);

    getResponse()
        .bind("vehicle", vehicle)
        .render("vehicles/details");
  }
}
