package io.github.bfvstats.logparser;

import io.github.bfvstats.logparser.xml.BfLog;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class XmlParser {
  public static BfLog parseXmlLogFile(File file) throws FileNotFoundException, JAXBException {
    JAXBContext context = JAXBContext.newInstance(BfLog.class);
    Unmarshaller um = context.createUnmarshaller();
    return (BfLog) um.unmarshal(new FileReader(file));
  }

  public static void main(String[] args) throws JAXBException, FileNotFoundException {
    String fileName = "D:\\Projects\\bfvstats\\ev_15567-20161224_0825.xml";
    File file = new File(fileName);
    BfLog bfLog = parseXmlLogFile(file);
  }
}