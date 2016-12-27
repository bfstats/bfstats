package io.github.bfvstats.logparser;

import io.github.bfvstats.logparser.xml.BfLog;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class XmlParser {
  public static void main(String[] args) throws JAXBException, FileNotFoundException {
    JAXBContext context = JAXBContext.newInstance(BfLog.class);

    System.out.println();
    System.out.println("Output from our XML File: ");
    Unmarshaller um = context.createUnmarshaller();


    BfLog bfLog = (BfLog) um.unmarshal(new FileReader("D:\\Projects\\bfvstats\\example.xml"));
    String engine = bfLog.getEngine();
  }
}
