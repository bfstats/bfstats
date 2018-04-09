package io.github.bfstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MapUsage {
  private String gameCode;
  private String code;
  private String name;
  private Integer score;
  private int timesUsed;
  private float percentage;
}
