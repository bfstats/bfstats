package io.github.bfvstats.logparser.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "param", namespace = BfLog.NAMESPACE)
public class BfParam {
  @XmlAttribute(name = "type", required = true)
  private String type; // "string", "int", "vec3"

  @XmlAttribute(name = "name", required = true)
  private String name; // "server name"

  @XmlValue
  private String value; // "T54", "1", "498.28/56.171/406.15"

  @XmlTransient
  public Object getTypeAwareValue() {
    switch (type) {
      case "string":
        return value;
      case "int":
        return Integer.parseInt(value);
      case "vec3":
        String[] coordinates = value.split("/", 3);
        String coordinate1 = coordinates[0];
        String coordinate2 = coordinates[1];
        String coordinate3 = coordinates[2];
        // TODO: create special Vector3 class or Coordinates class
        return coordinates;
    }

    return null;
  }

  public String toString() {
    return name + "=" + value;
  }
}
