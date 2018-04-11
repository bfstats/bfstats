package io.github.bfstats.logparser.xml;

import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.bfstats.logparser.xml.Helpers.toDuration;
import static java.util.Optional.ofNullable;

@XmlRootElement(name = "round")
@Getter
@ToString(of = {"timestamp"})
public class BfRound {
  @XmlAttribute(name = "timestamp", required = true)
  private String timestamp; // 9.143

  public Duration getDurationSinceLogStart() {
    return toDuration(timestamp);
  }

  @XmlElementWrapper(name = "server")
  @XmlElement(name = "setting")
  private List<BfSetting> settings = new ArrayList<>(40);

  @XmlTransient
  private Map<String, String> settingsMap;

  @XmlElement(name = "event")
  // initializing with non-null, because jaxb would otherwise keep it null if there are no events
  private List<BfEvent> events = new ArrayList<>();

  @XmlElement(name = "roundstats")
  private BfRoundStats roundStats;

  // specially named method afterUnmarshal is called by JAXB
  @SuppressWarnings("unused")
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

  public Float getFloatSettingValue(String settingName) {
    String settingValue = getSettingValue(settingName);
    if (settingValue == null) {
      return null;
    }
    return Float.valueOf(settingValue);
  }

  public Boolean getBooleanSettingValue(String settingName) {
    return ofNullable(getIntegerSettingValue(settingName))
        .map(integerValue -> integerValue.equals(1))
        .orElse(null);
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

  public int getSoldierFriendlyFireOnSplash() {
    return getIntegerSettingValue("soldierffonsplash");
  }

  public int getVehicleFriendlyFire() {
    return getIntegerSettingValue("vehicleff");
  }

  public int getVehicleFriendlyFireOnSplash() {
    return getIntegerSettingValue("vehicleffonsplash");
  }

  public int getFriendlyFireKickback() {
    // for some reason kickback is kept as fractional 0..1, which is different from other similar values
    float kickback = getFloatSettingValue("kickback") * 100;
    return Math.round(kickback);
  }

  public int getFriendlyFireKickbackOnSplash() {
    // for some reason kickbacksplash is kept as fractional 0..1, which is different from other similar values
    float kickbacksplash = getFloatSettingValue("kickbacksplash") * 100;
    return Math.round(kickbacksplash);
  }

  public int getTicketRatio() {
    return getIntegerSettingValue("ticketratio");
  }

  public boolean isTeamKillPunished() {
    return getBooleanSettingValue("tkpunish");
  }

  public boolean isPunkBusterEnabled() {
    return getBooleanSettingValue("sv_punkbuster");
  }

  public boolean isAutoBalanceEnabled() {
    return getBooleanSettingValue("autobalance");
  }

  public int getTagDistance() {
    return getIntegerSettingValue("tagdistance");
  }

  public int getTagDistanceScope() {
    return getIntegerSettingValue("tagdistancescope");
  }

  public boolean isNoseCameraAllowed() {
    return getBooleanSettingValue("allownosecam");
  }

  public boolean isFreeCameraAllowed() {
    return getBooleanSettingValue("freecamera");
  }

  public boolean isExternalViewsAllowed() {
    return getBooleanSettingValue("externalviews");
  }

  public boolean isHitIndicationEnabled() {
    return getBooleanSettingValue("hitindication");
  }

  /**
   * If false, then the server is accessible only in LAN
   */
  public boolean isInternet() {
    return getBooleanSettingValue("internet");
  }

  public Integer getCoopCpu() {
    return getIntegerSettingValue("coopcpu");
  }

  public Integer getCoopSkill() {
    return getIntegerSettingValue("coopskill");
  }

  public Integer getAlliedPlayerCountRatio() {
    return getIntegerSettingValue("alliedtr");
  }

  public Integer getAxisPlayerCountRatio() {
    return getIntegerSettingValue("axistr");
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



  bf1942 also has these:
  <bf:setting name="dedicated">1</bf:setting>
  <bf:setting name="crosshairpoint">1</bf:setting>
  <bf:setting name="deathcamtype">0</bf:setting>
  <bf:setting name="contentcheck">0</bf:setting>

   */
}
