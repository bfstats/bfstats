package io.github.bfstats.util;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class Utils {
  public static float percentage(int a, int total) {
    if (a < 0) {
      // would make much sense to have a negative percent
      return 0;
    }

    if (total == 0) {
      if (a != 0) {
        log.warn("total is 0, but a is " + a);
      }
      return 0;
    }
    return a * 100f / total;
  }

  public static Map<String, String> loadPropertiesFileFromResources(String filePath) {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    Properties props = new Properties();
    try (InputStream resourceStream = loader.getResourceAsStream(filePath)) {
      props.load(resourceStream);
    } catch (IOException e) {
      log.warn("Could not load properties for path " + filePath, e);
    }
    return Maps.fromProperties(props);
  }
}
