package io.github.bfvstats.service;

import io.github.bfvstats.game.jooq.tables.records.PlayerNicknameRecord;
import io.github.bfvstats.game.jooq.tables.records.PlayerRecord;
import io.github.bfvstats.game.jooq.tables.records.RoundPlayerRecord;
import io.github.bfvstats.model.*;
import io.github.bfvstats.util.TranslationUtil;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.impl.DSL;
import ro.pippo.core.util.StringUtils;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.bfvstats.game.jooq.Tables.*;
import static io.github.bfvstats.util.DbUtils.getDslContext;
import static io.github.bfvstats.util.Utils.percentage;

public class PlayerService {

  private static final int BOT_PLAYER_ID = 1;
  private static final int LIMIT_PLAYER_STATS = 10;
  public static final DateTimeFormatter HM_FORMAT = DateTimeFormatter.ofPattern("H'h' mm'm'");

  public List<Player> getPlayers() {
    Result<PlayerRecord> records = getDslContext().selectFrom(PLAYER).fetch();
    return records.stream().map(PlayerService::toPlayer).collect(Collectors.toList());
  }

  public List<Player> findPlayers(String partialName) {
    Result<PlayerRecord> records = getDslContext()
        .select(PLAYER.ID, PLAYER_NICKNAME.NICKNAME.as("name"), PLAYER.KEYHASH)
        .from(PLAYER)
        .leftJoin(PLAYER_NICKNAME).on(PLAYER_NICKNAME.PLAYER_ID.eq(PLAYER.ID))
        .where(PLAYER.NAME.likeIgnoreCase("%" + partialName + "%"))
        .or(PLAYER_NICKNAME.NICKNAME.likeIgnoreCase("%" + partialName + "%"))
        .fetchInto(PLAYER);

    return records.stream().map(PlayerService::toPlayer).collect(Collectors.toList());
  }

  public Player getPlayer(int id) {
    PlayerRecord r = getDslContext().selectFrom(PLAYER).
        where(PLAYER.ID.eq(id)).fetchOne();
    return toPlayer(r);
  }

  private static Player toPlayer(PlayerRecord r) {
    return new Player()
        .setId(r.getId())
        .setName(r.getName())
        .setKeyHash(r.getKeyhash());
  }

  public Long fetchPlayerTotalTime(int playerId) {
    BigDecimal totalTimeInMs = getDslContext()
        .select(DSL.sum(DSL.timestampDiff(ROUND_PLAYER.END_TIME, ROUND_PLAYER.START_TIME)).as("totalTime"))
        .from(ROUND_PLAYER)
        .where(ROUND_PLAYER.PLAYER_ID.eq(playerId))
        .fetchOne("totalTime", BigDecimal.class);
    if (totalTimeInMs == null) {
      return null;
      // player is probably still playing and has no round player record yet
    }
    BigDecimal totalTimeInSeconds = totalTimeInMs.divide(BigDecimal.valueOf(1000), RoundingMode.DOWN);

    return totalTimeInSeconds.longValue();
  }

  public Map<LocalDateTime, Integer> fetchPlayersOnlineTimes() {
    Result<Record> records = getDslContext()
        .select()
        .select(ROUND_PLAYER.START_TIME.as("time"), DSL.inline("start").as("start_or_end")).from(ROUND_PLAYER)
        .unionAll(getDslContext().select(ROUND_PLAYER.END_TIME.as("time"), DSL.inline("end").as("start_or_end")).from(ROUND_PLAYER))
        .orderBy(DSL.field("time"))
        .fetch();

    Map<LocalDateTime, Integer> usersAt = new LinkedHashMap<>();
    int concurrentPlayers = 0;
    for (Record record : records) {
      Timestamp time = record.get("time", Timestamp.class);
      String startOrEnd = record.get("start_or_end", String.class);
      int concurrentPlayersBefore = concurrentPlayers;
      if (startOrEnd.equals("start")) {
        concurrentPlayers++;
      } else {
        concurrentPlayers--;
      }
      usersAt.put(time.toLocalDateTime(), concurrentPlayersBefore);
      usersAt.put(time.toLocalDateTime().plusNanos(1000), concurrentPlayers);
    }

    return usersAt;
  }

