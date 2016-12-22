package io.github.bfvstats.controller;

import io.github.bfvstats.Player;
import io.github.bfvstats.service.PlayerService;
import ro.pippo.controller.Controller;
import ro.pippo.core.Param;

import javax.inject.Inject;
import java.util.List;

public class PlayerController extends Controller {

  private PlayerService playerService;

  @Inject
  public PlayerController(PlayerService playerService) {
    this.playerService = playerService;
  }

  public void list() {
    List<Player> players = playerService.getPlayers();
    getResponse().bind("players", players).render("players/list");
  }

  public void details(@Param("id") int id) {
    Player player = playerService.getPlayer(id);
    getResponse().bind("player", player).render("players/details");
  }
}
