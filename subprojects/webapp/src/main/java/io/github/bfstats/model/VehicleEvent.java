package io.github.bfstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class VehicleEvent {
  private Integer playerId;
  private String playerName;
  private Integer playerTeam;
  private Location startLocation;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String vehicleCode;
  private String vehicleName;
}
