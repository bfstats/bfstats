package io.github.bfstats.model.geojson;

import lombok.Getter;

import java.util.Collection;

@Getter
public class FeatureCollection {
  public final String type = "FeatureCollection";
  public Collection<Feature> features;

  public FeatureCollection(Collection<Feature> features) {
    this.features = features;
  }
}
