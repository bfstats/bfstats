package io.github.bfvstats.controller;

import io.github.bfvstats.model.ChatMessage;
import io.github.bfvstats.model.MapStatsInfo;
import io.github.bfvstats.model.Round;
import io.github.bfvstats.service.ChatService;
import io.github.bfvstats.service.MapService;
import io.github.bfvstats.service.RoundService;
import ro.pippo.controller.Controller;
import ro.pippo.core.Param;

import javax.inject.Inject;
import java.util.List;

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

  public void list() {
    List<Round> rounds = roundService.getRounds(null);
    getResponse().bind("rounds", rounds).render("rounds/list");
  }

  public void details(@Param("id") int roundId) {
    Round round = roundService.getRound(roundId);
    List<RoundService.RoundPlayerStats> roundPlayerStats = roundService.getRoundPlayerStats(roundId);

    MapStatsInfo mapStatsInfo = mapService.getMapStatsInfoForPlayer(round.getMapCode(), null, roundId);

    List<ChatMessage> chatMessages = chatService.getChatMessages(roundId);

    getResponse()
        .bind("round", round)
        .bind("playerStats", roundPlayerStats)
        .bind("mapInfo", mapStatsInfo)
        .bind("chatMessages", chatMessages)
        .render("rounds/details");
  }
}
