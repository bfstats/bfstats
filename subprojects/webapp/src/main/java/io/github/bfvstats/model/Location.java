package io.github.bfvstats.model;

import lombok.Getter;

@Getter
public class Location {
  private float x;
  private float y;
  private float z;

  public Location(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ", " + z + '}';
  }
}
