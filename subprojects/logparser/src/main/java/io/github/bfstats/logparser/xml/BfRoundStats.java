package io.github.bfstats.logparser.xml;

import io.github.bfstats.logparser.xml.enums.Team;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static io.github.bfstats.logparser.xml.Helpers.toDuration;

@XmlRootElement(name = "roundstats")
@ToString(of = {"timestamp", "winningTeam", "victoryType", "teamTicketses"})
@Getter
public class BfRoundStats {
  @XmlAttribute(name = "timestamp", required = true)
  private String timestamp; // 3977.77

  public Duration getDurationSinceLogStart() {
    return toDuration(timestamp);
  }

  @XmlElement(name = "winningteam")
  private int winningTeam; // 1 or 2

  @XmlElement(name = "victorytype")
  private int victoryType; // 1 or ...?

  @XmlElement(name = "teamtickets")
  public List<BfTeamTickets> teamTicketses;

  // initializing with non-null, because jaxb would otherwise keep it null if there are no events
  @XmlElement(name = "playerstat")
  public List<BfPlayerStat> playerStats = new ArrayList<>();

  public Integer getTicketsForTeam(Team team) {
    return getTicketsForTeam(team.value());
  }

  public Integer getTicketsForTeam(int team) {
    return teamTicketses.stream()
        .filter(tt -> tt.getTeam() == team)
        .findFirst()
        .map(BfTeamTickets::getValue)
        .orElse(null);
  }

  public int getTicketsForTeam1() {
    return getTicketsForTeam(1);
  }

  public int getTicketsForTeam2() {
    return getTicketsForTeam(2);
  }

  @Getter
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
    private Set<BfStatParam> statParams;

    @XmlTransient
    private Map<String, String> parameters;

    public int getPlayerSlotId() {
      return getPlayerId();
    }

    public String getPlayerName() {
      return getParameters().get("player_name");
    }

    public boolean isAi() {
      return getParameters().get("is_ai").equals("1");
    }

    // 1 for NVA, 2 for USA
    public int getTeam() {
      return Integer.valueOf(getParameters().get("team"));
    }

    public int getScore() {
      return Integer.valueOf(getParameters().get("score"));
    }

    public int getKills() {
      return Integer.valueOf(getParameters().get("kills"));
    }

    public int getDeaths() {
      return Integer.valueOf(getParameters().get("deaths"));
    }

    public int getTks() {
      return Integer.valueOf(getParameters().get("tks"));
    }

    public int getCaptures() {
      return Integer.valueOf(getParameters().get("captures"));
    }

    public int getAttacks() {
      return Integer.valueOf(getParameters().get("attacks"));
    }

    public int getDefences() {
      return Integer.valueOf(getParameters().get("defences"));
    }

    // specially named method afterUnmarshal is called by JAXB
    @SuppressWarnings("unused")
    void afterUnmarshal(Unmarshaller u, Object parent) {
      this.parameters = getStatParams().stream()
          .collect(Collectors.toMap(BfStatParam::getName, BfStatParam::getValue));
    }
  }

  @Getter
  @EqualsAndHashCode(of = {"name"})
  public static class BfStatParam {
    @XmlAttribute(name = "name", required = true)
    private String name;

    // can also contain just bf:nonprint children
    @XmlElementRef(name = "root", type = BfNonPrint.class)
    @XmlMixed
    private List<Object> mixedContent = new ArrayList<>(1);

    public String getValue() {
      return BfNonPrint.mixedContentToString(mixedContent);
    }

    public String toString() {
      return name + "=" + getValue();
    }
  }
}
