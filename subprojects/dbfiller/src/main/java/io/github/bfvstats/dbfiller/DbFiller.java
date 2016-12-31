package io.github.bfvstats.dbfiller;

import io.github.bfvstats.logparser.XmlParser;
import io.github.bfvstats.logparser.xml.BfEvent;
import io.github.bfvstats.logparser.xml.BfLog;
import io.github.bfvstats.logparser.xml.BfRound;
import io.github.bfvstats.logparser.xml.enums.EventName;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class DbFiller {
  public static void main(String[] args) throws JAXBException, FileNotFoundException {
    String fileName = "D:\\Projects\\bfvstats\\ev_15567-20161224_0825.xml";
    File file = new File(fileName);
    BfLog bfLog = XmlParser.parseXmlLogFile(file);

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
}
