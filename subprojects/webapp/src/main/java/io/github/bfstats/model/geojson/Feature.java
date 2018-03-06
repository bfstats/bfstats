package io.github.bfstats.model.geojson;

import lombok.Getter;

import javax.annotation.Nullable;
import java.util.Map;

@Getter
public class Feature {
  public final String type = "Feature";
  public PointGeometry geometry;
  @Nullable
  public Map<String, Object> properties;
}
