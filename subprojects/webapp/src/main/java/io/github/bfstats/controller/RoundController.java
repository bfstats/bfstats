package io.github.bfstats.controller;

import io.github.bfstats.model.*;
import io.github.bfstats.service.ChatService;
import io.github.bfstats.service.MapService;
import io.github.bfstats.service.PlayerService;
import io.github.bfstats.service.RoundService;
import io.github.bfstats.util.SortUtils;
import ro.pippo.controller.*;
import ro.pippo.controller.extractor.Param;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
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

  @GET("active/?")
  public void activeRoundsList() {
    int page = SortUtils.getPageFromRequest(getRequest());
    List<Round> rounds = roundService.getActiveRounds(page);

    Map<LocalDate, List<Round>> roundsByDay = rounds.stream()
        .collect(Collectors.groupingBy(r -> r.getStartTime().toLocalDate(), LinkedHashMap::new, Collectors.toList()));

    int totalRoundsCount = roundService.getTotalActiveRoundsCount();

    getResponse()
        .bind("rounds", roundsByDay)
        .bind("totalRoundsCount", totalRoundsCount)
        .bind("currentPage", page)
        .render("rounds/list");
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

  @GET("{id: [0-9]+}")
  @Named("details")
  public void details(@Param("id") int roundId) {
    Round round = roundService.getRound(roundId);
    List<RoundService.RoundPlayerStats> roundPlayerStats = roundService.getRoundPlayerStats(roundId);

    BasicMapInfo basicMapInfo = mapService.getBasicMapInfo(round.getGameCode(), round.getMapCode());
    List<ChatMessage> chatMessages = chatService.getChatMessages(roundId, 1);

    List<RoundEvent> roundEvents = roundService.getRoundEvents(round.getGameCode(), roundId);

    List<ScoreEvent> scoreEvents = roundService.getScoreEvents(roundId);
    // TODO: mix chatMessages and roundEvents together
    //* player join/disconnect
    //* vehicle start-end?
    //* player spawn (kit, team)
    //* heal/repair

    getResponse()
        .bind("round", round)
        .bind("playerStats", roundPlayerStats)
        .bind("map", basicMapInfo)
        .bind("chatMessages", chatMessages)
        .bind("roundEvents", roundEvents)
        .bind("scoreEvents", scoreEvents)
        .render("rounds/details");
  }

  @GET("json/{id: [0-9]+}/events")
  @Produces(Produces.JSON)
  public void mapEventsJson(@Param("id") int roundId) {
    Round round = roundService.getRound(roundId);
    MapEvents mapEvents = mapService.getMapEvents(round.getGameCode(), round.getMapCode(), null, roundId, true);
    getRouteContext().json().send(mapEvents);
  }

  @GET("{id: [0-9]+}/players/{playerId: [0-9]+}")
  public void playerDetails(@Param("id") int roundId, @Param("playerId") int playerId) {
    Round round = roundService.getRound(roundId);
    List<RoundService.RoundPlayerStats> roundPlayerStats = roundService.getRoundPlayerStats(roundId);

    BasicMapInfo basicMapInfo = mapService.getBasicMapInfo(round.getGameCode(), round.getMapCode());

    Player player = playerService.getPlayer(playerId);

    getResponse()
        .bind("player", player)
        .bind("round", round)
        .bind("playerStats", roundPlayerStats)
        .bind("map", basicMapInfo)
        .bind("mapEventsUrlPath", "rounds/json/" + round.getId() + "/players/" + player.getId() + "/events")
        .bind("chatMessages", new ArrayList<ChatMessage>())
        .render("rounds/player");
  }

  @GET("json/{id: [0-9]+}/players/{playerId: [0-9]+}/events")
  @Produces(Produces.JSON)
  public void playerMapEventsJson(@Param("id") int roundId, @Param("playerId") int playerId) {
    Round round = roundService.getRound(roundId);
    MapEvents mapEvents = mapService.getMapEvents(round.getGameCode(), round.getMapCode(), playerId, roundId, true);
    getRouteContext().json().send(mapEvents);
  }
}
