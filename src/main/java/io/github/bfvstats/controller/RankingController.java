package io.github.bfvstats.controller;

import io.github.bfvstats.model.PlayerStats;
import io.github.bfvstats.service.PlayerService;
import io.github.bfvstats.service.RankingService;
import io.github.bfvstats.util.Sort;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.Path;
import ro.pippo.core.Request;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static io.github.bfvstats.util.SortUtils.getSortColumnAndOrderFromRequest;

@Path("/ranking")
public class RankingController extends Controller {

  private RankingService rankingService;
  private PlayerService playerService;

  @Inject
  public RankingController(RankingService rankingService, PlayerService playerService) {
    this.rankingService = rankingService;
    this.playerService = playerService;
  }

  public int getPageFromRequest(Request request) {
    String page = Arrays.stream(request.getQueryParameter("page").getValues())
        .findFirst()
        .orElse("1");
    return Integer.valueOf(page);
  }

  @GET("/?")
  public void ranking() {
    Sort sort = getSortColumnAndOrderFromRequest(getRequest());
    if (sort == null) {
      sort = new Sort("player_rank", Sort.SortOrder.ASC);
    }
    int page = getPageFromRequest(getRequest());
    List<PlayerStats> players = rankingService.getRankings(sort, page, null);

    int totalPlayerCount = rankingService.getTotalPlayerCount();

    //DateTimeFormatter jsDateUtcParamsFormatter = DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm");

    getResponse()
        .bind("totalPlayerCount", totalPlayerCount)
        .bind("players", players)
        .bind("sortingColumn", sort.getProperty())
        .bind("sortingOrder", sort.getOrder().name())
        .bind("currentPage", page)
        .render("ranking/ranking");
  }
}