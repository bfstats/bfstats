package io.github.bfvstats.pebble;

import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.extension.escaper.SafeString;

import java.util.List;
import java.util.Map;

public class DurationFilter implements Filter {
  @Override
  public List<String> getArgumentNames() {
    return null;
  }

  @Override
  public Object apply(Object input, Map<String, Object> args) {
    if (input == null) {
      return null;
    }

    if (input instanceof Long) {
      Long durationInSeconds = (Long) input;
      long hours = durationInSeconds / 3600;
      long minutes = durationInSeconds % 60;

      String durationHoursAndMinutes = hours + "H " + minutes + "M";
      return new SafeString(durationHoursAndMinutes);
    }

    return null;
  }
}
