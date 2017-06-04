package io.github.bfvstats.service;

import com.google.common.collect.ImmutableMap;
import io.github.bfvstats.game.jooq.tables.RoundPlayerTeam;
import io.github.bfvstats.model.*;
import io.github.bfvstats.model.geojson.Feature;
import io.github.bfvstats.model.geojson.FeatureCollection;
import io.github.bfvstats.model.geojson.PointGeometry;
import io.github.bfvstats.util.TranslationUtil;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.impl.DSL;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.bfvstats.game.jooq.Tables.*;
import static io.github.bfvstats.util.DbUtils.getDslContext;
import static io.github.bfvstats.util.Utils.percentage;
import static java.util.Optional.ofNullable;
import static org.jooq.impl.DSL.trueCondition;

public class MapService {
  public static final io.github.bfvstats.game.jooq.tables.Player KILLER_PLAYER_TABLE = PLAYER.as("killerPlayer");
  public static final RoundPlayerTeam KILLER_PLAYER_TEAM_TABLE = ROUND_PLAYER_TEAM.as("killerPlayerTeam");
  private static Map<String, Integer> mapSizesByMapCode = ImmutableMap.<String, Integer>builder()
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

  public BasicMapInfo getBasicMapInfo(String mapCode) {
    Integer mapSize = mapSizesByMapCode.get(mapCode);
    String mapName = TranslationUtil.getMapName(mapCode);

    return new BasicMapInfo()
        .setMapName(mapName)
        .setMapFileName(mapCode)
        .setMapSize(mapSize);
  }

  public MapEvents getMapEvents(String mapCode, Integer playerId, Integer roundId) {
    Result<Record> killRecords = getDslContext()
        .select(ROUND_PLAYER_DEATH.fields())
        .select(PLAYER.NAME)
        .select(KILLER_PLAYER_TABLE.NAME)
        .select(ROUND_PLAYER_TEAM.TEAM)
        .select(KILLER_PLAYER_TEAM_TABLE.TEAM)
        .from(ROUND_PLAYER_DEATH)
        .join(ROUND).on(ROUND.ID.eq(ROUND_PLAYER_DEATH.ROUND_ID))
        .join(PLAYER).on(PLAYER.ID.eq(ROUND_PLAYER_DEATH.PLAYER_ID))
        .leftJoin(ROUND_PLAYER_TEAM).on(ROUND_PLAYER_TEAM.ROUND_ID.eq(ROUND_PLAYER_DEATH.ROUND_ID)
            .and(ROUND_PLAYER_TEAM.PLAYER_ID.eq(ROUND_PLAYER_DEATH.PLAYER_ID))
            .and(ROUND_PLAYER_DEATH.EVENT_TIME.between(ROUND_PLAYER_TEAM.START_TIME, ROUND_PLAYER_TEAM.END_TIME))
        )
        .leftJoin(KILLER_PLAYER_TABLE).on(KILLER_PLAYER_TABLE.ID.eq(ROUND_PLAYER_DEATH.KILLER_PLAYER_ID))
        .leftJoin(KILLER_PLAYER_TEAM_TABLE).on(KILLER_PLAYER_TEAM_TABLE.ROUND_ID.eq(ROUND_PLAYER_DEATH.ROUND_ID)
            .and(KILLER_PLAYER_TEAM_TABLE.PLAYER_ID.eq(ROUND_PLAYER_DEATH.KILLER_PLAYER_ID))
            .and(ROUND_PLAYER_DEATH.EVENT_TIME.between(KILLER_PLAYER_TEAM_TABLE.START_TIME, KILLER_PLAYER_TEAM_TABLE.END_TIME))
        )
        .where(ROUND.MAP_CODE.eq(mapCode))
        .and(playerId == null ? ROUND_PLAYER_DEATH.KILLER_PLAYER_ID.isNotNull() : ROUND_PLAYER_DEATH.KILLER_PLAYER_ID.eq(playerId))
        .and(roundId == null ? trueCondition() : ROUND_PLAYER_DEATH.ROUND_ID.eq(roundId))
        .fetch();

    Result<Record> deathRecords = getDslContext()
        .select(ROUND_PLAYER_DEATH.fields())
        .select(PLAYER.NAME)
        .select(KILLER_PLAYER_TABLE.NAME)
        .select(ROUND_PLAYER_TEAM.TEAM)
        .select(KILLER_PLAYER_TEAM_TABLE.TEAM)
        .from(ROUND_PLAYER_DEATH)
        .join(ROUND).on(ROUND.ID.eq(ROUND_PLAYER_DEATH.ROUND_ID))
        .join(PLAYER).on(PLAYER.ID.eq(ROUND_PLAYER_DEATH.PLAYER_ID))
        .leftJoin(ROUND_PLAYER_TEAM).on(ROUND_PLAYER_TEAM.ROUND_ID.eq(ROUND_PLAYER_DEATH.ROUND_ID)
            .and(ROUND_PLAYER_TEAM.PLAYER_ID.eq(ROUND_PLAYER_DEATH.PLAYER_ID))
            .and(ROUND_PLAYER_DEATH.EVENT_TIME.between(ROUND_PLAYER_TEAM.START_TIME, ROUND_PLAYER_TEAM.END_TIME))
        )
        .leftJoin(KILLER_PLAYER_TABLE).on(KILLER_PLAYER_TABLE.ID.eq(ROUND_PLAYER_DEATH.KILLER_PLAYER_ID))
        .leftJoin(KILLER_PLAYER_TEAM_TABLE).on(KILLER_PLAYER_TEAM_TABLE.ROUND_ID.eq(ROUND_PLAYER_DEATH.ROUND_ID)
            .and(KILLER_PLAYER_TEAM_TABLE.PLAYER_ID.eq(ROUND_PLAYER_DEATH.KILLER_PLAYER_ID))
            .and(ROUND_PLAYER_DEATH.EVENT_TIME.between(KILLER_PLAYER_TEAM_TABLE.START_TIME, KILLER_PLAYER_TEAM_TABLE.END_TIME))
        )
        .where(ROUND.MAP_CODE.eq(mapCode))
        .and(playerId == null ? trueCondition() : ROUND_PLAYER_DEATH.PLAYER_ID.eq(playerId))
        .and(roundId == null ? trueCondition() : ROUND_PLAYER_DEATH.ROUND_ID.eq(roundId))
        .fetch();

    return toMapEvents(mapCode, killRecords, deathRecords);
  }

