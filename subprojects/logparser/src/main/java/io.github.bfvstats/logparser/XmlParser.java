package io.github.bfvstats.logparser;

import io.github.bfvstats.logparser.xml.BfLog;
import io.github.bfvstats.logparser.xml.BfRoundStats;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

public class XmlParser {
  public static void main(String[] args) throws JAXBException, FileNotFoundException {
    JAXBContext context = JAXBContext.newInstance(BfLog.class);

    Unmarshaller um = context.createUnmarshaller();

    System.out.println();
    System.out.println("Output from our XML File: ");
    BfLog bfLog = (BfLog) um.unmarshal(new FileReader("D:\\Projects\\bfvstats\\example.xml"));
    String engine = bfLog.getEngine();
    BfRoundStats.BfPlayerStat bfPlayerStat = bfLog.getRounds().get(0).getRoundStats().getPlayerStats().get(0);
    Map<String, String> parameters = bfPlayerStat.getParameters();
    System.out.println("ok");
  }
}
