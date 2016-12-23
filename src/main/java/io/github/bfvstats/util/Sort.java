package io.github.bfvstats.util;

import lombok.Getter;

public class Sort {
  public enum SortOrder {
    ASC, DESC
  }

  @Getter
  private String property;

  @Getter
  private SortOrder order;

  public Sort(String property, SortOrder order) {
    this.property = property;
    this.order = order;
  }

  org.jooq.SortOrder getJooqSortOrder() {
    if (this.order == SortOrder.ASC) {
      return org.jooq.SortOrder.ASC;
    } else if (this.order == SortOrder.DESC) {
      return org.jooq.SortOrder.DESC;
    }
    return null;
  }
}