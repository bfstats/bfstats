package io.github.bfvstats;

import ro.pippo.core.Pippo;
import ro.pippo.core.PippoConstants;
import ro.pippo.core.RuntimeMode;

public class Launcher {
  public static void main(String[] args) {
    System.setProperty(PippoConstants.SYSTEM_PROPERTY_PIPPO_MODE, RuntimeMode.DEV.toString());
    System.setProperty("pippo.hideLogo", "true");
    System.setProperty("org.jooq.no-logo", "true");
    Pippo pippo = new Pippo(new BasicApplication());
    pippo.start();
  }
}