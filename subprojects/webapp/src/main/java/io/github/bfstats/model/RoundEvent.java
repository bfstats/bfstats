package io.github.bfstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.Optional.ofNullable;

@Data
@Accessors(chain = true)
public class RoundEvent {
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

  public Integer getKillerOrDeathTeamId() {
    return getKillerPlayerTeam() != null ? getKillerPlayerTeam() : getPlayerTeam();
  }

  public Integer getKillerOrDeathPlayerId() {
    return getKillerPlayerId() != null ? getKillerPlayerId() : getPlayerId();
  }

  public String getKillerOrDeathPlayerName() {
    return getKillerPlayerName() != null ? getKillerPlayerName() : getPlayerName();
  }

  public String getMessage() {
    String killWeaponName = ofNullable(getKillWeapon()).map(Weapon::getName).orElse("killed");

    if (getKillerPlayerId() == null) {
      return "is no more";
    } else {
      String distance = String.format("%.0f", Math.floor(getDistance()));
      String teamKill = Objects.equals(getKillerPlayerTeam(), getPlayerTeam()) ? " TK" : "";
      return "[" + killWeaponName + "] " + getPlayerName() + " (" + distance + " meters)" + teamKill;
    }
  }
}
