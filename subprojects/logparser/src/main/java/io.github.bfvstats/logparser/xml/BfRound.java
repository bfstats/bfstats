package io.github.bfvstats.logparser.xml;

import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@ToString(of = {"timestamp"})
@XmlRootElement(name = "round", namespace = BfLog.NAMESPACE)
@Getter
public class BfRound {
  @XmlAttribute(name = "timestamp", required = true)
  private String timestamp; // 9.143

  @XmlElementWrapper(name = "server", namespace = BfLog.NAMESPACE)
  @XmlElement(name = "setting", namespace = BfLog.NAMESPACE)
  private List<BfSetting> settings;

  @XmlElement(name = "event", namespace = BfLog.NAMESPACE)
  private List<BfEvent> events;

  @XmlElement(name = "roundstats", namespace = BfLog.NAMESPACE)
  private BfRoundStats roundStats;
}
