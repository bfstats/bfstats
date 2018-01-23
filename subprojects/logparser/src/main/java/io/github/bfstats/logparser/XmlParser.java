package io.github.bfstats.logparser;

import io.github.bfstats.logparser.xml.BfLog;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;

public class XmlParser {
  public static BfLog parseXmlLogFile(File file, boolean tryFixing) throws IOException, JAXBException {
    if (file.length() == 0) {
      throw new IOException("skipping empty file");
    }

    JAXBContext context = JAXBContext.newInstance(BfLog.class);
    Unmarshaller um = context.createUnmarshaller();
    try {
      return (BfLog) um.unmarshal(new FileReader(file));
    } catch (UnmarshalException e) {
      if (tryFixing) {
        File fixedFile = fixFileEnding(file);
        return (BfLog) um.unmarshal(new FileReader(fixedFile));
      }
      throw e;
    }
  }

  public static void main(String[] args) throws JAXBException, IOException {
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

    // TODO: check if round is actually open
    lines.add("</bf:round>");
    lines.add("</bf:log>");

    //String newPathStr = file.getAbsolutePath() + ".fixed.xml";
    //Path newPath = Paths.get(newPathStr);
    return Files.write(path, lines).toFile();
  }
}
