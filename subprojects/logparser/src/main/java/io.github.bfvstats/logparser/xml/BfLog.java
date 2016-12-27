package io.github.bfvstats.logparser.xml;

import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "log", namespace = BfLog.NAMESPACE)
@Getter
@ToString(of = {"engine", "timestamp"})
public class BfLog {
  public static final String NAMESPACE = "http://www.dice.se/xmlns/bf/1.0";

  @XmlAttribute(name = "engine", required = true)
  private String engine; // BFVietnam v1.21

  @XmlAttribute(name = "timestamp", required = true)
  private String timestamp; // 20161224_0825 // YYYYMMDD_HHMM

  public LocalDateTime getTimestampAsDate() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
    return LocalDateTime.parse(timestamp, formatter);
  }

  @XmlElement(name = "event", namespace = BfLog.NAMESPACE)
  private List<BfEvent> events;

  @XmlElement(name = "round", namespace = BfLog.NAMESPACE)
  private List<BfRound> rounds;

  public String getEngine() {
    return engine;
  }
}


/*
        gamePaused event (no parameters)
        gameUnpaused event (no parameters)
        sayAll event (text parameter)
        ip parameter to existing connectPlayer event
        timestamp parameter in bf:log tag (see below)
        endGame event
            reason = timelimit, scoreLimit, tickets
            winner = 1|2
            winnerScore
            loserScore
        deployObject/undeployObject event
            type
        attachToHook/detachFromHook event
            cargo
        createVehicle event
            type
            team
 */