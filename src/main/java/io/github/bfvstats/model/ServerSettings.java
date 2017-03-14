package io.github.bfvstats.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ServerSettings {
  private String serverName;
  private int serverPort;
  private String modId;
  private String gameMode;
  private int maxGameTime;
  private int maxPlayers;
  private int scoreLimit;
  private int numberOfRoundsPerMap;
  private int spawnTime;
  private int spawnDelay;
  private int gameStartDelay;
  private int roundStartDelay;
  private int soldierFriendlyFire;
  private int vehicleFriendlyFire;
  private int ticketRatio;
  private boolean teamKillPunish;
  private boolean punkbusterEnabled;


  /*

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
