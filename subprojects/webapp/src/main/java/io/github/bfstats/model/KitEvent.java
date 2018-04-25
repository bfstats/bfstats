package io.github.bfstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class KitEvent {
  private Integer playerId;
  private String playerName;
  private Integer playerTeam;
  private Location location;
  private LocalDateTime time;
  private String kitCode;
  private String kitName;
  private String kitWeapons;
}
