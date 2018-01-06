package io.github.bfstats.pebble;

import com.mitchellbosecke.pebble.extension.Filter;
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

      String toFormat = (String) args.get("format");
      if (toFormat == null) {
        // literals are escaped with single quote
        toFormat = "H'h' mm'm'";
      }

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(toFormat, locale);

      String formatted = LocalTime.MIN.plusSeconds(durationInSeconds).format(formatter);
      return formatted;
    }

    return null;
  }
}
