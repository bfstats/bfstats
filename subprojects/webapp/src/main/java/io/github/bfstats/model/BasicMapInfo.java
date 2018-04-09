package io.github.bfstats.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class BasicMapInfo {
  private String mapName;
  private String gameCode;
  private String mapFileName;
  private Integer mapSize;
}
