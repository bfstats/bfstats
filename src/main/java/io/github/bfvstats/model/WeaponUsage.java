package io.github.bfvstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WeaponUsage {
  private String name;
  private int timesUsed;
  private float percentage;
}