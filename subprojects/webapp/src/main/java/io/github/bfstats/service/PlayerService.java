package io.github.bfstats.service;

import io.github.bfstats.dbstats.jooq.tables.records.GamePlayerRecord;
import io.github.bfstats.dbstats.jooq.tables.records.PlayerNicknameRecord;
import io.github.bfstats.dbstats.jooq.tables.records.PlayerRecord;
import io.github.bfstats.exceptions.NotFoundException;
import io.github.bfstats.model.*;
import io.github.bfstats.util.TranslationUtil;
import org.jooq.*;
import org.jooq.impl.DSL;
import ro.pippo.core.util.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.github.bfstats.dbstats.jooq.Tables.*;
import static io.github.bfstats.util.DateTimeUtils.toUserZone;
import static io.github.bfstats.util.DbUtils.getDslContext;
import static io.github.bfstats.util.Utils.percentage;
import static java.util.stream.Collectors.toList;

public class PlayerService {

  private static final int BOT_PLAYER_ID = 1;
  private static final int LIMIT_PLAYER_STATS = 10;
  public static final DateTimeFormatter HM_FORMAT = DateTimeFormatter.ofPattern("H'h' mm'm'");

  public List<Player> getPlayers() {
    Result<PlayerRecord> records = getDslContext().selectFrom(PLAYER).fetch();
    return records.stream().map(PlayerService::toPlayer).collect(toList());
  }

  public List<Player> findPlayers(String partialName) {
    Result<PlayerRecord> records = getDslContext()
        .select(PLAYER.ID, PLAYER_NICKNAME.NICKNAME.as("name"), PLAYER.KEYHASH)
        .from(PLAYER)
        .innerJoin(PLAYER_NICKNAME).on(PLAYER_NICKNAME.PLAYER_ID.eq(PLAYER.ID))
        .where(PLAYER_NICKNAME.NICKNAME.likeIgnoreCase("%" + partialName + "%"))
        .fetchInto(PLAYER);

    return records.stream().map(PlayerService::toPlayer).collect(toList());
  }

  public Player getPlayer(int id) {
    PlayerRecord r = getDslContext().selectFrom(PLAYER).
        where(PLAYER.ID.eq(id)).fetchOne();

    if (r == null) {
      throw new NotFoundException("player with id " + id + " not found");
    }

    return toPlayer(r);
  }

  private static Player toPlayer(PlayerRecord r) {
    String keyhash = r.getKeyhash();
    String partialKeyHash = keyhash.substring(25);

    return new Player()
        .setId(r.getId())
        .setName(r.getName())
        .setKeyHash(keyhash)
        .setPartialKeyHash(partialKeyHash);
  }

  public Long fetchPlayerTotalTime(int playerId) {
    BigDecimal totalTimeInMs = getDslContext()
        .select(DSL.sum(DSL.timestampDiff(GAME_PLAYER.END_TIME, GAME_PLAYER.START_TIME)).as("totalTime"))
        .from(GAME_PLAYER)
        .where(GAME_PLAYER.PLAYER_ID.eq(playerId))
        .fetchOne("totalTime", BigDecimal.class);
    if (totalTimeInMs == null) {
      return null;
      // player is probably still playing and has no round player record yet
    }
    BigDecimal totalTimeInSeconds = totalTimeInMs.divide(BigDecimal.valueOf(1000), RoundingMode.DOWN);

    return totalTimeInSeconds.longValue();
  }