  public LocalDateTime getPlayerLastSeen(int playerId) {
    RoundPlayerRecord roundPlayerRecord = getDslContext().selectFrom(ROUND_PLAYER)
        .where(ROUND_PLAYER.PLAYER_ID.eq(playerId))
        .orderBy(ROUND_PLAYER.END_ROUND_ID.desc())
        .limit(1)
        .fetchOne();

    if (roundPlayerRecord == null) {
      return null;
    }

    return roundPlayerRecord.getEndTime().toLocalDateTime();
  }

  public PlayerDetails getPlayerDetails(int playerId) {
    Long totalTimeInSeconds = fetchPlayerTotalTime(playerId);
    LocalDateTime lastSeen = getPlayerLastSeen(playerId);

    return new PlayerDetails()
        .setTotalTimeInSeconds(totalTimeInSeconds)
        .setLastSeen(lastSeen);
  }

  public List<NicknameUsage> getNicknameUsages(int playerId) {
    Result<PlayerNicknameRecord> records = getDslContext().selectFrom(PLAYER_NICKNAME)
        .where(PLAYER_NICKNAME.PLAYER_ID.eq(playerId))
        .fetch();
    return records.stream().map(PlayerService::toNicknameUsage).collect(Collectors.toList());
  }

  private static NicknameUsage toNicknameUsage(PlayerNicknameRecord r) {
    return new NicknameUsage()
        .setName(r.getNickname())
        .setTimesUsed(r.getTimesUsed());
  }

  public List<PlayerAndTotal> getKillsByVictims(@Nullable Integer playerId) {
    Result<Record3<Integer, Integer, String>> killRecords = getDslContext()
        .select(DSL.count().as("kill_count"), ROUND_PLAYER_DEATH.PLAYER_ID, PLAYER.NAME)
        .from(ROUND_PLAYER_DEATH)
        .join(PLAYER).on(PLAYER.ID.eq(ROUND_PLAYER_DEATH.PLAYER_ID))
        .where(ROUND_PLAYER_DEATH.KILLER_PLAYER_ID.eq(playerId))
        .and(ROUND_PLAYER_DEATH.PLAYER_ID.notEqual(BOT_PLAYER_ID))
        .groupBy(ROUND_PLAYER_DEATH.PLAYER_ID)
        .orderBy(DSL.count().desc())
        .limit(LIMIT_PLAYER_STATS)
        .fetch();

    Integer totalKillCount = killRecords.stream()
        .map(r -> r.get("kill_count", Integer.class))
        .reduce(0, Integer::sum);

    return killRecords.stream().map(r -> {
      Integer killCount = r.get("kill_count", Integer.class);
      return new PlayerAndTotal()
          .setPlayerId(r.get(ROUND_PLAYER_DEATH.PLAYER_ID))
          .setPlayerName(r.get(PLAYER.NAME))
          .setTotal(killCount)
          .setPercentage(percentage(killCount, totalKillCount));
    }).collect(Collectors.toList());
  }

