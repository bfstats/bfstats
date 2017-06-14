package io.github.bfvstats.controller;

import io.github.bfvstats.model.*;
import io.github.bfvstats.service.MapService;
import io.github.bfvstats.service.PlayerService;
import io.github.bfvstats.service.RankingService;
import io.github.bfvstats.service.RoundService;
import io.github.bfvstats.util.Sort;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.POST;
import ro.pippo.controller.Path;
import ro.pippo.controller.extractor.Param;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;

@Path("/players")
public class PlayerController extends Controller {

  private PlayerService playerService;
  private MapService mapService;
  private RankingService rankingService;
  private RoundService roundService;

  @Inject
  public PlayerController(PlayerService playerService, MapService mapService, RankingService rankingService, RoundService roundService) {
    this.playerService = playerService;
    this.mapService = mapService;
    this.rankingService = rankingService;
    this.roundService = roundService;
  }

  @GET({"/?", "/search"})
  @POST("/search")
  public void search(@Nullable @Param("query") String partialName) {
    List<Player> players;
    if (partialName != null) {
      players = playerService.findPlayers(partialName);
    } else {
      players = playerService.getPlayers();
    }

    getResponse().bind("players", players)
        .bind("query", partialName)
        .render("players/search");
  }

  @GET("/{id: [0-9]+}")
  public void details(@Param("id") int playerId) {
    Player player = playerService.getPlayer(playerId);
    List<NicknameUsage> nicknameUsages = playerService.getNicknameUsages(playerId);
    Sort dummySort = new Sort("player_rank", Sort.SortOrder.ASC);
    PlayerStats playerStats = rankingService.getRankings(dummySort, 1, playerId).stream().findFirst().orElse(null);
    PlayerDetails playerDetails = playerService.getPlayerDetails(playerId);

    List<WeaponUsage> weapons = playerService.getWeaponUsages(playerId);
    List<KitUsage> kits = playerService.getKitUsages(playerId);
    List<VehicleUsage> vehicles = playerService.getVehicleUsages(playerId);
    List<MapUsage> maps = mapService.getMapUsagesForPlayer(playerId);

    List<PlayerAndTotal> killsByVictims = playerService.getKillsByVictims(playerId);
    List<PlayerAndTotal> deathsByKillers = playerService.getDeathsByKillers(playerId);

    List<Round> rounds = roundService.getRoundsForPlayer(playerId);

    getResponse()
        .bind("player", player)
        .bind("nicknames", nicknameUsages)
        .bind("playerStats", playerStats)
        .bind("playerDetails", playerDetails)
        .bind("killsByVictims", killsByVictims)
        .bind("deathsByKillers", deathsByKillers)
        .bind("weapons", weapons)
        .bind("kits", kits)
        .bind("vehicles", vehicles)
        .bind("maps", maps)
        .bind("rounds", rounds)
        .render("players/details");
  }

  @GET("/{id: [0-9]+}/map/{mapCode}")
  public void mapStats(@Param("id") int playerId, @Param("mapCode") String mapCode) {
    Player player = playerService.getPlayer(playerId);
    BasicMapInfo basicMapInfo = mapService.getBasicMapInfo(mapCode);

    getResponse()
        .bind("player", player)
        .bind("map", basicMapInfo)
        .bind("mapEventsUrlPath", "players/json/" + player.getId() + "/map/" + mapCode + "/events")
        .render("players/map");
  }

  @GET("json/{id: [0-9]+}/map/{mapCode}/events")
  public void mapEventsJson(@Param("id") int playerId, @Param("mapCode") String mapCode) {
    MapEvents mapEvents = mapService.getMapEvents(mapCode, playerId, null);
    getRouteContext().json().send(mapEvents);
  }
}
