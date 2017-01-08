package io.github.bfvstats.service;

import io.github.bfvstats.Player;
import io.github.bfvstats.game.jooq.tables.records.PlayerNicknameRecord;
import io.github.bfvstats.game.jooq.tables.records.PlayerRecord;
import io.github.bfvstats.model.KitUsage;
import io.github.bfvstats.model.NicknameUsage;
import io.github.bfvstats.model.VehicleUsage;
import io.github.bfvstats.model.WeaponUsage;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.bfvstats.game.jooq.Tables.*;
import static io.github.bfvstats.util.DbUtils.getDslContext;

public class PlayerService {
  public List<Player> getPlayers() {
    Result<PlayerRecord> records = getDslContext().selectFrom(PLAYER).fetch();
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

  public List<NicknameUsage> getNicknameUsages(int playerId) {
    Result<PlayerNicknameRecord> records = getDslContext().selectFrom(PLAYER_NICKNAME)
        .where(PLAYER_NICKNAME.PLAYER_ID.eq(playerId))
        .fetch();
    return records.stream().map(PlayerService::toNicknameUsage).collect(Collectors.toList());
  }

  private static NicknameUsage toNicknameUsage(PlayerNicknameRecord r) {
    return new NicknameUsage()
        .setName(r.getNickname())
        .setTimesUsed(0);
  }

  public List<WeaponUsage> getWeaponUsages(int playerId) {
    Result<Record2<String, Integer>> records = getDslContext().select(ROUND_PLAYER_DEATH.KILL_WEAPON, DSL.count().as("times_used"))
        .from(ROUND_PLAYER_DEATH)
        .where(ROUND_PLAYER_DEATH.KILL_WEAPON.isNotNull())
        .and(ROUND_PLAYER_DEATH.KILLER_PLAYER_ID.eq(playerId))
        .groupBy(ROUND_PLAYER_DEATH.KILL_WEAPON)
        .orderBy(DSL.count().desc())
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
    return new WeaponUsage()
        .setName(r.get(ROUND_PLAYER_DEATH.KILL_WEAPON))
        .setTimesUsed(timesUsed)
        .setPercentage(timesUsed * 100 / totalTimesUsed);
  }

  public List<KitUsage> getKitUsages(int playerId) {
    Result<Record2<String, Integer>> records = getDslContext().select(ROUND_PLAYER_PICKUP_KIT.KIT, DSL.count().as("times_used"))
        .from(ROUND_PLAYER_PICKUP_KIT)
        .where(ROUND_PLAYER_PICKUP_KIT.PLAYER_ID.eq(playerId))
        .groupBy(ROUND_PLAYER_PICKUP_KIT.KIT)
        .orderBy(DSL.count().desc())
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
    return new KitUsage()
        .setName(r.get(ROUND_PLAYER_PICKUP_KIT.KIT))
        .setTimesUsed(timesUsed)
        .setPercentage(timesUsed * 100 / totalTimesUsed);
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
        .fetch();

    int totalVehiclesDriveTimeInSeconds = records.stream()
        .map(r -> r.get("total_duration", Integer.class))
        .reduce(0, Integer::sum);

    return records.stream().map(r -> toVehicleUsage(r, totalVehiclesDriveTimeInSeconds))
        .collect(Collectors.toList());
  }

  private static VehicleUsage toVehicleUsage(Record r, float totalVehiclesDriveTimeInSeconds) {
    int driveTime = r.get("total_duration", Integer.class);
    return new VehicleUsage()
        .setName(r.get(ROUND_PLAYER_VEHICLE.VEHICLE))
        .setDriveTime(convertSecondToHHMMSSString(driveTime)) // seconds
        .setPercentage(driveTime * 100 / totalVehiclesDriveTimeInSeconds)
        .setTimesUsed(r.get("times_used", Integer.class));
  }

  private static String convertSecondToHHMMSSString(int nSecondTime) {
    return LocalTime.MIN.plusSeconds(nSecondTime).toString();
  }

}