package io.github.bfvstats.logparser.xml;

import lombok.Getter;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement(name = "nonprint")
@Getter
public class BfNonPrint {
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
    char charValue = (char) this.value;
    return Character.toString(charValue);
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