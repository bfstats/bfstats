package io.github.bfstats.logparser.xml;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "setting")
@Getter
public class BfSetting {
  @XmlAttribute(name = "name", required = true)
  private String name; // "server name"

  @XmlValue
  private String value; // "Vietnam Europe CO-OP", xml value, not attr

  public String toString() {
    return name + "=" + value;
  }
}
