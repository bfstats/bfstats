package io.github.bfstats.logparser.xml.enums.event;

/*
<bf:event name="endGame" timestamp="1472.17">
    <bf:param type="int" name="player_id">0</bf:param>
    <bf:param type="vec3" name="player_location">598.165/41.898/556.825</bf:param>
    <bf:param type="string" name="reason">tickets</bf:param>
    <bf:param type="int" name="winner">2</bf:param>
    <bf:param type="int" name="winnerScore">212</bf:param>
    <bf:param type="int" name="loserScore">185</bf:param>
</bf:event>

OR

<bf:event name="endGame" timestamp="3648.07">
    <bf:param type="string" name="reason">timeLimit</bf:param>
    <bf:param type="int" name="winner">2</bf:param>
    <bf:param type="int" name="winnerScore">0</bf:param>
    <bf:param type="int" name="loserScore">0</bf:param>
</bf:event>
 */
public enum EndGameParams {
  player_id, player_location,
  reason, // timeLimit, scoreLimit, tickets
  winner, // 1 or 2
  winnerScore, loserScore
}
