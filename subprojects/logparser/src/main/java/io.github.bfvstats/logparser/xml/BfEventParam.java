package io.github.bfvstats.logparser.xml;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

// similar to BfStatParam, but with type
@XmlRootElement(name = "param", namespace = BfLog.NAMESPACE)
@EqualsAndHashCode(of = {"name", "value"})
@Getter
public class BfEventParam {
  @XmlAttribute(name = "type", required = true)
  private String type; // "string", "int", "vec3"

  @XmlAttribute(name = "name", required = true)
  private String name; // "server name"

  // can also contain just bf:nonprint children
  @XmlElementRef(name = "root", type = BfNonPrint.class)
  @XmlMixed
  private List<Object> mixedContent;

  @XmlTransient
  private String value; // "T54", "1", "498.28/56.171/406.15"

  public Object getTypeAwareValue() {
    if (value == null) {
      return null;
    }

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

  void afterUnmarshal(Unmarshaller u, Object parent) {
    this.value = BfNonPrint.mixedContentToString(mixedContent);
  }

  public String toString() {
    return name + "=" + value;
  }
}
