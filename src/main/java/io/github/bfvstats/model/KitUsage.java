package io.github.bfvstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class KitUsage {
  private String name;
  private int timesUsed;
  private float percentage;
}