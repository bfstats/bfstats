package io.github.bfstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class MedPackEvent {
  private Integer playerId;
  private String playerName;
  private Integer playerTeam;
  private String playerTeamCode;
  private Location startLocation;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private Integer durationSeconds;

  @Nullable
  private Integer usedMedPackPoints;

  private Integer healedPlayerId;
  private String healedPlayerName;
}
