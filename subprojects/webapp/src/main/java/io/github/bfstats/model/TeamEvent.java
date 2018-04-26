package io.github.bfstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class TeamEvent {
  private Integer playerId;
  private String playerName;
  private Integer playerTeam;
  private String playerTeamCode;
  private String playerTeamName;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
}

