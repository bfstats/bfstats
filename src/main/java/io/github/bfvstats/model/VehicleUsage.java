package io.github.bfvstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VehicleUsage {
  private String name;
  private int timesUsed;
  private String driveTime;
  private float percentage;
}
