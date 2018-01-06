package io.github.bfstats.model;

import io.github.bfstats.model.geojson.FeatureCollection;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class MapEvents {
  private String mapName;
  private String mapFileName;
  private Integer mapSize;

  private FeatureCollection killFeatureCollection;
  private FeatureCollection deathFeatureCollection;
}
