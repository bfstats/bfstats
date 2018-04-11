package io.github.bfstats.logparser.xml;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

// similar to BfStatParam, but with type
@XmlRootElement(name = "param")
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
  private List<Object> mixedContent = new ArrayList<>(1);

  @XmlTransient
  private String value; // "T54", "1", "498.28/56.171/406.15"

  // specially named method afterUnmarshal is called by JAXB
  @SuppressWarnings("unused")
  void afterUnmarshal(Unmarshaller u, Object parent) {
    this.value = BfNonPrint.mixedContentToString(mixedContent);
  }

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
        if ("(unknown)".equals(value)) {
          return null;
        }

        return value.split("/", 3);
    }

    return null;
  }

  public String toString() {
    return name + "=" + value;
  }
}
