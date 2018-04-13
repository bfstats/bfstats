package io.github.bfstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class KitUsage {
  private String gameCode;
  private String code;
  private String name;
  private String weapons;
  private int timesUsed;
  private float percentage;
}
