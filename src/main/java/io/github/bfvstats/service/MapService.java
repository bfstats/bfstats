package io.github.bfvstats.service;

import com.google.common.collect.ImmutableMap;
import io.github.bfvstats.game.jooq.tables.records.RoundPlayerDeathRecord;
import io.github.bfvstats.model.Location;
import io.github.bfvstats.model.MapStatsInfo;
import io.github.bfvstats.model.MapUsage;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.bfvstats.game.jooq.Tables.*;
import static io.github.bfvstats.util.DbUtils.getDslContext;

public class MapService {
  public static Map<String, Integer> mapSizesByMap = ImmutableMap.<String, Integer>builder()
      .put("defense_of_con_thien", 2048)
      .put("fall_of_saigon", 2048)
      .put("ho_chi_minh_trail", 1024)
      .put("ho_chi_minh_trail_alt", 1024)
      .put("hue", 1024)
      .put("hue_alt", 1024)
      .put("ia_drang", 2048)
      .put("khe_sahn", 2048)
      .put("landing_zone_albany", 2048)
      .put("lang_vei", 2048)
      .put("operation_cedar_falls", 2048)
      .put("operation_flaming_dart", 2048)
      .put("operation_game_warden", 2048)
      .put("operation_hastings", 2048)
      .put("operation_irving", 2048)
      .put("quang_tri", 1024)
      .put("quang_tri_alt", 1024)
      .put("saigon68", 1024)
      .build();

  public MapStatsInfo getMapStatsInfoForPlayer(String mapCode, int playerId) {
    Result<RoundPlayerDeathRecord> killRecords = getDslContext()
        .select()
        .from(ROUND_PLAYER_DEATH)
        .join(ROUND).on(ROUND.ID.eq(ROUND_PLAYER_DEATH.ROUND_ID))
        .where(ROUND.MAP_CODE.eq(mapCode))
        .and(ROUND_PLAYER_DEATH.KILLER_PLAYER_ID.eq(playerId))
        .fetch()
        .into(ROUND_PLAYER_DEATH);

    Result<RoundPlayerDeathRecord> deathRecords = getDslContext()
        .select()
        .from(ROUND_PLAYER_DEATH)
        .join(ROUND).on(ROUND.ID.eq(ROUND_PLAYER_DEATH.ROUND_ID))
        .where(ROUND.MAP_CODE.eq(mapCode))
        .and(ROUND_PLAYER_DEATH.PLAYER_ID.eq(playerId))
        .fetch()
        .into(ROUND_PLAYER_DEATH);

    return toMapStatsInfo(mapCode, killRecords, deathRecords);
  }


  private MapStatsInfo toMapStatsInfo(String mapCode, Result<RoundPlayerDeathRecord> killRecords, Result<RoundPlayerDeathRecord> deathRecords) {
    Collection<Location> killLocations = new ArrayList<>();
    Collection<Location> deathLocations = new ArrayList<>();

    for (RoundPlayerDeathRecord roundPlayerScoreEventRecord : killRecords) {
      BigDecimal x = roundPlayerScoreEventRecord.getPlayerLocationX();
      BigDecimal y = roundPlayerScoreEventRecord.getPlayerLocationY();
      BigDecimal z = roundPlayerScoreEventRecord.getPlayerLocationZ();
      Location location = new Location(x.floatValue(), y.floatValue(), z.floatValue());
      killLocations.add(location);
    }

    for (RoundPlayerDeathRecord roundPlayerScoreEventRecord : deathRecords) {
      BigDecimal x = roundPlayerScoreEventRecord.getPlayerLocationX();
      BigDecimal y = roundPlayerScoreEventRecord.getPlayerLocationY();
      BigDecimal z = roundPlayerScoreEventRecord.getPlayerLocationZ();
      Location location = new Location(x.floatValue(), y.floatValue(), z.floatValue());
      deathLocations.add(location);
    }

    Integer mapSize = mapSizesByMap.get(mapCode);

    return new MapStatsInfo()
        .setMapName(mapCode)
        .setMapFileName(mapCode)
        .setMapSize(mapSize)
        .setKillLocations(killLocations)
        .setDeathLocations(deathLocations);
  }

  public List<MapUsage> getMapUsagesForPlayer(int playerId) {
    Result<Record2<String, Integer>> records = getDslContext().select(ROUND.MAP_CODE, ROUND_END_STATS_PLAYER.SCORE)
        .from(ROUND_END_STATS_PLAYER)
        .join(ROUND).on(ROUND.ID.eq(ROUND_END_STATS_PLAYER.ROUND_ID))
        .where(ROUND_END_STATS_PLAYER.PLAYER_ID.eq(playerId))
        .groupBy(ROUND.MAP_CODE)
        .fetch();

    float totalMapsScore = records.stream()
        .map(r -> r.get(ROUND_END_STATS_PLAYER.SCORE, Integer.class))
        .reduce(0, Integer::sum);

    return records.stream().map(r -> toMapUsage(r, totalMapsScore)).collect(Collectors.toList());
  }

  private static MapUsage toMapUsage(Record r, float totalMapsScore) {
    return new MapUsage()
        .setName(r.get(ROUND.MAP_CODE, String.class))
        .setScore(r.get(ROUND_END_STATS_PLAYER.SCORE, Integer.class))
        .setPercentage(r.get(ROUND_END_STATS_PLAYER.SCORE, Integer.class) * 100 / totalMapsScore);
  }
}
