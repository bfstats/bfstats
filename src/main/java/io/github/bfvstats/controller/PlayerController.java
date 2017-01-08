package io.github.bfvstats.controller;

import io.github.bfvstats.Player;
import io.github.bfvstats.model.*;
import io.github.bfvstats.service.MapService;
import io.github.bfvstats.service.PlayerService;
import ro.pippo.controller.Controller;
import ro.pippo.core.Param;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerController extends Controller {

  private PlayerService playerService;
  private MapService mapService;

  @Inject
  public PlayerController(PlayerService playerService, MapService mapService) {
    this.playerService = playerService;
    this.mapService = mapService;
  }

  public void list() {
    List<Player> players = playerService.getPlayers();
    getResponse().bind("players", players).render("players/list");
  }

  public void details(@Param("id") int playerId) {
    Player player = playerService.getPlayer(playerId);
    List<NicknameUsage> otherNicknames = playerService.getNicknameUsages(playerId).stream()
        .filter(nu -> !nu.getName().equals(player.getName())).collect(Collectors.toList());

    List<WeaponUsage> weapons = playerService.getWeaponUsages(playerId);
    List<KitUsage> kits = playerService.getKitUsages(playerId);
    List<VehicleUsage> vehicles = playerService.getVehicleUsages(playerId);
    List<MapUsage> maps = mapService.getMapUsagesForPlayer(playerId);

    getResponse()
        .bind("player", player)
        .bind("otherNicknames", otherNicknames)
        .bind("weapons", weapons)
        .bind("kits", kits)
        .bind("vehicles", vehicles)
        .bind("maps", maps)
        .render("players/details");
  }

  public void mapStats(@Param("id") int playerId, @Param("mapCode") String mapCode) {
    Player player = playerService.getPlayer(playerId);

    MapStatsInfo mapStatsInfo = mapService.getMapStatsInfoForPlayer(mapCode, playerId, null);

    getResponse()
        .bind("player", player)
        .bind("mapInfo", mapStatsInfo)
        .render("players/map");
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
