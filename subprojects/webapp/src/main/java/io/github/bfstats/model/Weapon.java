package io.github.bfstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Weapon {
  private String gameCode;
  private String code;
  private String name;

  public Weapon(String gameCode, String code, String name) {
    this.gameCode = gameCode;
    this.code = code;
    this.name = name;
  }
}
