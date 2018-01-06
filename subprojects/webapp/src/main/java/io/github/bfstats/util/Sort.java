package io.github.bfstats.util;

import lombok.Getter;
import lombok.ToString;

@ToString
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
