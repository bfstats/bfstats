package io.github.bfvstats;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Accessors(chain = true)
@XmlRootElement
public class Player {
  private int id;
  private String name;
  private String phone;
  private String address;
}