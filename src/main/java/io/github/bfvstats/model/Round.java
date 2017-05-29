package io.github.bfvstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Round {
  private Integer id;
  private String mapCode;
  private String mapName;
  private String modeName;
  private LocalDateTime startTime;

  // end stats
  private LocalDateTime endTime;
  private long durationInMinutes;
  private int winningTeam;
  private int victoryType;
  private int endTicketsTeam1;
  private int endTicketsTeam2;

  @Nullable
  private ServerSettings serverSettings;
}
