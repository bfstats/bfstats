package io.github.bfvstats.service;

import io.github.bfvstats.model.KitUsage;

public class KitService {
  public KitUsage getKit(String kitCode) {
    return new KitUsage().setName(kitCode);
  }
}
