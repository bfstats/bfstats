package io.github.bfstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PlayerAchievements {
  private boolean maxPoints;
  private boolean maxScore;
  private boolean maxGoldCount;
  private boolean maxSilverCount;
  private boolean maxBronzeCount;
  private boolean maxHealsAllCount;
  private boolean maxRepairsAllCount;
  private boolean maxCaptures;

  private long achievementsCount = 0;
}
