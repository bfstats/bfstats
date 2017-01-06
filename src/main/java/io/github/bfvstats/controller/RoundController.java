package io.github.bfvstats.controller;

import io.github.bfvstats.model.Round;
import io.github.bfvstats.service.RoundService;
import ro.pippo.controller.Controller;
import ro.pippo.core.Param;

import javax.inject.Inject;
import java.util.List;

public class RoundController extends Controller {

  private final RoundService roundService;

  @Inject
  public RoundController(RoundService roundService) {
    this.roundService = roundService;
  }

  public void list() {
    List<Round> rounds = roundService.getRounds();
    getResponse().bind("rounds", rounds).render("rounds/list");
  }

  public void details(@Param("id") int roundId) {
    Round round = roundService.getRound(roundId);

    getResponse()
        .bind("round", round)
        .render("rounds/details");
  }
}
