package io.github.bfvstats.logparser;

import io.github.bfvstats.logparser.xml.BfEvent;
import io.github.bfvstats.logparser.xml.BfLog;
import io.github.bfvstats.logparser.xml.BfRound;
import io.github.bfvstats.logparser.xml.enums.EventName;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class XmlParser {
  public static void main(String[] args) throws JAXBException, FileNotFoundException {
    JAXBContext context = JAXBContext.newInstance(BfLog.class);
    Unmarshaller um = context.createUnmarshaller();
    //String fileName = "D:\\Projects\\bfvstats\\example.xml";
    String fileName = "D:\\Projects\\bfvstats\\ev_15567-20161224_0825.xml";
    BfLog bfLog = (BfLog) um.unmarshal(new FileReader(fileName));

    Map<Integer, List<String[]>> killLocationsByPlayers = new HashMap<>();
    for (BfRound bfRound : bfLog.getRounds()) {
      for (BfEvent bfEvent : bfRound.getEvents()) {
        if (bfEvent.getName().equals(EventName.scoreEvent.name())) {
          if (bfEvent.getIntegerParamValueByName("victim_id") != null) {
            String map = bfRound.getMap();
            Integer playerId = bfEvent.getPlayerId();
            if (playerId > 60) {
              continue; // skip bots
            }
            String[] playerLocation = bfEvent.getPlayerLocation();
            List<String[]> listOfCoordinates = killLocationsByPlayers.computeIfAbsent(playerId, k -> new ArrayList<>());
            listOfCoordinates.add(playerLocation);
          }
        }
      }
    }

    killLocationsByPlayers.forEach((playerId, listOfCoordinates) -> {
      System.out.println();
      System.out.println("Player " + playerId);
      for (String[] coordinates : listOfCoordinates) {
        String x = coordinates[0];
        String y = coordinates[1];
        String z = coordinates[2];

        System.out.println("(" + x + ", " + y + ", " + z + ")");
        System.out.println("(" + Float.parseFloat(x) + ", " + Float.parseFloat(y) + ")");
        System.out.println("--");
      }
    });

    System.out.println("000");
    Set<String> scoreTypes = bfLog.getRounds().stream()
        .flatMap(r -> r.getEvents().stream())
        .filter(e -> e.isEvent(EventName.scoreEvent))
        .map(e -> e.getStringParamValueByName("score_type"))
        .collect(Collectors.toSet());

    System.out.println(scoreTypes);
  }



  /*<bf:event name="scoreEvent" timestamp="2550.24">
    <bf:param type="int" name="player_id">215</bf:param>
    <bf:param type="vec3" name="player_location">553.227/50.7121/329.704</bf:param>

    <bf:param type="string" name="score_type">Kill</bf:param>
    <bf:param type="int" name="victim_id">248</bf:param>
    <bf:param type="string" name="weapon">(none)</bf:param>
</bf:event>
<bf:event name="scoreEvent" timestamp="2550.24">
    <bf:param type="int" name="player_id">248</bf:param>
    <bf:param type="vec3" name="player_location">489.993/50.0385/323.769</bf:param>

    <bf:param type="string" name="score_type">DeathNoMsg</bf:param>
    <bf:param type="string" name="weapon">(none)</bf:param>
</bf:event>*/


}
