package io.github.bfvstats.controller;

import io.github.bfvstats.model.ChatMessage;
import io.github.bfvstats.model.MapStatsInfo;
import io.github.bfvstats.model.Player;
import io.github.bfvstats.model.Round;
import io.github.bfvstats.service.ChatService;
import io.github.bfvstats.service.MapService;
import io.github.bfvstats.service.PlayerService;
import io.github.bfvstats.service.RoundService;
import io.github.bfvstats.util.SortUtils;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.Path;
import ro.pippo.controller.extractor.Param;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/rounds")
public class RoundController extends Controller {

  private final RoundService roundService;
  private final ChatService chatService;
  private final MapService mapService;
  private final PlayerService playerService;

  @Inject
  public RoundController(RoundService roundService, ChatService chatService, MapService mapService, PlayerService playerService) {
    this.roundService = roundService;
    this.chatService = chatService;
    this.mapService = mapService;
    this.playerService = playerService;
  }

  @GET("/?")
  public void list() {
    int page = SortUtils.getPageFromRequest(getRequest());
    List<Round> rounds = roundService.getRounds(page);

    Map<LocalDate, List<Round>> roundsByDay = rounds.stream()
        .collect(Collectors.groupingBy(r -> r.getStartTime().toLocalDate(), LinkedHashMap::new, Collectors.toList()));

    int totalRoundsCount = roundService.getTotalRoundsCount();

    getResponse()
        .bind("rounds", roundsByDay)
        .bind("totalRoundsCount", totalRoundsCount)
        .bind("currentPage", page)
        .render("rounds/list");
  }

  @GET("{id}")
  public void details(@Param("id") int roundId) {
    Round round = roundService.getRound(roundId);
    List<RoundService.RoundPlayerStats> roundPlayerStats = roundService.getRoundPlayerStats(roundId);

    MapStatsInfo mapStatsInfo = mapService.getMapStatsInfoForPlayer(round.getMapCode(), null, roundId);

    List<ChatMessage> chatMessages = chatService.getChatMessages(roundId, 1);
    Map<LocalDate, List<ChatMessage>> messagesByDay = chatMessages.stream()
        .collect(Collectors.groupingBy(r -> r.getTime().toLocalDate(), LinkedHashMap::new, Collectors.toList()));

    getResponse()
        .bind("round", round)
        .bind("playerStats", roundPlayerStats)
        .bind("map", mapStatsInfo)
        .bind("chatMessages", messagesByDay)
        .render("rounds/details");
  }


  @GET("{id}/players/{playerId}")
  public void details(@Param("id") int roundId, @Param("playerId") int playerId) {
    Round round = roundService.getRound(roundId);
    List<RoundService.RoundPlayerStats> roundPlayerStats = roundService.getRoundPlayerStats(roundId);

    MapStatsInfo mapStatsInfo = mapService.getMapStatsInfoForPlayer(round.getMapCode(), playerId, roundId);

    Player player = playerService.getPlayer(playerId);

    getResponse()
        .bind("player", player)
        .bind("round", round)
        .bind("playerStats", roundPlayerStats)
        .bind("map", mapStatsInfo)
        .bind("chatMessages", new HashMap<>())
        .render("rounds/player");
  }
}
