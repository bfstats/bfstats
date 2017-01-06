package io.github.bfvstats.controller;

import io.github.bfvstats.model.ChatMessage;
import io.github.bfvstats.model.Round;
import io.github.bfvstats.service.ChatService;
import io.github.bfvstats.service.RoundService;
import ro.pippo.controller.Controller;
import ro.pippo.core.Param;

import javax.inject.Inject;
import java.util.List;

public class RoundController extends Controller {

  private final RoundService roundService;
  private final ChatService chatService;

  @Inject
  public RoundController(RoundService roundService, ChatService chatService) {
    this.roundService = roundService;
    this.chatService = chatService;
  }

  public void list() {
    List<Round> rounds = roundService.getRounds();
    getResponse().bind("rounds", rounds).render("rounds/list");
  }

  public void details(@Param("id") int roundId) {
    Round round = roundService.getRound(roundId);
    List<ChatMessage> chatMessages = chatService.getChatMessages(roundId);

    getResponse()
        .bind("round", round)
        .bind("chatMessages", chatMessages)
        .render("rounds/details");
  }
}
