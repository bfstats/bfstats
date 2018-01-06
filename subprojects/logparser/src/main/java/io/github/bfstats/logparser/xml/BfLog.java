package io.github.bfstats.logparser.xml;

import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "log")
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

  @XmlMixed
  @XmlElementRefs({
      @XmlElementRef(name = "event", type = BfEvent.class),
      @XmlElementRef(name = "round", type = BfRound.class)
  })
  private List<Object> rootEventsAndRounds;

  public String getEngine() {
    return engine;
  }
}


/*
        gamePaused event (no parameters)
        gameUnpaused event (no parameters)
        sayAll event (text parameter)
        ip parameter to existing connectPlayer event
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
