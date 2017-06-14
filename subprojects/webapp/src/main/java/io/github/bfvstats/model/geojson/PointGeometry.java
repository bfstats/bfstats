package io.github.bfvstats.model.geojson;

public class PointGeometry {
  public final String type = "Point";
  public float[] coordinates;

  public PointGeometry(float[] coordinates) {
    this.coordinates = coordinates;
  }
}
