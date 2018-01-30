package io.github.bfstats.controller;

import io.github.bfstats.model.BasicMapInfo;
import io.github.bfstats.model.Game;
import io.github.bfstats.service.GameService;
import io.github.bfstats.service.MapService;
import io.github.bfstats.util.SortUtils;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.Path;
import ro.pippo.controller.extractor.Param;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/games")
public class GameController extends Controller {

  private final GameService gameService;
  private final MapService mapService;

  @Inject
  public GameController(GameService gameService, MapService mapService) {
    this.gameService = gameService;
    this.mapService = mapService;
  }

  @GET("/?")
  public void list() {
    int page = SortUtils.getPageFromRequest(getRequest());

    List<Game> games = gameService.getGames(page);

    Map<LocalDate, List<Game>> gamesByDay = games.stream()
        .collect(Collectors.groupingBy(g -> g.getStartTime().toLocalDate(), LinkedHashMap::new, Collectors.toList()));

    int totalGamesCount = gameService.getTotalGamesCount();

    getResponse()
        .bind("games", gamesByDay)
        .bind("totalGamesCount", totalGamesCount)
        .bind("currentPage", page)
        .render("games/list");
  }

  @GET("{id: [0-9]+}")
  public void details(@Param("id") int gameId) {
    Game game = gameService.getGame(gameId);

    BasicMapInfo basicMapInfo = mapService.getBasicMapInfo(game.getMapCode());

    getResponse()
        .bind("game", game)
        .bind("map", basicMapInfo)
        .render("games/details");
  }
}
