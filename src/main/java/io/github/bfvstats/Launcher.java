package io.github.bfvstats;

import ro.pippo.core.Pippo;
import ro.pippo.core.PippoConstants;
import ro.pippo.core.RuntimeMode;

public class Launcher {
  public static void main(String[] args) {
    System.setProperty(PippoConstants.SYSTEM_PROPERTY_PIPPO_MODE, RuntimeMode.DEV.toString());
    Pippo pippo = new Pippo(new BasicApplication());
    pippo.start();
  }
}