  public List<PlayerAndTotal> getDeathsByKillers(@Nullable Integer playerId) {
    Result<Record3<Integer, Integer, String>> deathRecords = getDslContext()
        .select(DSL.count().as("death_count"), ROUND_PLAYER_DEATH.KILLER_PLAYER_ID, PLAYER.NAME)
        .from(ROUND_PLAYER_DEATH)
        .join(PLAYER).on(PLAYER.ID.eq(ROUND_PLAYER_DEATH.KILLER_PLAYER_ID))
        .where(ROUND_PLAYER_DEATH.PLAYER_ID.eq(playerId))
        .and(ROUND_PLAYER_DEATH.KILLER_PLAYER_ID.isNotNull())
        .and(ROUND_PLAYER_DEATH.KILLER_PLAYER_ID.notEqual(BOT_PLAYER_ID))
        .groupBy(ROUND_PLAYER_DEATH.KILLER_PLAYER_ID)
        .orderBy(DSL.count().desc())
        .limit(LIMIT_PLAYER_STATS)
        .fetch();

    Integer totalDeathCount = deathRecords.stream()
        .map(r -> r.get("death_count", Integer.class))
        .reduce(0, Integer::sum);

    return deathRecords.stream().map(r -> {
          Integer deathCount = r.get("death_count", Integer.class);
          return new PlayerAndTotal()
              .setPlayerId(r.get(ROUND_PLAYER_DEATH.KILLER_PLAYER_ID))
              .setPlayerName(r.get(PLAYER.NAME))
              .setTotal(deathCount)
              .setPercentage(percentage(deathCount, totalDeathCount));
        }
    ).collect(Collectors.toList());
  }

  public List<WeaponUsage> getWeaponUsages(int playerId) {
    Result<Record2<String, Integer>> records = getDslContext().select(ROUND_PLAYER_DEATH.KILL_WEAPON, DSL.count().as("times_used"))
        .from(ROUND_PLAYER_DEATH)
        .where(ROUND_PLAYER_DEATH.KILL_WEAPON.isNotNull())
        .and(ROUND_PLAYER_DEATH.KILLER_PLAYER_ID.eq(playerId))
        .groupBy(ROUND_PLAYER_DEATH.KILL_WEAPON)
        .orderBy(DSL.count().desc())
        .limit(LIMIT_PLAYER_STATS)
        .fetch();

    Integer totalTimesUsed = records.stream()
        .map(r -> r.get("times_used", Integer.class))
        .reduce(0, Integer::sum);

    return records.stream()
        .map(r -> toWeaponUsage(r, totalTimesUsed))
        .collect(Collectors.toList());
  }


  private static WeaponUsage toWeaponUsage(Record r, int totalTimesUsed) {
    Integer timesUsed = r.get("times_used", Integer.class);
    String code = r.get(ROUND_PLAYER_DEATH.KILL_WEAPON);
    String weaponOrVehicleName = TranslationUtil.getWeaponOrVehicleName(code);
    return new WeaponUsage()
        .setCode(code)
        .setName(weaponOrVehicleName)
        .setTimesUsed(timesUsed)
        .setPercentage(percentage(timesUsed, totalTimesUsed));
  }

  public List<KitUsage> getKitUsages(int playerId) {
    Result<Record2<String, Integer>> records = getDslContext().select(ROUND_PLAYER_PICKUP_KIT.KIT, DSL.count().as("times_used"))
        .from(ROUND_PLAYER_PICKUP_KIT)
        .where(ROUND_PLAYER_PICKUP_KIT.PLAYER_ID.eq(playerId))
        .groupBy(ROUND_PLAYER_PICKUP_KIT.KIT)
        .orderBy(DSL.count().desc())
        .limit(LIMIT_PLAYER_STATS)
        .fetch();

    Integer totalTimesUsed = records.stream()
        .map(r -> r.get("times_used", Integer.class))
        .reduce(0, Integer::sum);

    return records.stream()
        .map(r -> toKitUsage(r, totalTimesUsed))
        .collect(Collectors.toList());
  }

  private static KitUsage toKitUsage(Record r, int totalTimesUsed) {
    Integer timesUsed = r.get("times_used", Integer.class);
    String code = r.get(ROUND_PLAYER_PICKUP_KIT.KIT);
    return new KitUsage()
        .setCode(code)
        .setName(KitService.kitName(code))
        .setTimesUsed(timesUsed)
        .setPercentage(percentage(timesUsed, totalTimesUsed));
  }

