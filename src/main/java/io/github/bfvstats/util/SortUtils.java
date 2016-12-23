package io.github.bfvstats.util;

import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.impl.DSL;
import ro.pippo.core.ParameterValue;
import ro.pippo.core.Request;

import java.util.Arrays;

public class SortUtils {

  // ?sort=column,order (order defaults to ASC)
  public static Sort getSortColumnAndOrderFromRequest(Request request) {
    ParameterValue sort = request.getQueryParameter("sort");
    return Arrays.stream(sort.getValues()).findFirst()
        .map(sortColumnAndOrderStr -> {
          String[] sortColumnAndOrder = sortColumnAndOrderStr.split(",");
          String sortColumn = sortColumnAndOrder[0];

          Sort.SortOrder sortOrder = Sort.SortOrder.ASC;
          if (sortColumnAndOrder.length == 2) {
            String sortOrderStr = sortColumnAndOrder[1];
            if (sortOrderStr.equals("ASC") || sortOrderStr.equals("DESC")) {
              sortOrder = Sort.SortOrder.valueOf(sortOrderStr);
            }
          }
          return new Sort(sortColumn, sortOrder);
        }).orElse(null);
  }

  public static SortField<?> getSortField(Table table, Sort sort) {
    return getSortableField(table, sort.getProperty())
        .sort(getJooqSortOrder(sort.getOrder()));
  }

  private static org.jooq.SortOrder getJooqSortOrder(Sort.SortOrder sortOrder) {
    if (sortOrder == Sort.SortOrder.ASC) {
      return org.jooq.SortOrder.ASC;
    } else if (sortOrder == Sort.SortOrder.DESC) {
      return org.jooq.SortOrder.DESC;
    }
    return null;
  }

  // if string type column, then converts it to lower(), so that sorting is case insensitive
  private static Field<?> getSortableField(Table table, String columnName) {
    Field<?> field = table.field(columnName);
    if (field.getType().equals(String.class)) {
      Field<String> fieldTypedString = field.cast(String.class);
      field = DSL.lower(fieldTypedString);
    }
    return field;
  }
}
