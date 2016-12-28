package io.github.bfvstats.logparser.xml;

import java.time.Duration;

public class Helpers {

  public static Duration toDuration(String secondsAndMs) {
    // seconds to milliseconds
    // 94.8923 = 94892.3
    // 94.892  = 94892
    // 94.89   = 94890

    // max digits after the dot is 4 based on my observations, so there can still be .x even in milliseconds
    // so converting to nanoseconds instead

    String[] secondsAndMilliseconds = secondsAndMs.split("\\.");
    String secondsStr = secondsAndMilliseconds[0];
    int seconds = Integer.parseInt(secondsStr);

    String millisecondsStr = secondsAndMilliseconds.length == 2 ? secondsAndMilliseconds[1] : "";
    // pad up to 6 zeros (max seems to be 4 in logs)
    String microsecondsStr = String.format("%-6s", millisecondsStr).replace(' ', '0');
    int microseconds = Integer.parseInt(microsecondsStr);
    long nanoseconds = microseconds * 1000;

    return Duration.ofSeconds(seconds, nanoseconds);
  }
}
