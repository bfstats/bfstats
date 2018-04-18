package io.github.bfstats.service;

import com.google.common.collect.ImmutableMap;
import io.github.bfstats.model.*;
import io.github.bfstats.model.geojson.Feature;
import io.github.bfstats.model.geojson.FeatureCollection;
import io.github.bfstats.model.geojson.PointGeometry;
import io.github.bfstats.util.TranslationUtil;
import org.jooq.*;
import org.jooq.impl.DSL;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.bfstats.dbstats.jooq.Tables.*;
import static io.github.bfstats.util.DbUtils.getDslContext;
import static io.github.bfstats.util.Utils.loadPropertiesFileFromResources;
import static io.github.bfstats.util.Utils.percentage;
import static java.util.Optional.ofNullable;

public class MapService {
  private RoundService roundService;

  @Inject
  public MapService(RoundService roundService) {
    this.roundService = roundService;
  }

  private static Map<String, Map<String, Integer>> mapSizeByMapCodeByGameCode = ImmutableMap.<String, Map<String, Integer>>builder()
      .put("bf1942", findMapSizes("bf1942"))
      .put("bfvietnam", findMapSizes("bfvietnam"))
      .build();

  private static Map<String, Integer> findMapSizes(String modName) {
    return loadPropertiesFileFromResources("maps/" + modName + "/mapsizes.properties")
        .entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, e -> Integer.parseInt(e.getValue())));
  }

  public BasicMapInfo getBasicMapInfo(String gameCode, String mapCode) {
    String mapName = TranslationUtil.getMapName(gameCode, mapCode);

    Integer mapSize = mapSizeByMapCodeByGameCode.get(gameCode).get(mapCode);

    return new BasicMapInfo()
        .setGameCode(gameCode)
        .setMapCode(mapCode)
        .setMapName(mapName)
        .setMapFileName(mapCode)
        .setMapSize(mapSize);
  }

  public MapEvents getMapEvents(String gameCode, String mapCode, Integer playerId, Integer roundId, boolean withProps) {
    Result<Record> killRecords = roundService.fetchKillRecords(mapCode, playerId, roundId);
    Result<Record> deathRecords = roundService.fetchDeathRecords(mapCode, playerId, roundId);
    return toMapEvents(gameCode, mapCode, killRecords, deathRecords, withProps);
  }


  private MapEvents toMapEvents(String gameCode, String mapCode, Result<Record> killRecords, Result<Record> deathRecords, boolean withProps) {
    Collection<Feature> killFeatures = new ArrayList<>();
    for (Record deathRecord : killRecords) {
      BigDecimal killerX = deathRecord.get(ROUND_PLAYER_DEATH.KILLER_LOCATION_X);
      BigDecimal killerY = deathRecord.get(ROUND_PLAYER_DEATH.KILLER_LOCATION_Y);
      BigDecimal killerZ = deathRecord.get(ROUND_PLAYER_DEATH.KILLER_LOCATION_Z);
      Location killerLocation = new Location(killerX.floatValue(), killerY.floatValue(), killerZ.floatValue());

      Feature feature = new Feature();
      feature.geometry = new PointGeometry(new float[]{killerLocation.getX(), killerLocation.getZ()});

      if (withProps) {
        MapEvent killEvent = roundService.toKillEvent(gameCode, deathRecord, killerLocation);
        feature.properties = createProps(killEvent, true);
      }

      killFeatures.add(feature);
    }
    FeatureCollection killFeatureCollection = new FeatureCollection(killFeatures);

    Collection<Feature> deathFeatures = new ArrayList<>();
    for (Record deathRecord : deathRecords) {
      BigDecimal deathX = deathRecord.get(ROUND_PLAYER_DEATH.PLAYER_LOCATION_X);
      BigDecimal deathY = deathRecord.get(ROUND_PLAYER_DEATH.PLAYER_LOCATION_Y);
      BigDecimal deathZ = deathRecord.get(ROUND_PLAYER_DEATH.PLAYER_LOCATION_Z);
      Location deathLocation = new Location(deathX.floatValue(), deathY.floatValue(), deathZ.floatValue());

      Feature feature = new Feature();
      feature.geometry = new PointGeometry(new float[]{deathLocation.getX(), deathLocation.getZ()});

      if (withProps) {
        MapEvent deathEvent = roundService.toDeathEvent(gameCode, deathRecord, deathLocation);
        feature.properties = createProps(deathEvent, false);
      }

      //String popupContent = String.format("%s <span style='font-weight: bold'>%s</span> %s %s %s %s", mapEvent.getTime(), mapEvent.getKillerPlayerName(), mapEvent.getKillerPlayerTeam(), mapEvent.getKillWeapon().getName(), mapEvent.getPlayerName(), mapEvent.getPlayerTeam());
      //feature.properties.put("popupContent", popupContent);
      deathFeatures.add(feature);
    }
    FeatureCollection deathFeatureCollection = new FeatureCollection(deathFeatures);

    BasicMapInfo basicMapInfo = getBasicMapInfo(gameCode, mapCode);

    return new MapEvents()
        .setMapName(basicMapInfo.getMapName())
        .setMapFileName(basicMapInfo.getMapFileName())
        .setMapSize(basicMapInfo.getMapSize())
        .setKillFeatureCollection(killFeatureCollection)
        .setDeathFeatureCollection(deathFeatureCollection);
  }


  private static String createPopupContent(@Nonnull MapEvent mapEvent, boolean kill) {
    String time = mapEvent.getTime().format(DateTimeFormatter.ISO_LOCAL_TIME);

    String killWeaponName = ofNullable(mapEvent.getKillWeapon()).map(Weapon::getName).orElse("killed");

    String styleBold = "style='font-weight: bold;font-size:1.2em'";

    if (mapEvent.getKillerPlayerName() == null) {
      String victim = String.format("<span class='name team-%d'" + (!kill ? styleBold : "") + ">%s</span> died", mapEvent.getPlayerTeam(), mapEvent.getPlayerName());
      return String.format("%s %s", time, victim);
    } else {
      String killer = String.format("<span class='name team-%d'" + (kill ? styleBold : "") + ">%s</span>", mapEvent.getKillerPlayerTeam(), mapEvent.getKillerPlayerName());
      String victim = String.format("<span class='name team-%d'" + (!kill ? styleBold : "") + ">%s</span>", mapEvent.getPlayerTeam(), mapEvent.getPlayerName());

      String cssClass = "team-color-" + mapEvent.getKillerPlayerTeam();
      String distance = String.format("%.0f", Math.floor(mapEvent.getDistance()));
      return String.format("<span class='%s'>%s [%s] %s<br>%s (%s meters)</span>", cssClass, killer, killWeaponName, victim, time, distance);
    }
  }

  private static Map<String, Object> createProps(@Nonnull MapEvent mapEvent, boolean kill) {
    Map<String, Object> props = new HashMap<>();
    props.put("type", kill ? "kill" : "death");
    props.put("time", mapEvent.getTime().format(DateTimeFormatter.ISO_LOCAL_TIME));
    props.put("killerName", mapEvent.getKillerPlayerName());
    props.put("killerTeam", mapEvent.getKillerPlayerTeam());
    props.put("killWeaponName", ofNullable(mapEvent.getKillWeapon()).map(Weapon::getName).orElse(null));
    props.put("victimName", mapEvent.getPlayerName());
    props.put("victimTeam", mapEvent.getPlayerTeam());
    props.put("popupContent", createPopupContent(mapEvent, kill));
    return props;
  }

  public List<MapUsage> getMapUsages() {
    Result<Record3<String, String, Integer>> records = getDslContext().select(ROUND.GAME_CODE, ROUND.MAP_CODE, DSL.count().as("times_used"))
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
              String gameCode = r.get(ROUND.GAME_CODE);
              String mapCode = r.get(ROUND.MAP_CODE);
              Integer timesUsed = r.get("times_used", Integer.class);
              return new MapUsage()
                  .setGameCode(gameCode)
                  .setCode(mapCode)
                  .setName(TranslationUtil.getMapName(gameCode, mapCode))
                  .setPercentage(percentage(timesUsed, totalTimesUsed))
                  .setTimesUsed(timesUsed);
            }
        )
        .collect(Collectors.toList());
  }

  public List<MapUsage> getMapUsagesForPlayer(int playerId) {
    Field<BigDecimal> fieldMapTotalScore = DSL.sum(ROUND_END_STATS_PLAYER.SCORE).as("map_total_score");
    Result<Record4<String, String, BigDecimal, Integer>> records = getDslContext()
        .select(ROUND.GAME_CODE, ROUND.MAP_CODE, fieldMapTotalScore, DSL.count().as("times_used"))
        .from(ROUND_END_STATS_PLAYER)
        .join(ROUND).on(ROUND.ID.eq(ROUND_END_STATS_PLAYER.ROUND_ID))
        .where(ROUND_END_STATS_PLAYER.PLAYER_ID.eq(playerId))
        .groupBy(ROUND.MAP_CODE)
        .orderBy(fieldMapTotalScore.desc())
        .limit(10)
        .fetch();

    int totalMapsScore = records.stream()
        .map(r -> r.get("map_total_score", Integer.class))
        .reduce(0, Integer::sum);

    return records.stream().map(r -> toMapUsage(r, totalMapsScore)).collect(Collectors.toList());
  }

  private static MapUsage toMapUsage(Record r, int totalMapsScore) {
    String gameCode = r.get(ROUND.GAME_CODE);
    String mapCode = r.get(ROUND.MAP_CODE);
    Integer mapTotalScore = r.get("map_total_score", Integer.class);
    return new MapUsage()
        .setGameCode(gameCode)
        .setCode(mapCode)
        .setName(TranslationUtil.getMapName(gameCode, mapCode))
        .setScore(mapTotalScore)
        .setPercentage(percentage(mapTotalScore, totalMapsScore))
        .setTimesUsed(r.get("times_used", Integer.class));
  }
}
