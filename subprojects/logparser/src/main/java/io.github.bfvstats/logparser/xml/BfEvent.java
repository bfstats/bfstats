package io.github.bfvstats.logparser.xml;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "event", namespace = BfLog.NAMESPACE)
@Getter
public class BfEvent {
  @XmlAttribute(name = "name", required = true)
  private String name; // createVehicle

  @XmlAttribute(name = "timestamp", required = true)
  private String timestamp; // 9.143

  @XmlElement(name = "param", type = BfParam.class, namespace = BfLog.NAMESPACE)
  private List<BfParam> params;

  public String toString() {
    return timestamp + " " + name + "(" + params + ")";
  }
}
