package io.github.bfvstats.controller;

import io.github.bfvstats.model.PlayerStats;
import io.github.bfvstats.service.RankingService;
import ro.pippo.controller.Controller;

import javax.inject.Inject;
import java.util.List;

import static io.github.bfvstats.util.SortUtils.getSortColumnAndOrderFromRequest;

public class RankingController extends Controller {

  private RankingService rankingService;

  @Inject
  public RankingController(RankingService rankingService) {
    this.rankingService = rankingService;
  }

  public void ranking() {
    List<PlayerStats> players = rankingService.getRankings(getSortColumnAndOrderFromRequest(getRequest()));
    getResponse().bind("players", players).render("ranking/ranking");
  }
}