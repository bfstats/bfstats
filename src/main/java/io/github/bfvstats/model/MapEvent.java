package io.github.bfvstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MapEvent {
  private Location location;
  private Integer killerPlayerId;
  private Integer playerId;
  private Weapon killWeapon;

}
