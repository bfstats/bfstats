package io.github.bfvstats.service;

import io.github.bfvstats.model.Location;
import io.github.bfvstats.model.MapStatsInfo;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import static io.github.bfvstats.jpa.Tables.*;
import static io.github.bfvstats.util.DbUtils.getDslContext;

public class MapService {

  public MapStatsInfo getMapStatsInfoForPlayer(String mapCode, int playerId) {
    Record2<BigDecimal, String> record = getDslContext().select(DSL.sum(SELECTBF_PLAYERSTATS.SCORE).as("score"),
        SELECTBF_GAMES.MAP.cast(String.class).as("map"))
        .from(SELECTBF_PLAYERSTATS)
        .join(SELECTBF_ROUNDS).on(SELECTBF_ROUNDS.ID.eq(SELECTBF_PLAYERSTATS.ROUND_ID))
        .join(SELECTBF_GAMES).on(SELECTBF_GAMES.ID.eq(SELECTBF_ROUNDS.GAME_ID))
        .where(SELECTBF_PLAYERSTATS.PLAYER_ID.eq(playerId).and(SELECTBF_GAMES.MAP.eq(mapCode)))
        .groupBy(SELECTBF_GAMES.MAP)
        .having(SELECTBF_PLAYERSTATS.SCORE.notEqual(0))
        .orderBy(SELECTBF_PLAYERSTATS.SCORE.desc())
        .fetchOne();

    return toMapStatsInfo(record);
  }

  private static MapStatsInfo toMapStatsInfo(Record r) {
    if (r == null) {
      return null;
    }

    String map = r.get("map", String.class);

    Collection<Location> killLocations = new ArrayList<>();
    killLocations.add(new Location(537.534f, 42.5929f, 377.772f));
    Collection<Location> deathLocations = new ArrayList<>();
    MapStatsInfo mapStatsInfo = new MapStatsInfo()
        .setMapName(map)
        .setMapFileName(map)
        .setKillLocations(killLocations)
        .setDeathLocations(deathLocations);

    return mapStatsInfo;
  }
}
