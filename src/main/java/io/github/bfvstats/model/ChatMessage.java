package io.github.bfvstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Accessors(chain = true)
public class ChatMessage {
  private int playerId;
  private String playerName;
  private String text;
  private LocalDateTime time;
  private Integer toTeam;
  private Integer playerTeam;
}
