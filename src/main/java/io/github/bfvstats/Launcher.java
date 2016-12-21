package io.github.bfvstats;

import ro.pippo.core.Pippo;

public class Launcher {
  public static void main(String[] args) {
    Pippo pippo = new Pippo(new BasicApplication());
    pippo.start();
  }
}