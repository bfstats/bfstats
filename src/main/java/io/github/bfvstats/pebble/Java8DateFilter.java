package io.github.bfvstats.pebble;

import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.extension.escaper.SafeString;
import com.mitchellbosecke.pebble.template.EvaluationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Java8DateFilter implements Filter {

  private final List<String> argumentNames = new ArrayList<>();

  public Java8DateFilter() {
    argumentNames.add("format");
    argumentNames.add("existingFormat");
  }

  @Override
  public List<String> getArgumentNames() {
    return argumentNames;
  }

  @Override
  public Object apply(Object input, Map<String, Object> args) {
    if (input == null) {
      return null;
    }
    LocalDateTime dateTime;
    DateTimeFormatter existingDateTimeFormatter;
    DateTimeFormatter intendedDateTimeFormatter;

    EvaluationContext context = (EvaluationContext) args.get("_context");
    Locale locale = context.getLocale();

    intendedDateTimeFormatter = DateTimeFormatter.ofPattern((String) args.get("format"), locale);

    if (args.get("existingFormat") != null) {
      existingDateTimeFormatter = DateTimeFormatter.ofPattern((String) args.get("existingFormat"), locale);
      try {
        dateTime = LocalDateTime.parse((String) input, existingDateTimeFormatter);
      } catch (DateTimeParseException e) {
        throw new RuntimeException("Could not parse date", e);
      }
    } else {
      dateTime = (LocalDateTime) input;
    }
    return new SafeString(dateTime.format(intendedDateTimeFormatter));
  }
}
