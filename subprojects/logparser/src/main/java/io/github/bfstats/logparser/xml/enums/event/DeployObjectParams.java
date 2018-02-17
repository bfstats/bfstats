package io.github.bfstats.logparser.xml.enums.event;

/*
<bf:event name="deployObject" timestamp="488.762">
    <bf:param type="int" name="player_id">1</bf:param>
    <bf:param type="vec3" name="player_location">1185.78/34.1166/1317.99</bf:param>
    <bf:param type="string" name="type">MortarUS</bf:param>
</bf:event>
 */
public enum DeployObjectParams {
  player_id, player_location, type // type is: MortarUS, MortarNVA, O_Ditch
}
