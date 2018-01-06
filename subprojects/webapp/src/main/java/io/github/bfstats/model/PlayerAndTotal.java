package io.github.bfstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PlayerAndTotal {
  int playerId;
  String playerName;
  int total;
  private float percentage;
}
