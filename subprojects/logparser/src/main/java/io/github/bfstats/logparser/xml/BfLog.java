package io.github.bfstats.logparser.xml;

import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.Optional.ofNullable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "log")
@Getter
@ToString(of = {"engine", "timestamp"})
public class BfLog {

  // bfvietnam
  public static final String NAMESPACE = "http://www.dice.se/xmlns/bf/1.0";

  // bf1942: <bf:log version="1.1" xmlns:bf="http://www.dice.se/xmlns/bf/1.1">
  public static final String NAMESPACE_11 = "http://www.dice.se/xmlns/bf/1.1";

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
    return ofNullable(engine).orElse("bf1942");
  }

  public BfRound getFirstRound() {
    return getRootEventsAndRounds().stream()
        .filter(child -> child instanceof BfRound)
        .map(child -> (BfRound) child)
        .findFirst().orElseThrow(() -> new IllegalStateException("log file does not contain any rounds " + getTimestampAsDate()));
  }

  public boolean isTimestampMissing() {
    return getTimestamp() == null;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
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
