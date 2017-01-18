package io.github.bfvstats.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {
  public static float percentage(int a, int total) {
    if (total == 0) {
      if (a != 0) {
        log.warn("total is 0, but a is " + a);
      }
      return 0;
    }
    return a * 100f / total;
  }
}
