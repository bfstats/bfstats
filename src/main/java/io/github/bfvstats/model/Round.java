package io.github.bfvstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Round {
  private Integer id;
  private String mapCode;
  private LocalDateTime startTime;
}
