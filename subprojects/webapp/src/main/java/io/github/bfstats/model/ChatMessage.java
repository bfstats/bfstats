package io.github.bfstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ChatMessage {
  private int playerId;
  private String playerName;
  private Location location;
  private String text;
  private LocalDateTime time;
  @Nullable
  private Integer targetTeamId;
  @Nullable
  private String targetTeamCode;
  @Nullable
  private String targetTeamName;
  private Integer playerTeam;
  private String playerTeamCode;
  private Integer roundId;
}
