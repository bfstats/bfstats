package io.github.bfstats.model;

import lombok.Getter;

import javax.annotation.Nonnull;

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

  public double distance(@Nonnull Location other) {
    float x2 = other.getX();
    float y2 = other.getY();
    float z2 = other.getZ();

    return Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2) + Math.pow(z2 - z, 2));
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ", " + z + '}';
  }
}
