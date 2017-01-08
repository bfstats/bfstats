package io.github.bfvstats.controller;

import io.github.bfvstats.model.PlayerStats;
import io.github.bfvstats.service.PlayerService;
import io.github.bfvstats.service.RankingService;
import io.github.bfvstats.util.Sort;
import ro.pippo.controller.Controller;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.bfvstats.util.SortUtils.getSortColumnAndOrderFromRequest;

public class RankingController extends Controller {

  private RankingService rankingService;
  private PlayerService playerService;

  @Inject
  public RankingController(RankingService rankingService, PlayerService playerService) {
    this.rankingService = rankingService;
    this.playerService = playerService;
  }

  public void ranking() {
    Sort sort = getSortColumnAndOrderFromRequest(getRequest());
    if (sort == null) {
      sort = new Sort("player_rank", Sort.SortOrder.ASC);
    }
    List<PlayerStats> players = rankingService.getRankings(sort, null);

    Map<LocalDateTime, Integer> playersOnline = playerService.fetchPlayersOnlineTimes();

    //DateTimeFormatter jsDateUtcParamsFormatter = DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm");


    String playersOnlineForHighCharts = playersOnline.entrySet().stream()
        .map(localDateTimeIntegerEntry -> {
          LocalDateTime dateTime = localDateTimeIntegerEntry.getKey();
          Integer concurrentOnline = localDateTimeIntegerEntry.getValue();

          int y = dateTime.getYear();
          int mo = dateTime.getMonthValue() - 1;
          int d = dateTime.getDayOfMonth();
          int h = dateTime.getHour();
          int mi = dateTime.getMinute();
          int s = dateTime.getSecond();

          String jsDateUtcParams = y + "," + mo + "," + d + "," + h + "," + mi + "," + s;

          //String jsDateUtcParams = dateTime.format(jsDateUtcParamsFormatter);
          return "[Date.UTC(" + jsDateUtcParams + "), " + concurrentOnline + "]";
        })
        .collect(Collectors.joining(","));

    getResponse()
        .bind("playersOnline", playersOnlineForHighCharts)
        .bind("players", players)
        .bind("sortingColumn", sort.getProperty())
        .bind("sortingOrder", sort.getOrder().name())
        .render("ranking/ranking");
  }
}