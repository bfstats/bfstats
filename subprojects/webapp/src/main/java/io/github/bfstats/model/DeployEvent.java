package io.github.bfstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class DeployEvent {
  private Integer playerId;
  private String playerName;
  private Integer playerTeam;
  private String playerTeamCode;
  private Location location;
  private LocalDateTime time;
  private String objectCode;
  private String objectName;
}

