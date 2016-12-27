package io.github.bfvstats.logparser.xml;

import lombok.Getter;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@XmlRootElement(name = "event", namespace = BfLog.NAMESPACE)
@Getter
public class BfEvent {
  @XmlAttribute(name = "name", required = true)
  private String name; // createVehicle

  @XmlAttribute(name = "timestamp", required = true)
  private String timestamp; // 9.143

  @XmlElement(name = "param", type = BfParam.class, namespace = BfLog.NAMESPACE)
  private List<BfParam> params;

  @XmlTransient
  private Map<String, Object> typeConvertedParameters;

  // specially named method afterUnmarshal is called by JAXB
  void afterUnmarshal(Unmarshaller u, Object parent) {
    this.typeConvertedParameters = getParams().stream()
        .collect(Collectors.toMap(BfParam::getName, BfParam::getTypeAwareValue));
  }

  public Object getTypedParamValueByName(String paramName) {
    return typeConvertedParameters.get(paramName);
  }

  public String toString() {
    return timestamp + " " + name + "(" + params + ")";
  }
}
