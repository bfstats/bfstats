package io.github.bfvstats.logparser.xml.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlEnum(Integer.class)
public enum Team {
  /**
   * NVA
   */
  @XmlEnumValue("1")
  TEAM_1(1),

  /**
   * USA
   */
  @XmlEnumValue("2")
  TEAM_2(2);

  private final int value;

  Team(int v) {
    value = v;
  }

  public int value() {
    return value;
  }

  public static Team fromValue(int teamId) {
    for (Team c : Team.values()) {
      if (c.value == teamId) {
        return c;
      }
    }
    throw new IllegalArgumentException("Unknown team id: " + teamId);
  }

}
