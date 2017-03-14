package io.github.bfvstats.pebble;

import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.extension.escaper.SafeString;
import com.mitchellbosecke.pebble.template.EvaluationContext;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DurationFilter implements Filter {
  private final List<String> argumentNames = new ArrayList<>();

  public DurationFilter() {
    argumentNames.add("format");
  }

  @Override
  public List<String> getArgumentNames() {
    return null;
  }

  @Override
  public Object apply(Object input, Map<String, Object> args) {
    if (input == null) {
      return null;
    }

    EvaluationContext context = (EvaluationContext) args.get("_context");
    Locale locale = context.getLocale();

    if (input instanceof Long) {
      Long durationInSeconds = (Long) input;
      long hours = durationInSeconds / 3600;
      long minutes = (durationInSeconds % 3600) / 60;

      String toFormat = (String) args.get("format");
      if (toFormat == null) {
        // literals are escaped with single quote
        toFormat = "H'h' mm'm'";
      }

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(toFormat, locale);

      String formatted = LocalTime.MIN.plusSeconds(durationInSeconds).format(formatter);

      String durationHoursAndMinutes = hours + "h " + minutes + "m";
      return new SafeString(durationHoursAndMinutes);
    }

    return null;
  }
}