  public List<VehicleUsage> getVehicleUsages(int playerId) {
    Result<Record3<String, BigDecimal, Integer>> records = getDslContext()
        .select(
            ROUND_PLAYER_VEHICLE.VEHICLE,
            DSL.sum(ROUND_PLAYER_VEHICLE.DURATION_SECONDS).as("total_duration"),
            DSL.count().as("times_used")
        )
        .from(ROUND_PLAYER_VEHICLE)
        .where(ROUND_PLAYER_VEHICLE.PLAYER_ID.eq(playerId))
        .groupBy(ROUND_PLAYER_VEHICLE.VEHICLE)
        .orderBy(DSL.field("total_duration").desc())
        .limit(LIMIT_PLAYER_STATS)
        .fetch();

    int totalVehiclesDriveTimeInSeconds = records.stream()
        .map(r -> r.get("total_duration", Integer.class))
        .reduce(0, Integer::sum);

    return records.stream().map(r -> toVehicleUsage(r, totalVehiclesDriveTimeInSeconds))
        .collect(Collectors.toList());
  }

  private static VehicleUsage toVehicleUsage(Record r, int totalVehiclesDriveTimeInSeconds) {
    int driveTime = r.get("total_duration", Integer.class);
    String code = r.get(ROUND_PLAYER_VEHICLE.VEHICLE);
    String codeWithoutModifiers = withoutModifiers(code);
    if (codeWithoutModifiers.isEmpty()) {
      codeWithoutModifiers = code;
    }

    String vehicleName = TranslationUtil.getVehicleName(codeWithoutModifiers);
    if (code.length() > codeWithoutModifiers.length()) {
      String modifierName = code.substring(codeWithoutModifiers.length());
      modifierName = removePCO(modifierName);
      modifierName = StringUtils.removeStart(modifierName, "_");
      vehicleName += " " + modifierName;
    }

    return new VehicleUsage()
        .setCode(code)
        .setName(vehicleName)
        .setDriveTime(convertSecondsToLocalTime(driveTime).format(HM_FORMAT)) // seconds
        .setPercentage(percentage(driveTime, totalVehiclesDriveTimeInSeconds))
        .setTimesUsed(r.get("times_used", Integer.class));
  }

  private static String removePCO(String code) {
    code = StringUtils.removeEnd(code, "PCO6");
    code = StringUtils.removeEnd(code, "PCO5");
    code = StringUtils.removeEnd(code, "PCO4");
    code = StringUtils.removeEnd(code, "PCO3");
    code = StringUtils.removeEnd(code, "PCO2");
    code = StringUtils.removeEnd(code, "PCO1");
    code = StringUtils.removeEnd(code, "PCO");

    code = StringUtils.removeEnd(code, "_");
    return code;
  }

  private static String withoutModifiers(String code) {
    code = removePCO(code);

    code = StringUtils.removeEnd(code, "Left");
    code = StringUtils.removeEnd(code, "Right");

    code = StringUtils.removeEnd(code, "Funner");
    code = StringUtils.removeEnd(code, "RearGunner");
    code = StringUtils.removeEnd(code, "Gunner");
    code = StringUtils.removeEnd(code, "ArmedPassenger");
    code = StringUtils.removeEnd(code, "Passenger2");
    code = StringUtils.removeEnd(code, "Passenger");
    code = StringUtils.removeEnd(code, "CoPilot");
    code = StringUtils.removeEnd(code, "MG"); // Machine gun
    code = StringUtils.removeEnd(code, "TOW"); // Tube-launched, Optically tracked, Wire-guided; weapon of MuttTOW = BGM-71 TOW

    code = StringUtils.removeEnd(code, "_");

    return code;
  }

  private static String convertSecondToHHMMSSString(int nSecondTime) {
    return convertSecondsToLocalTime(nSecondTime).toString();
  }

  private static LocalTime convertSecondsToLocalTime(int nSecondTime) {
    return LocalTime.MIN.plusSeconds(nSecondTime);
  }
}