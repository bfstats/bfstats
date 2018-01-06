package io.github.bfstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ChatMessage {
  private int playerId;
  private String playerName;
  private Location location;
  private String text;
  private LocalDateTime time;
  private Integer toTeam;
  private Integer playerTeam;
  private Integer roundId;
}
