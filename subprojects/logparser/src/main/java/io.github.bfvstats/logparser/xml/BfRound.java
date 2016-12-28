package io.github.bfvstats.logparser.xml;

import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.bfvstats.logparser.xml.Helpers.toDuration;

@ToString(of = {"timestamp"})
@XmlRootElement(name = "round", namespace = BfLog.NAMESPACE)
@Getter
public class BfRound {
  @XmlAttribute(name = "timestamp", required = true)
  private String timestamp; // 9.143

  public Duration getDurationSinceLogStart() {
    return toDuration(timestamp);
  }

  @XmlElementWrapper(name = "server", namespace = BfLog.NAMESPACE)
  @XmlElement(name = "setting", namespace = BfLog.NAMESPACE)
  private List<BfSetting> settings;

  @XmlTransient
  private Map<String, String> settingsMap;

  @XmlElement(name = "event", namespace = BfLog.NAMESPACE)
  private List<BfEvent> events;

  @XmlElement(name = "roundstats", namespace = BfLog.NAMESPACE)
  private BfRoundStats roundStats;

  // specially named method afterUnmarshal is called by JAXB
  void afterUnmarshal(Unmarshaller u, Object parent) {
    this.settingsMap = getSettings().stream()
        .collect(Collectors.toMap(BfSetting::getName, BfSetting::getValue));
  }

  public String getSettingValue(String settingName) {
    return settingsMap.get(settingName);
  }

  public Integer getIntegerSettingValue(String settingName) {
    String settingValue = getSettingValue(settingName);
    if (settingValue == null) {
      return null;
    }
    return Integer.valueOf(settingValue);
  }
}