package io.github.bfstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PlayerStats {
  int playerId;
  int rank;
  String name;
  String partialKeyHash;
  int points;
  int averageScore;
  int score;
  int kills;
  int deaths;
  Double killDeathRatio;
  int goldCount;
  int silverCount;
  int bronzeCount;
  int teamKills;
  int captures;
  int defences;
  int roundsPlayed;
  int heals;
  int selfHeals;
  int otherHeals;

  int repairs;
  int selfRepairs;
  int otherRepairs;
  int unmannedRepairs;

  double scorePerMinute;
}
