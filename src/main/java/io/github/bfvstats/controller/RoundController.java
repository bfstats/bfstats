package io.github.bfvstats.controller;

import io.github.bfvstats.model.ChatMessage;
import io.github.bfvstats.model.MapStatsInfo;
import io.github.bfvstats.model.Round;
import io.github.bfvstats.service.ChatService;
import io.github.bfvstats.service.MapService;
import io.github.bfvstats.service.RoundService;
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

@Path("/rounds")
public class RoundController extends Controller {

  private final RoundService roundService;
  private final ChatService chatService;
  private final MapService mapService;

  @Inject
  public RoundController(RoundService roundService, ChatService chatService, MapService mapService) {
    this.roundService = roundService;
    this.chatService = chatService;
    this.mapService = mapService;
  }

  @GET("/?")
  public void list() {
    List<Round> rounds = roundService.getRounds(null);

    Map<LocalDate, List<Round>> roundsByDay = rounds.stream()
        .collect(Collectors.groupingBy(r -> r.getStartTime().toLocalDate(), LinkedHashMap::new, Collectors.toList()));

    getResponse().bind("rounds", roundsByDay).render("rounds/list");
  }

  @GET("{id}")
  public void details(@Param("id") int roundId) {
    Round round = roundService.getRound(roundId);
    List<RoundService.RoundPlayerStats> roundPlayerStats = roundService.getRoundPlayerStats(roundId);

    MapStatsInfo mapStatsInfo = mapService.getMapStatsInfoForPlayer(round.getMapCode(), null, roundId);

    List<ChatMessage> chatMessages = chatService.getChatMessages(roundId);
    Map<LocalDate, List<ChatMessage>> messagesByDay = chatMessages.stream()
        .collect(Collectors.groupingBy(r -> r.getTime().toLocalDate(), LinkedHashMap::new, Collectors.toList()));

    getResponse()
        .bind("round", round)
        .bind("playerStats", roundPlayerStats)
        .bind("map", mapStatsInfo)
        .bind("chatMessages", messagesByDay)
        .render("rounds/details");
  }
}
