package io.github.bfstats.logparser.xml.enums.event;

/*
<bf:event name="radioMessage" timestamp="546.133">
    <bf:param type="int" name="player_id">0</bf:param>
    <bf:param type="vec3" name="player_location">507.409/62.8483/1137.45</bf:param>
    <bf:param type="int" name="message">33</bf:param>
    <bf:param type="int" name="broadcast">1</bf:param>
</bf:event>

 */
public enum RadioMessageParams {
  player_id, player_location, message, broadcast
}
