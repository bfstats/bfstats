package io.github.bfstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WeaponUsage {
  private String gameCode;
  private String code;
  private String name;
  private int timesUsed;
  private float percentage;
}
