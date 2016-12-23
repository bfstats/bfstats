package io.github.bfvstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PlayerStats {
  int id;
  int rank;
  String name;
  int points;
  int averageScore;
  int score;
  int kills;
  int deaths;
  double killDeathRatio;
  int goldCount;
  int silverCount;
  int bronzeCount;
  int teamKills;
  int attacks;
  int captures;
  int objectives;
  int roundsPlayed;
  int heals;
  int otherRepairs;

  int selfHeals;
  int repairs;
  double scorePerMinute;
}