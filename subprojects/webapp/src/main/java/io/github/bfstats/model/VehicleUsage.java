package io.github.bfstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VehicleUsage {
  private String code;
  private String name;
  private int timesUsed;
  private String driveTime;
  private float percentage;
}
