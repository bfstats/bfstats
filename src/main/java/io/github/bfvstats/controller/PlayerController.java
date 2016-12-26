package io.github.bfvstats.controller;

import io.github.bfvstats.Player;
import io.github.bfvstats.model.MapUsage;
import io.github.bfvstats.model.NicknameUsage;
import io.github.bfvstats.model.VehicleUsage;
import io.github.bfvstats.model.WeaponUsage;
import io.github.bfvstats.service.PlayerService;
import ro.pippo.controller.Controller;
import ro.pippo.core.Param;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

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
    List<NicknameUsage> otherNicknames = playerService.getNicknameUsages(id).stream()
        .filter(nu -> !nu.getName().equals(player.getName())).collect(Collectors.toList());
    List<WeaponUsage> weapons = playerService.getWeaponUsages(id);
    List<VehicleUsage> vehicles = playerService.getVehicleUsages(id);
    List<MapUsage> maps = playerService.getMapUsages(id);

    getResponse()
        .bind("player", player)
        .bind("otherNicknames", otherNicknames)
        .bind("weapons", weapons)
        .bind("vehicles", vehicles)
        .bind("maps", maps)
        .render("players/details");
  }

  public void jsonRandom() {
    Player player = createPlayer();
    getRouteContext().json().send(player);
  }

  private Player createPlayer() {
    return new Player()
        .setId(12345)
        .setName("John")
        .setKeyHash("Sunflower Street, No. 6");
  }
}