  private static Integer findPlayerTeam(Record deathRecord) {
    Integer playerTeam = deathRecord.get(ROUND_PLAYER_TEAM.TEAM);
    if (playerTeam == null && deathRecord.get(PLAYER.NAME) != null && "Kill".equals(deathRecord.get(ROUND_PLAYER_DEATH.KILL_TYPE))) {
      Integer killerTeam = deathRecord.get(KILLER_PLAYER_TEAM_TABLE.TEAM);
      // we don't store bot team, so guessing it instead
      playerTeam = Objects.equals(killerTeam, 1) ? 2 : 1;
    }
    return playerTeam;
  }

  private static Integer findKillerTeam(Record deathRecord) {
    Integer killerPlayerTeam = deathRecord.get(KILLER_PLAYER_TEAM_TABLE.TEAM);
    if (killerPlayerTeam == null && deathRecord.get(KILLER_PLAYER_TABLE.NAME) != null && "Kill".equals(deathRecord.get(ROUND_PLAYER_DEATH.KILL_TYPE))) {
      Integer playerTeam = deathRecord.get(ROUND_PLAYER_TEAM.TEAM);
      // we don't store bot team, so guessing it instead
      killerPlayerTeam = Objects.equals(playerTeam, 1) ? 2 : 1;
    }
    return killerPlayerTeam;
  }

