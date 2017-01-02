package io.github.bfvstats.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collection;

@Setter
@Getter
@Accessors(chain = true)
public class MapStatsInfo {
  private String mapName;
  private String mapFileName;
  private Integer mapSize;

  private Collection<Location> killLocations;
  private Collection<Location> deathLocations;
}
