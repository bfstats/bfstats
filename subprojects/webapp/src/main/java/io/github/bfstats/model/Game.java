package io.github.bfstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Game {
  private int id;

  private String mapCode;
  private String mapName;
  private String modeName;
  private LocalDateTime startTime;

  @Nullable
  private ServerSettings serverSettings;
}