  private MapEvents toMapEvents(String mapCode, Result<Record> killRecords, Result<Record> deathRecords) {
    Collection<MapEvent> killEvents = new ArrayList<>();
    Collection<MapEvent> deathEvents = new ArrayList<>();

    Collection<Feature> killFeatures = new ArrayList<>();
    for (Record deathRecord : killRecords) {
      BigDecimal x = deathRecord.get(ROUND_PLAYER_DEATH.KILLER_LOCATION_X);
      BigDecimal y = deathRecord.get(ROUND_PLAYER_DEATH.KILLER_LOCATION_Y);
      BigDecimal z = deathRecord.get(ROUND_PLAYER_DEATH.KILLER_LOCATION_Z);
      Location location = new Location(x.floatValue(), y.floatValue(), z.floatValue());
      String killType = deathRecord.get(ROUND_PLAYER_DEATH.KILL_TYPE);

      Integer killerPlayerId = deathRecord.get(ROUND_PLAYER_DEATH.KILLER_PLAYER_ID);
      Integer playerId = deathRecord.get(ROUND_PLAYER_DEATH.PLAYER_ID);
      String killWeaponCode = deathRecord.get(ROUND_PLAYER_DEATH.KILL_WEAPON);
      String playerName = deathRecord.get(PLAYER.NAME);
      String killerPlayerName = deathRecord.get(KILLER_PLAYER_TABLE.NAME);

      Integer playerTeam = findPlayerTeam(deathRecord);
      Integer killerPlayerTeam = findKillerTeam(deathRecord);

      LocalDateTime deathTime = deathRecord.get(ROUND_PLAYER_DEATH.EVENT_TIME).toLocalDateTime();

      Weapon killWeapon = ofNullable(killWeaponCode)
          .map(c -> new Weapon(killWeaponCode, TranslationUtil.getWeaponOrVehicleName(killWeaponCode)))
          .orElse(null);


      MapEvent killEvent = new MapEvent()
          .setLocation(location)
          .setTime(deathTime)
          .setKillerPlayerId(killerPlayerId)
          .setKillerPlayerName(killerPlayerName)
          .setKillerPlayerTeam(killerPlayerTeam)
          .setPlayerId(playerId)
          .setPlayerName(playerName)
          .setPlayerTeam(playerTeam)
          .setKillWeapon(killWeapon)
          .setKillType(killType);

      killEvents.add(killEvent);

      Feature feature = new Feature();
      feature.geometry = new PointGeometry(new float[]{location.getX(), location.getZ()});
      feature.properties = createProps(killEvent, true);
      killFeatures.add(feature);
    }
    FeatureCollection killFeatureCollection = new FeatureCollection(killFeatures);

    Collection<Feature> deathFeatures = new ArrayList<>();
    for (Record deathRecord : deathRecords) {
      BigDecimal x = deathRecord.get(ROUND_PLAYER_DEATH.PLAYER_LOCATION_X);
      BigDecimal y = deathRecord.get(ROUND_PLAYER_DEATH.PLAYER_LOCATION_Y);
      BigDecimal z = deathRecord.get(ROUND_PLAYER_DEATH.PLAYER_LOCATION_Z);
      Location location = new Location(x.floatValue(), y.floatValue(), z.floatValue());
      String killType = deathRecord.get(ROUND_PLAYER_DEATH.KILL_TYPE);

      Integer killerPlayerId = deathRecord.get(ROUND_PLAYER_DEATH.KILLER_PLAYER_ID);
      Integer playerId = deathRecord.get(ROUND_PLAYER_DEATH.PLAYER_ID);
      String killWeaponCode = deathRecord.get(ROUND_PLAYER_DEATH.KILL_WEAPON);
      String playerName = deathRecord.get(PLAYER.NAME);
      String killerPlayerName = deathRecord.get(KILLER_PLAYER_TABLE.NAME);
      Integer playerTeam = findPlayerTeam(deathRecord);
      Integer killerPlayerTeam = findKillerTeam(deathRecord);

      LocalDateTime deathTime = deathRecord.get(ROUND_PLAYER_DEATH.EVENT_TIME).toLocalDateTime();

      Weapon killWeapon = ofNullable(killWeaponCode)
          .map(c -> new Weapon(killWeaponCode, TranslationUtil.getWeaponOrVehicleName(killWeaponCode)))
          .orElse(null);

      MapEvent deathEvent = new MapEvent()
          .setLocation(location)
          .setTime(deathTime)
          .setKillerPlayerId(killerPlayerId)
          .setKillerPlayerName(killerPlayerName)
          .setKillerPlayerTeam(killerPlayerTeam)
          .setPlayerId(playerId)
          .setPlayerName(playerName)
          .setPlayerTeam(playerTeam)
          .setKillWeapon(killWeapon)
          .setKillType(killType);

      deathEvents.add(deathEvent);

      Feature feature = new Feature();
      feature.geometry = new PointGeometry(new float[]{location.getX(), location.getZ()});
      feature.properties = createProps(deathEvent, false);
      //String popupContent = String.format("%s <span style='font-weight: bold'>%s</span> %s %s %s %s", mapEvent.getTime(), mapEvent.getKillerPlayerName(), mapEvent.getKillerPlayerTeam(), mapEvent.getKillWeapon().getName(), mapEvent.getPlayerName(), mapEvent.getPlayerTeam());
      //feature.properties.put("popupContent", popupContent);
      deathFeatures.add(feature);
    }
    FeatureCollection deathFeatureCollection = new FeatureCollection(deathFeatures);

    Integer mapSize = mapSizesByMapCode.get(mapCode);
    String mapName = TranslationUtil.getMapName(mapCode);

    return new MapEvents()
        .setMapName(mapName)
        .setMapFileName(mapCode)
        .setMapSize(mapSize)
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
      return String.format("<span class='team-color-%d'>%s %s [%s] %s</span>", mapEvent.getKillerPlayerTeam(), time, killer, killWeaponName, victim);
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
                  .setName(TranslationUtil.getMapName(mapCode))
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
        .limit(10)
        .fetch();

    int totalMapsScore = records.stream()
        .map(r -> r.get(ROUND_END_STATS_PLAYER.SCORE, Integer.class))
        .reduce(0, Integer::sum);

    return records.stream().map(r -> toMapUsage(r, totalMapsScore)).collect(Collectors.toList());
  }

  private static MapUsage toMapUsage(Record r, int totalMapsScore) {
    String mapCode = r.get(ROUND.MAP_CODE);
    return new MapUsage()
        .setCode(mapCode)
        .setName(TranslationUtil.getMapName(mapCode))
        .setScore(r.get(ROUND_END_STATS_PLAYER.SCORE, Integer.class))
        .setPercentage(percentage(r.get(ROUND_END_STATS_PLAYER.SCORE, Integer.class), totalMapsScore))
        .setTimesUsed(r.get("times_used", Integer.class));
  }
}
