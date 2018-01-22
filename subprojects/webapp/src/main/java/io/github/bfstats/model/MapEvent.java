package io.github.bfstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class MapEvent {
  private Location location;
  private Location relatedLocation;
  private LocalDateTime time;
  private Integer killerPlayerId;
  private String killerPlayerName;
  private Integer killerPlayerTeam;
  private Integer playerId;
  private Weapon killWeapon;
  private String playerName;
  private Integer playerTeam;
  private String killType;

  public Double getDistance() {
    if (getRelatedLocation() == null) {
      return null;
    }

    return getLocation().distance(getRelatedLocation());
  }
}
