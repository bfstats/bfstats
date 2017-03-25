package io.github.bfvstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Weapon {
  private String code;
  private String name;

  public Weapon(String code, String name) {
    this.code = code;
    this.name = name;
  }
}
