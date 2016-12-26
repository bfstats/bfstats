package io.github.bfvstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MapUsage {
  private String name;
  private Integer score;
  private float percentage;
}
