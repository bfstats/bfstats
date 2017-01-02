package io.github.bfvstats.logparser.xml;

import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.time.Duration;
import java.util.ArrayList;
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
  // initializing with non-null, because jaxb would otherwise keep it null if there are no events
  private List<BfEvent> events = new ArrayList<>();

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

  public String getServerName() {
    return getSettingValue("server name");
  }

  public int getPort() {
    return getIntegerSettingValue("port");
  }

  public String getModId() {
    return getSettingValue("modid");
  }

  public String getGame() {
    return getSettingValue("mapid");
  }

  public String getMap() {
    return getSettingValue("map");
  }

  public String getGameMode() {
    return getSettingValue("game mode");
  }

  public int getMaxGameTime() {
    return getIntegerSettingValue("gametime");
  }

  public int getMaxPlayers() {
    return getIntegerSettingValue("maxplayers");
  }

  public int getScoreLimit() {
    return getIntegerSettingValue("scorelimit");
  }

  public int getNumberOfRoundsPerMap() {
    return getIntegerSettingValue("norounds");
  }

  public int getSpawnTime() {
    return getIntegerSettingValue("spawntime");
  }

  public int getSpawnDelay() {
    return getIntegerSettingValue("spawndelay");
  }

  public int getGameStartDelay() {
    return getIntegerSettingValue("gamestartdelay");
  }

  public int getRoundStartDelay() {
    return getIntegerSettingValue("roundstartdelay");
  }

  public int getSoldierFriendlyFire() {
    return getIntegerSettingValue("soldierff");
  }

  public int getVehicleFriendlyFire() {
    return getIntegerSettingValue("vehicleff");
  }

  public int getTicketRatio() {
    return getIntegerSettingValue("ticketratio");
  }

  public boolean isTeamKillPunished() {
    Integer tkpunish = getIntegerSettingValue("tkpunish");
    return tkpunish.equals(1);
  }

  public boolean isPunkBusterEnabled() {
    Integer punkbuster = getIntegerSettingValue("sv_punkbuster");
    return punkbuster.equals(1);
  }
  /*


    tkpunish = bfRound.getIntegerSettingValue("tkpunish");
    Integer deathCameraType = bfRound.getIntegerSettingValue("deathcamtype");

    <bf:setting name="server name">Vietnam Europe CO-OP</bf:setting>
  <bf:setting name="port">15567</bf:setting>
  <bf:setting name="modid">bfvietnam</bf:setting>
  <bf:setting name="mapid">BFVietnam</bf:setting>
  <bf:setting name="map">khe_sahn</bf:setting>
  <bf:setting name="game mode">GPM_COOP</bf:setting>
  <bf:setting name="gametime">35</bf:setting>
  <bf:setting name="maxplayers">60</bf:setting>
  <bf:setting name="scorelimit">0</bf:setting>
  <bf:setting name="norounds">2</bf:setting>
  <bf:setting name="spawntime">10</bf:setting>
  <bf:setting name="spawndelay">5</bf:setting>
  <bf:setting name="gamestartdelay">15</bf:setting>
  <bf:setting name="roundstartdelay">10</bf:setting>
  <bf:setting name="soldierff">100</bf:setting>
  <bf:setting name="vehicleff">100</bf:setting>
  <bf:setting name="ticketratio">100</bf:setting>

  <bf:setting name="internet">1</bf:setting>
  <bf:setting name="alliedtr">1</bf:setting>
  <bf:setting name="axistr">1</bf:setting>
  <bf:setting name="coopskill">100</bf:setting>
  <bf:setting name="coopcpu">75</bf:setting>
  <bf:setting name="allownosecam">1</bf:setting>
  <bf:setting name="freecamera">0</bf:setting>
  <bf:setting name="externalviews">1</bf:setting>
  <bf:setting name="autobalance">0</bf:setting>
  <bf:setting name="tagdistance">100</bf:setting>
  <bf:setting name="tagdistancescope">300</bf:setting>
  <bf:setting name="kickback">0</bf:setting>
  <bf:setting name="kickbacksplash">0</bf:setting>
  <bf:setting name="soldierffonsplash">100</bf:setting>
  <bf:setting name="vehicleffonsplash">100</bf:setting>
  <bf:setting name="hitindication">1</bf:setting>
  <bf:setting name="tkpunish">0</bf:setting>
  <bf:setting name="sv_punkbuster">0</bf:setting>
   */
}