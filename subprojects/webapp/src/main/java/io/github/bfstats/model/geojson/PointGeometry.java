package io.github.bfstats.model.geojson;

import lombok.Getter;

@Getter
public class PointGeometry {
  public final String type = "Point";
  public float[] coordinates;

  public PointGeometry(float[] coordinates) {
    this.coordinates = coordinates;
  }
}
