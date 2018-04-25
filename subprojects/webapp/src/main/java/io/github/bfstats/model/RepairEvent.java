package io.github.bfstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class RepairEvent {
  private Integer playerId;
  private String playerName;
  private Integer playerTeam;
  private Location startLocation;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private Integer durationSeconds;

  @Nullable
  private Integer usedRepairPoints;

  private String vehicleType;
}

