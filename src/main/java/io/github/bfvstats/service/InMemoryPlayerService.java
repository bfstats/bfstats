package io.github.bfvstats.service;

import io.github.bfvstats.Player;

import java.util.ArrayList;
import java.util.List;

public class InMemoryPlayerService implements PlayerService {

  @Override
  public List<Player> getPlayers() {
    List<Player> players = new ArrayList<>();
    players.add(getPlayer(1));
    players.add(getPlayer(2));
    return players;
  }

  @Override
  public Player getPlayer(int id) {
    return new Player()
        .setId(id)
        .setName("John")
        .setKeyHash("Sunflower Street, No. 6");
  }
}
