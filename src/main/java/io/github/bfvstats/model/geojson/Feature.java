package io.github.bfvstats.model.geojson;

import java.util.Map;

public class Feature {
  public final String type = "Feature";
  public PointGeometry geometry;
  public Map<String, Object> properties;
}