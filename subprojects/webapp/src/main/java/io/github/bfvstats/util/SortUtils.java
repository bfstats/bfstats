package io.github.bfvstats.util;

import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.DSL;
import ro.pippo.core.ParameterValue;
import ro.pippo.core.Request;

import java.util.Arrays;

public class SortUtils {
  public static int getPageFromRequest(Request request) {
    String page = Arrays.stream(request.getQueryParameter("page").getValues())
        .findFirst()
        .orElse("1");
    return Integer.valueOf(page);
  }

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

  public static org.jooq.SortOrder getJooqSortOrder(Sort.SortOrder sortOrder) {
    if (sortOrder == Sort.SortOrder.ASC) {
      return org.jooq.SortOrder.ASC;
    } else if (sortOrder == Sort.SortOrder.DESC) {
      return org.jooq.SortOrder.DESC;
    }
    return null;
  }

  // if string type column, then converts it to lower(), so that sorting is case insensitive
  public static Field<?> getSortableField(String columnName) {
    Field<?> field = DSL.field(columnName);
    if (field.getType().equals(String.class)) {
      Field<String> fieldTypedString = field.cast(String.class);
      field = DSL.lower(fieldTypedString);
    }
    return field;
  }

  // if string type column, then converts it to lower(), so that sorting is case insensitive
  public static Field<?> getSortableField(Table table, String columnName) {
    Field<?> field = table.field(columnName);
    if (field == null) {
      throw new IllegalArgumentException("Bad sorting property");
    }
    if (field.getType().equals(String.class)) {
      Field<String> fieldTypedString = field.cast(String.class);
      field = DSL.lower(fieldTypedString);
    }
    return field;
  }
}
