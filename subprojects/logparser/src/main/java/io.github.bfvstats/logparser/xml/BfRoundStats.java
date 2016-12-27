package io.github.bfvstats.logparser.xml;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.util.List;
import java.util.Set;

@XmlRootElement(name = "roundstats", namespace = BfLog.NAMESPACE)
@ToString(of = {"timestamp", "winningTeam", "victoryType", "teamTicketses"})
@Getter
public class BfRoundStats {
  @XmlAttribute(name = "timestamp", required = true)
  private String timestamp; // 3977.77

  @XmlElement(name = "winningteam")
  private int winningTeam; // 1 or 2

  @XmlElement(name = "victorytype")
  private int victoryType; // 1 or ...?

  @XmlElement(name = "teamtickets")
  public List<BfTeamTickets> teamTicketses;

  @XmlElement(name = "playerstat")
  public List<BfPlayerStat> playerStats;

  private static class BfTeamTickets {
    @XmlAttribute(name = "team", required = true)
    private int team;

    @XmlValue
    private int value;

    public String toString() {
      return team + "=" + value;
    }
  }

  @Getter
  @ToString(of = {"playerId", "statParams"})
  public static class BfPlayerStat {
    @XmlAttribute(name = "playerid", required = true)
    private int playerId;
    /*
    <bf:statparam name="player_name">
      <bf:nonprint>205</bf:nonprint><bf:nonprint>194</bf:nonprint>
      <bf:nonprint>200</bf:nonprint><bf:nonprint>210</bf:nonprint>
      <bf:nonprint>210</bf:nonprint>
    </bf:statparam>
    <bf:statparam name="is_ai">0</bf:statparam>
    <bf:statparam name="team">2</bf:statparam>
    <bf:statparam name="score">0</bf:statparam>
    <bf:statparam name="kills">0</bf:statparam>
    <bf:statparam name="deaths">0</bf:statparam>
    <bf:statparam name="tks">0</bf:statparam>
    <bf:statparam name="captures">0</bf:statparam>
    <bf:statparam name="attacks">0</bf:statparam>
    <bf:statparam name="defences">0</bf:statparam>
     */
    @XmlElement(name = "statparam")
    public Set<BfStatParam> statParams;
  }

  @Getter
  @EqualsAndHashCode(of = {"name", "value"})
  public static class BfStatParam {
    @XmlAttribute(name = "name", required = true)
    private String name;

    // can also contain just bf:nonprint children
    @XmlValue
    private String value;

    public String toString() {
      return name + "=" + value;
    }
  }

}

