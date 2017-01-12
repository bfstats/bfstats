package io.github.bfvstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PlayerStats {
  int playerId;
  int rank;
  String name;
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
  int roundsPlayed;
  int heals;
  int otherRepairs;

  int selfHeals;
  int repairs;
  double scorePerMinute;
}