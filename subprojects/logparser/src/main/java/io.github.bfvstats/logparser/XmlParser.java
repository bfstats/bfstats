package io.github.bfvstats.logparser;

import io.github.bfvstats.logparser.xml.BfLog;
import io.github.bfvstats.logparser.xml.BfRoundStats;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Duration;
import java.util.List;

public class XmlParser {
  public static void main(String[] args) throws JAXBException, FileNotFoundException {
    JAXBContext context = JAXBContext.newInstance(BfLog.class);
    Unmarshaller um = context.createUnmarshaller();
    BfLog bfLog = (BfLog) um.unmarshal(new FileReader("D:\\Projects\\bfvstats\\example.xml"));
    List<BfRoundStats.BfPlayerStat> playerStats = bfLog.getRounds().get(0).getRoundStats().getPlayerStats();
    playerStats.forEach(bfPlayerStat -> System.out.println(bfPlayerStat.getPlayerName() + " bot? " + bfPlayerStat.isAi()));
    Duration durationSinceRoundStart = bfLog.getRounds().get(0).getDurationSinceLogStart();
    System.out.println(durationSinceRoundStart);
    System.out.println("seconds: " + durationSinceRoundStart.getSeconds());
    System.out.println("nanos: " + durationSinceRoundStart.getNano());
    // 10.1008
    System.out.println("ok");
  }
}
