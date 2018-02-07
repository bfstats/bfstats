package io.github.bfstats.controller;

import io.github.bfstats.model.PlayerStats;
import io.github.bfstats.service.PlayerService;
import io.github.bfstats.service.RankingService;
import io.github.bfstats.util.Sort;
import io.github.bfstats.util.SortUtils;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.Path;

import javax.inject.Inject;
import java.util.List;

import static io.github.bfstats.util.SortUtils.getSortColumnAndOrderFromRequest;

@Path({"/", "/ranking"})
public class RankingController extends Controller {

  private RankingService rankingService;
  private PlayerService playerService;

  @Inject
  public RankingController(RankingService rankingService, PlayerService playerService) {
    this.rankingService = rankingService;
    this.playerService = playerService;
  }

  @GET("/")
  public void ranking() {
    Sort sort = getSortColumnAndOrderFromRequest(getRequest());
    if (sort == null) {
      sort = new Sort("player_rank", Sort.SortOrder.ASC);
    }
    int page = SortUtils.getPageFromRequest(getRequest());
    List<PlayerStats> players = rankingService.getRankings(sort, page, null);

    int totalPlayerCount = rankingService.getTotalPlayerCount();

    //DateTimeFormatter jsDateUtcParamsFormatter = DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm");

    getResponse()
        .bind("totalPlayerCount", totalPlayerCount)
        .bind("currentPage", page)
        .bind("players", players)
        .bind("sortingColumn", sort.getProperty())
        .bind("sortingOrder", sort.getOrder().name())
        .render("ranking/ranking");
  }
}