  // returns times in UTC
  public Map<LocalDateTime, Integer> fetchPlayersOnlineTimes(@Nonnull LocalDateTime since) {
    Timestamp sinceTimestamp = Timestamp.valueOf(since);

    Result<Record> records = getDslContext()
        .select()
        .select(GAME_PLAYER.START_TIME.as("time"), DSL.inline("start").as("start_or_end")).from(GAME_PLAYER).where(GAME_PLAYER.START_TIME.greaterThan(sinceTimestamp))
        .unionAll(getDslContext().select(GAME_PLAYER.END_TIME.as("time"), DSL.inline("end").as("start_or_end")).from(GAME_PLAYER).where(GAME_PLAYER.START_TIME.greaterThan(sinceTimestamp)))
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

  // returns times in UTC
  public Map<LocalDate, Integer> fetchUniquePlayerCountPerDay(@Nonnull LocalDateTime since) {
    Timestamp sinceTimestamp = Timestamp.valueOf(since);

    Table<Record2<Date, Integer>> playersPerDay = DSL.select(DSL.date(GAME_PLAYER.START_TIME).as("day"), GAME_PLAYER.PLAYER_ID)
        .from(GAME_PLAYER)
        .where(GAME_PLAYER.START_TIME.greaterThan(sinceTimestamp))
        .groupBy(DSL.date(GAME_PLAYER.START_TIME), GAME_PLAYER.PLAYER_ID)
        .asTable("a");

    Result<Record2<Object, Integer>> records = getDslContext()
        .select(DSL.field("day"), DSL.count().as("count"))
        .from(playersPerDay)
        .groupBy(playersPerDay.field("day"))
        .orderBy(DSL.field("day").asc())
        .fetch();

    Map<LocalDate, Integer> m = new HashMap<>();
    for (int i = 0; i < records.size(); i++) {
      Record2<Object, Integer> record = records.get(i);
      LocalDate date = record.get("day", Date.class).toLocalDate();
      Integer count = record.get("count", Integer.class);
      m.put(date, count);

      // if not first element
      if (i != 0) {
        Record2<Object, Integer> prevRecord = records.get(i - 1);
        LocalDate prevDate = prevRecord.get("day", Date.class).toLocalDate();
        LocalDate prevDay = date.minusDays(1);
        // if prev date is not the prev day, then it means prev day had 0 players, so insert such point in graph
        if (!prevDate.equals(prevDay)) {
          m.put(prevDay, 0);
        }
      }

      // if not last element
      if (i + 1 < records.size()) {
        Record2<Object, Integer> nextRecord = records.get(i + 1);
        LocalDate nextDate = nextRecord.get("day", Date.class).toLocalDate();
        LocalDate nextDay = date.plusDays(1);
        // if next date is not the next day, then it means next day had 0 players, so insert such point in graph
        if (!nextDate.equals(nextDay)) {
          m.put(nextDay, 0);
        }
      }

      // if last element and it's not today (meaning there are 0 players)
      if (i + 1 == records.size() && date.compareTo(LocalDate.now()) < 0) {
        LocalDate nextDay = date.plusDays(1);
        m.put(nextDay, 0);
        if (nextDay.compareTo(LocalDate.now()) < 0) {
          m.put(LocalDate.now(), 0);
        }
      }

    }

    return m;
  }

  public LocalDateTime getPlayerLastSeen(int playerId) {
    GamePlayerRecord gamePlayerRecord = getDslContext().selectFrom(GAME_PLAYER)
        .where(GAME_PLAYER.PLAYER_ID.eq(playerId))
        .orderBy(GAME_PLAYER.END_ROUND_ID.desc())
        .limit(1)
        .fetchOne();

    if (gamePlayerRecord == null) {
      return null;
    }

    return gamePlayerRecord.getEndTime().toLocalDateTime();
  }

  public PlayerDetails getPlayerDetails(int playerId) {
    Long totalTimeInSeconds = fetchPlayerTotalTime(playerId);
    LocalDateTime lastSeen = toUserZone(getPlayerLastSeen(playerId));

    return new PlayerDetails()
        .setTotalTimeInSeconds(totalTimeInSeconds)
        .setLastSeen(lastSeen);
  }

  public List<NicknameUsage> getNicknameUsages(int playerId) {
    Result<PlayerNicknameRecord> records = getDslContext().selectFrom(PLAYER_NICKNAME)
        .where(PLAYER_NICKNAME.PLAYER_ID.eq(playerId))
        .fetch();
    return records.stream().map(PlayerService::toNicknameUsage).collect(toList());
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
    }).collect(toList());
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
    ).collect(toList());
  }

  public List<WeaponUsage> getWeaponUsages(int playerId) {
    Result<Record3<String, String, Integer>> records = getDslContext().select(ROUND.GAME_CODE, ROUND_PLAYER_DEATH.KILL_WEAPON, DSL.count().as("times_used"))
        .from(ROUND_PLAYER_DEATH)
        .innerJoin(ROUND).on(ROUND.ID.eq(ROUND_PLAYER_DEATH.ROUND_ID))
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
        .collect(toList());
  }


  private static WeaponUsage toWeaponUsage(Record r, int totalTimesUsed) {
    Integer timesUsed = r.get("times_used", Integer.class);
    String gameCode = r.get(ROUND.GAME_CODE);
    String code = r.get(ROUND_PLAYER_DEATH.KILL_WEAPON);
    String weaponOrVehicleName = TranslationUtil.getWeaponOrVehicleName(gameCode, code);
    return new WeaponUsage()
        .setGameCode(gameCode)
        .setCode(code)
        .setName(weaponOrVehicleName)
        .setTimesUsed(timesUsed)
        .setPercentage(percentage(timesUsed, totalTimesUsed));
  }

  public List<KitUsage> getKitUsages(int playerId) {
    Result<Record3<String, String, Integer>> records = getDslContext().select(ROUND.GAME_CODE, ROUND_PLAYER_PICKUP_KIT.KIT, DSL.count().as("times_used"))
        .from(ROUND_PLAYER_PICKUP_KIT)
        .innerJoin(ROUND).on(ROUND.ID.eq(ROUND_PLAYER_PICKUP_KIT.ROUND_ID))
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
        .collect(toList());
  }

  private static KitUsage toKitUsage(Record r, int totalTimesUsed) {
    Integer timesUsed = r.get("times_used", Integer.class);
    String gameCode = r.get(ROUND.GAME_CODE);
    String code = r.get(ROUND_PLAYER_PICKUP_KIT.KIT);
    KitService.KitNameAndWeapons kitNameAndWeapons = KitService.findKitNameAndWeapons(gameCode, code);
    return new KitUsage()
        .setGameCode(gameCode)
        .setCode(code)
        .setName(kitNameAndWeapons.getName())
        .setWeapons(kitNameAndWeapons.getWeapons())
        .setTimesUsed(timesUsed)
        .setPercentage(percentage(timesUsed, totalTimesUsed));
  }

  public List<VehicleUsage> getVehicleUsages(int playerId) {
    Result<Record4<String, String, BigDecimal, Integer>> records = getDslContext()
        .select(
            ROUND.GAME_CODE,
            ROUND_PLAYER_VEHICLE.VEHICLE,
            DSL.sum(ROUND_PLAYER_VEHICLE.DURATION_SECONDS).as("total_duration"),
            DSL.count().as("times_used")
        )
        .from(ROUND_PLAYER_VEHICLE)
        .innerJoin(ROUND).on(ROUND.ID.eq(ROUND_PLAYER_VEHICLE.ROUND_ID))
        .where(ROUND_PLAYER_VEHICLE.PLAYER_ID.eq(playerId))
        .groupBy(ROUND_PLAYER_VEHICLE.VEHICLE)
        .orderBy(DSL.field("total_duration").desc())
        .limit(LIMIT_PLAYER_STATS)
        .fetch();

    int totalVehiclesDriveTimeInSeconds = records.stream()
        .map(r -> r.get("total_duration", Integer.class))
        .reduce(0, Integer::sum);

    return records.stream().map(r -> toVehicleUsage(r, totalVehiclesDriveTimeInSeconds))
        .collect(toList());
  }

  private static VehicleUsage toVehicleUsage(Record r, int totalVehiclesDriveTimeInSeconds) {
    int driveTime = r.get("total_duration", Integer.class);
    String code = r.get(ROUND_PLAYER_VEHICLE.VEHICLE);
    String gameCode = r.get(ROUND.GAME_CODE);
    String codeWithoutModifiers = withoutModifiers(code);
    if (codeWithoutModifiers.isEmpty()) {
      codeWithoutModifiers = code;
    }

    String vehicleName = TranslationUtil.getVehicleName(gameCode, codeWithoutModifiers);
    if (code.length() > codeWithoutModifiers.length()) {
      String modifierName = code.substring(codeWithoutModifiers.length());
      modifierName = removePCO(modifierName);
      modifierName = StringUtils.removeStart(modifierName, "_");
      modifierName = TranslationUtil.getVehicleModifier(modifierName);
      vehicleName += " " + modifierName;
    }

    return new VehicleUsage()
        .setGameCode(gameCode)
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

  private static LocalTime convertSecondsToLocalTime(int nSecondTime) {
    return LocalTime.MIN.plusSeconds(nSecondTime);
  }
}
