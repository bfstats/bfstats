package io.github.bfvstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class MapEvent {
  private Location location;
  private LocalDateTime time;
  private Integer killerPlayerId;
  private Integer playerId;
  private Weapon killWeapon;
  private String playerName;
  private String killerPlayerName;
}
