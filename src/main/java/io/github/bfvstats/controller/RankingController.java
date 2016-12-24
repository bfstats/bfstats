package io.github.bfvstats.controller;

import io.github.bfvstats.model.PlayerStats;
import io.github.bfvstats.service.RankingService;
import io.github.bfvstats.util.Sort;
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
    Sort sort = getSortColumnAndOrderFromRequest(getRequest());
    if (sort == null) {
      sort = new Sort("rank", Sort.SortOrder.ASC);
    }
    List<PlayerStats> players = rankingService.getRankings(sort);

    getResponse()
        .bind("players", players)
        .bind("sortingColumn", sort.getProperty())
        .bind("sortingOrder", sort.getOrder().name())
        .render("ranking/ranking");
  }
}