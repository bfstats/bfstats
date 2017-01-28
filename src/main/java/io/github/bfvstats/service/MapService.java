package io.github.bfvstats.service;

import com.google.common.collect.ImmutableMap;
import io.github.bfvstats.game.jooq.tables.records.RoundPlayerDeathRecord;
import io.github.bfvstats.model.Location;
import io.github.bfvstats.model.MapStatsInfo;
import io.github.bfvstats.model.MapUsage;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.bfvstats.game.jooq.Tables.*;
import static io.github.bfvstats.util.DbUtils.getDslContext;
import static io.github.bfvstats.util.Utils.percentage;
import static org.jooq.impl.DSL.trueCondition;

public class MapService {
  public static Map<String, Integer> mapSizesByMapCode = ImmutableMap.<String, Integer>builder()
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

  public static Map<String, String> mapNameByMapCode = ImmutableMap.<String, String>builder()
      .put("defense_of_con_thien", "Defence of Con Thien")
      .put("fall_of_saigon", "Fall of Saigon")
      .put("ho_chi_minh_trail", "Ho Chi Minh Trail")
      .put("ho_chi_minh_trail_alt", "Cambodian Incursion")
      .put("hue", "Hue - 1968")
      .put("hue_alt", "Reclaiming Hue")
      .put("ia_drang", "The Ia Drang Valley")
      .put("khe_sahn", "Siege of Khe Sahn")
      .put("landing_zone_albany", "Landing Zone Albany")
      .put("lang_vei", "Fall of Lang Vei")
      .put("operation_cedar_falls", "Operation Cedar Falls")
      .put("operation_flaming_dart", "Operation Flaming Dart")
      .put("operation_game_warden", "Operation Game Warden")
      .put("operation_hastings", "Operation Hastings")
      .put("operation_irving", "Operation Irving")
      .put("quang_tri", "Quang Tri - 1968")
      .put("quang_tri_alt", "Quang Tri - 1972")
      .put("saigon68", "Saigon 1968")
      .build();

  public MapStatsInfo getMapStatsInfoForPlayer(String mapCode, Integer playerId, Integer roundId) {
    Result<RoundPlayerDeathRecord> killRecords = getDslContext()
        .select()
        .from(ROUND_PLAYER_DEATH)
        .join(ROUND).on(ROUND.ID.eq(ROUND_PLAYER_DEATH.ROUND_ID))
        .where(ROUND.MAP_CODE.eq(mapCode))
        .and(playerId == null ? ROUND_PLAYER_DEATH.KILLER_PLAYER_ID.isNotNull() : ROUND_PLAYER_DEATH.KILLER_PLAYER_ID.eq(playerId))
        .and(roundId == null ? trueCondition() : ROUND_PLAYER_DEATH.ROUND_ID.eq(roundId))
        .fetch()
        .into(ROUND_PLAYER_DEATH);

    Result<RoundPlayerDeathRecord> deathRecords = getDslContext()
        .select()
        .from(ROUND_PLAYER_DEATH)
        .join(ROUND).on(ROUND.ID.eq(ROUND_PLAYER_DEATH.ROUND_ID))
        .where(ROUND.MAP_CODE.eq(mapCode))
        .and(playerId == null ? trueCondition() : ROUND_PLAYER_DEATH.PLAYER_ID.eq(playerId))
        .and(roundId == null ? trueCondition() : ROUND_PLAYER_DEATH.ROUND_ID.eq(roundId))
        .fetch()
        .into(ROUND_PLAYER_DEATH);

    return toMapStatsInfo(mapCode, killRecords, deathRecords);
  }


  private MapStatsInfo toMapStatsInfo(String mapCode, Result<RoundPlayerDeathRecord> killRecords, Result<RoundPlayerDeathRecord> deathRecords) {
    Collection<Location> killLocations = new ArrayList<>();
    Collection<Location> deathLocations = new ArrayList<>();

    for (RoundPlayerDeathRecord roundPlayerScoreEventRecord : killRecords) {
      BigDecimal x = roundPlayerScoreEventRecord.getKillerLocationX();
      BigDecimal y = roundPlayerScoreEventRecord.getKillerLocationY();
      BigDecimal z = roundPlayerScoreEventRecord.getKillerLocationZ();
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

    Integer mapSize = mapSizesByMapCode.get(mapCode);
    String mapName = mapName(mapCode);

    return new MapStatsInfo()
        .setMapName(mapName)
        .setMapFileName(mapCode)
        .setMapSize(mapSize)
        .setKillLocations(killLocations)
        .setDeathLocations(deathLocations);
  }

  public List<MapUsage> getMapUsages() {
    Result<Record2<String, Integer>> records = getDslContext().select(ROUND.MAP_CODE, DSL.count().as("times_used"))
        .from(ROUND)
        .join(ROUND_END_STATS).on(ROUND_END_STATS.ROUND_ID.eq(ROUND.ID)) // to ignore empty rounds
        .groupBy(ROUND.MAP_CODE)
        .orderBy(DSL.count().desc())
        .fetch();

    int totalTimesUsed = records.stream()
        .map(r -> r.get("times_used", Integer.class))
        .reduce(0, Integer::sum);

    return records.stream()
        .map(r -> {
              String mapCode = r.get(ROUND.MAP_CODE);
              Integer timesUsed = r.get("times_used", Integer.class);
              return new MapUsage()
                  .setCode(mapCode)
                  .setName(mapName(mapCode))
                  .setPercentage(percentage(timesUsed, totalTimesUsed))
                  .setTimesUsed(timesUsed);
            }
        )
        .collect(Collectors.toList());
  }

  public List<MapUsage> getMapUsagesForPlayer(int playerId) {
    Result<Record3<String, Integer, Integer>> records = getDslContext().select(ROUND.MAP_CODE, ROUND_END_STATS_PLAYER.SCORE, DSL.count().as("times_used"))
        .from(ROUND_END_STATS_PLAYER)
        .join(ROUND).on(ROUND.ID.eq(ROUND_END_STATS_PLAYER.ROUND_ID))
        .where(ROUND_END_STATS_PLAYER.PLAYER_ID.eq(playerId))
        .groupBy(ROUND.MAP_CODE)
        .orderBy(ROUND_END_STATS_PLAYER.SCORE.desc())
        .fetch();

    int totalMapsScore = records.stream()
        .map(r -> r.get(ROUND_END_STATS_PLAYER.SCORE, Integer.class))
        .reduce(0, Integer::sum);

    return records.stream().map(r -> toMapUsage(r, totalMapsScore)).collect(Collectors.toList());
  }

  public static String mapName(String mapCode) {
    return mapNameByMapCode.getOrDefault(mapCode, mapCode);
  }

  private static MapUsage toMapUsage(Record r, int totalMapsScore) {
    String mapCode = r.get(ROUND.MAP_CODE);
    return new MapUsage()
        .setCode(mapCode)
        .setName(mapName(mapCode))
        .setScore(r.get(ROUND_END_STATS_PLAYER.SCORE, Integer.class))
        .setPercentage(percentage(r.get(ROUND_END_STATS_PLAYER.SCORE, Integer.class), totalMapsScore))
        .setTimesUsed(r.get("times_used", Integer.class));
  }
}
