package io.github.bfvstats.service;

import com.google.common.collect.ImmutableMap;
import io.github.bfvstats.model.KitUsage;

import java.util.Map;

public class KitService {
  private static Map<String, String> kitNameByCode = ImmutableMap.<String, String>builder()
      .put("NVA_Assault", "NVA Assault")
      .build();


  public static String kitName(String kitCode) {
    return kitNameByCode.getOrDefault(kitCode, kitCode);
  }

  public KitUsage getKit(String kitCode) {
    return new KitUsage().setName(kitCode);
  }
}
