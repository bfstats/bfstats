package io.github.bfstats.pebble;

import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Filter;

import java.util.HashMap;
import java.util.Map;

public class Java8Extension extends AbstractExtension {

  @Override
  public Map<String, Filter> getFilters() {
    Map<String, Filter> filters = new HashMap<>();
    filters.put("dt", new Java8DateFilter());
    return filters;
  }
}
