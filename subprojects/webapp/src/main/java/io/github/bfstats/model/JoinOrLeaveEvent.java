package io.github.bfstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class JoinOrLeaveEvent {
  private Integer playerId;
  private String playerName;
  private LocalDateTime time;
  private String type; // join or leave
}
