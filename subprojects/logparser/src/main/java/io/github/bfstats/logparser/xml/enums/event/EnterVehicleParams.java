package io.github.bfstats.logparser.xml.enums.event;

/*
  <bf:event name="enterVehicle" timestamp="28.6371">
    <bf:param type="int" name="player_id">254</bf:param>
    <bf:param type="vec3" name="player_location">380.544/47.4348/296.217</bf:param>
    <bf:param type="string" name="vehicle">Patton</bf:param>
    <bf:param type="int" name="pco_id">0</bf:param>
    <bf:param type="int" name="is_default">0</bf:param>
  </bf:event>
 */
public enum EnterVehicleParams {
  player_id, player_location, vehicle,
  pco_id, // PCO = Player Controllable Object
  is_default
}
