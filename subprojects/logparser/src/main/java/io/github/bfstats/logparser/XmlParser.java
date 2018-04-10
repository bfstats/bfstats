package io.github.bfstats.logparser;

import io.github.bfstats.logparser.xml.BfLog;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLFilterImpl;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;

public class XmlParser {
  public static BfLog parseXmlLogFile(File file, boolean tryFixing) throws IOException, JAXBException, ParserConfigurationException, SAXException {
    if (file.length() == 0) {
      throw new IOException("skipping empty file");
    }

    XMLReader xmlReader = SAXParserFactory.newInstance()
        .newSAXParser()
        .getXMLReader();
    XMLFilter namespaceFilter = new NamespaceFilter();
    namespaceFilter.setParent(xmlReader);

    UnmarshallerHandler unmarshallerHandler = JAXBContext.newInstance(BfLog.class)
        .createUnmarshaller()
        .getUnmarshallerHandler();
    namespaceFilter.setContentHandler(unmarshallerHandler);

    try {
      return unmarshal(file, namespaceFilter, unmarshallerHandler);
    } catch (SAXParseException e) {
      if (tryFixing) {
        File fixedFile = fixFileEnding(file);
        return unmarshal(fixedFile, namespaceFilter, unmarshallerHandler);
      }
      throw e;
    }
  }

  private static BfLog unmarshal(File file, XMLFilter namespaceFilter, UnmarshallerHandler unmarshallerHandler) throws IOException, SAXException, JAXBException {
    InputSource xml = new InputSource(new FileReader(file));
    namespaceFilter.parse(xml);
    Object result = unmarshallerHandler.getResult();
    BfLog bfLog = (BfLog) result;
    if (bfLog.isTimestampMissing()) {
      String timestampString = extractTimestampStringFromFilename(file);
      bfLog.setTimestamp(timestampString);
    }
    return bfLog;
  }

  private static String extractTimestampStringFromFilename(File file) {
    String filename = file.getName().split("\\.")[0];
    String timestamp = filename.substring(filename.length() - 13);
    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
    //LocalDateTime timestampAsDate = LocalDateTime.parse(timestamp, formatter);
    return timestamp;
  }

  public static void main(String[] args) throws JAXBException, IOException, ParserConfigurationException, SAXException {
    String fileName = "D:\\bflogs\\ev_15567-20170114_1327.xml";
    File file = new File(fileName);
    BfLog bfLog = parseXmlLogFile(file, true);
  }

  private static File fixFileEnding(File file) throws IOException {
    Path path = file.toPath();
    List<String> lines = Files.readAllLines(path);
    String lastValidLine = null;
    int lastValidLineIndex = 0;
    String lastLine = null;
    int lastLineIndex = 0;
    for (int i = 0; i < lines.size(); i++) {
      String l = lines.get(i);
      if (l.startsWith("</bf:") && l.endsWith(">")) {
        lastValidLine = l;
        lastValidLineIndex = i;
      }
      lastLine = l;
      lastLineIndex = i;
    }
    if (lastLineIndex == lastValidLineIndex) {
      return file;
    }

    int badEndLines = lastLineIndex - lastValidLineIndex;
    int finalLastLineIndex = lines.size() - 1;
    IntStream.range(0, badEndLines).forEach(lineIndex -> lines.remove(finalLastLineIndex - lineIndex));

    if (lastValidLine.equals("</bf:event>")) {
    } else {
      System.out.println("last valid line was " + lastValidLine);
    }

    lines.add("<!-- xml file ending fixed by bfstats -->");
    // TODO: check if round is actually open (remember, there can be multiple rounds in one file)
    lines.add("</bf:round>");
    lines.add("</bf:log>");

    //String newPathStr = file.getAbsolutePath() + ".fixed.xml";
    //Path newPath = Paths.get(newPathStr);
    return Files.write(path, lines).toFile();
  }

  // can use different namespace uri for same namespace prefix
  public static class NamespaceFilter extends XMLFilterImpl {
    private static final String NAMESPACE = BfLog.NAMESPACE;

    @Override
    public void endElement(String uri, String localName, String qName)
        throws SAXException {
      super.endElement(NAMESPACE, localName, qName);
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes atts) throws SAXException {
      super.startElement(NAMESPACE, qName.substring(3), qName, atts);
    }

  }
}
