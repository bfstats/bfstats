package io.github.bfstats.model.geojson;

import java.util.Collection;

public class FeatureCollection {
  public final String type = "FeatureCollection";
  public Collection<Feature> features;

  public FeatureCollection(Collection<Feature> features) {
    this.features = features;
  }
}
