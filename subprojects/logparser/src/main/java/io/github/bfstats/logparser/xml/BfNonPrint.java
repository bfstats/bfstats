package io.github.bfstats.logparser.xml;

import lombok.Getter;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement(name = "nonprint")
@Getter
public class BfNonPrint {
  private static final Charset CHARSET_CYRILLIC = Charset.forName("windows-1251");

  @XmlValue
  int value;

  @XmlTransient
  private String strValue;

  void afterUnmarshal(Unmarshaller u, Object parent) {
    this.strValue = valueAsString();
  }

  private String valueAsString() {
    // 128 seems to be used for space, at least in sayAll event
    // char code references something else for some reason :S
    if (value == 128) {
      return " ";
    }

    byte charCode = (byte) (this.value & 0xFF);
    return new String(new byte[]{charCode}, CHARSET_CYRILLIC);
  }

  public String toString() {
    return String.valueOf(strValue);
  }

  public static String mixedContentToString(List<Object> mixedContent) {
    String value = mixedContent.stream()
        .map(Object::toString)
        .collect(Collectors.joining());

    return value.replace("\n", "").replace("\r", "");
  }
}
