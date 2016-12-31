package io.github.bfvstats.model;

import lombok.Getter;

@Getter
public class Location {
  public Location(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  private float x;
  private float y;
  private float z;
}
