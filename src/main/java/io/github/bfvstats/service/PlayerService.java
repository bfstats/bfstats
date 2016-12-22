package io.github.bfvstats.service;

import io.github.bfvstats.Player;

import java.util.List;

public interface PlayerService {
  List<Player> getPlayers();

  Player getPlayer(int id);
}
