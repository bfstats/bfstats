package io.github.bfvstats.logparser;

import io.github.bfvstats.logparser.xml.BfLog;
import io.github.bfvstats.logparser.xml.BfRoundStats;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class XmlParser {
  public static void main(String[] args) throws JAXBException, FileNotFoundException {
    JAXBContext context = JAXBContext.newInstance(BfLog.class);

    Unmarshaller um = context.createUnmarshaller();

    System.out.println();
    System.out.println("Output from our XML File: ");
    BfLog bfLog = (BfLog) um.unmarshal(new FileReader("D:\\Projects\\bfvstats\\example.xml"));
    List<BfRoundStats.BfPlayerStat> playerStats = bfLog.getRounds().get(0).getRoundStats().getPlayerStats();
    playerStats.forEach(bfPlayerStat -> System.out.println(bfPlayerStat.getPlayerName() + " bot? " + bfPlayerStat.isAi()));
    System.out.println("ok");
  }
}
