package io.github.bfvstats.logparser.xml;

import io.github.bfvstats.logparser.xml.enums.EventName;
import lombok.Getter;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.bfvstats.logparser.xml.Helpers.toDuration;

@XmlRootElement(name = "event", namespace = BfLog.NAMESPACE)
@Getter
public class BfEvent {
  @XmlAttribute(name = "name", required = true)
  private String name; // createVehicle

  @XmlAttribute(name = "timestamp", required = true)
  private String timestamp; // 9.143

  public Duration getDurationSinceLogStart() {
    return toDuration(timestamp);
  }

  @XmlElement(name = "param", type = BfEventParam.class, namespace = BfLog.NAMESPACE)
  private List<BfEventParam> params;

  @XmlTransient
  private Map<String, Object> typeConvertedParameters;

  // specially named method afterUnmarshal is called by JAXB
  void afterUnmarshal(Unmarshaller u, Object parent) {
    this.typeConvertedParameters = getParams().stream()
        .collect(Collectors.toMap(BfEventParam::getName, BfEventParam::getTypeAwareValue));
  }

  public String getStringParamValueByName(String paramName) {
    return (String) getTypedParamValueByName(paramName);
  }

  public Integer getIntegerParamValueByName(String paramName) {
    return (Integer) getTypedParamValueByName(paramName);
  }

  public String[] getVector3ParamValueByName(String paramName) {
    return (String[]) getTypedParamValueByName(paramName);
  }

  public Object getTypedParamValueByName(String paramName) {
    return typeConvertedParameters.get(paramName);
  }

  public String toString() {
    return timestamp + " " + name + "(" + params + ")";
  }

  public boolean isEvent(EventName eventName) {
    return getName().equals(eventName.name());
  }

  // nullable
  public Integer getPlayerId() {
    return getIntegerParamValueByName("player_id");
  }

  // nullable
  public String[] getPlayerLocation() {
    return getVector3ParamValueByName("player_location");
  }
}