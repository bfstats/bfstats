package io.github.bfvstats.service;


import io.github.bfvstats.Player;
import io.github.bfvstats.jpa.tables.records.SelectbfDrivesRecord;
import io.github.bfvstats.jpa.tables.records.SelectbfKillsWeaponRecord;
import io.github.bfvstats.jpa.tables.records.SelectbfNicknamesRecord;
import io.github.bfvstats.jpa.tables.records.SelectbfPlayersRecord;
import io.github.bfvstats.model.MapUsage;
import io.github.bfvstats.model.NicknameUsage;
import io.github.bfvstats.model.VehicleUsage;
import io.github.bfvstats.model.WeaponUsage;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.bfvstats.jpa.Tables.*;
import static io.github.bfvstats.util.DbUtils.getDslContext;

public class PlayerService {
  public List<Player> getPlayers() {
    Result<SelectbfPlayersRecord> records = getDslContext().selectFrom(SELECTBF_PLAYERS).fetch();
    return records.stream().map(PlayerService::toPlayer).collect(Collectors.toList());
  }

  public Player getPlayer(int id) {
    SelectbfPlayersRecord r = getDslContext().selectFrom(SELECTBF_PLAYERS).
        where(SELECTBF_PLAYERS.ID.eq(id)).fetchOne();
    return toPlayer(r);
  }

  private static Player toPlayer(SelectbfPlayersRecord r) {
    return new Player()
        .setId(r.getId())
        .setName(r.getName())
        .setKeyHash(r.getKeyhash());
  }

  public List<NicknameUsage> getNicknameUsages(int playerId) {
    Result<SelectbfNicknamesRecord> records = getDslContext().selectFrom(SELECTBF_NICKNAMES)
        .where(SELECTBF_NICKNAMES.PLAYER_ID.eq(playerId))
        .fetch();
    return records.stream().map(PlayerService::toNicknameUsage).collect(Collectors.toList());
  }

  private static NicknameUsage toNicknameUsage(SelectbfNicknamesRecord r) {
    return new NicknameUsage()
        .setName(r.getNickname())
        .setTimesUsed(r.getTimesUsed());
  }

  public List<WeaponUsage> getWeaponUsages(int playerId) {
    Result<SelectbfKillsWeaponRecord> records = getDslContext().selectFrom(SELECTBF_KILLS_WEAPON)
        .where(SELECTBF_KILLS_WEAPON.PLAYER_ID.eq(playerId))
        .orderBy(SELECTBF_KILLS_WEAPON.TIMES_USED.desc())
        .fetch();

    Integer totalWeaponsTimesUsed = records.stream().map(SelectbfKillsWeaponRecord::getTimesUsed).reduce(0, Integer::sum);
    return records.stream().map(r -> toWeaponUsage(r, totalWeaponsTimesUsed)).collect(Collectors.toList());
  }

  private static WeaponUsage toWeaponUsage(SelectbfKillsWeaponRecord r, int totalWeaponsTimesUsed) {
    return new WeaponUsage()
        .setName(r.getWeapon())
        .setTimesUsed(r.getTimesUsed())
        .setPercentage(r.getTimesUsed() * 100 / totalWeaponsTimesUsed);
  }

  public List<VehicleUsage> getVehicleUsages(int playerId) {
    Result<SelectbfDrivesRecord> records = getDslContext().selectFrom(SELECTBF_DRIVES)
        .where(SELECTBF_DRIVES.PLAYER_ID.eq(playerId))
        .orderBy(SELECTBF_DRIVES.DRIVETIME.desc())
        .fetch();

    float totalVehiclesDriveTime = records.stream().map(SelectbfDrivesRecord::getDrivetime).reduce(0f, Float::sum);
    return records.stream().map(r -> toVehicleUsage(r, totalVehiclesDriveTime)).collect(Collectors.toList());
  }

  private static VehicleUsage toVehicleUsage(SelectbfDrivesRecord r, float totalVehiclesDriveTime) {
    return new VehicleUsage()
        .setName(r.getVehicle().toString())
        .setDriveTime(convertSecondToHHMMSSString(r.getDrivetime().intValue())) // seconds
        .setTimesUsed(r.getTimesUsed())
        .setPercentage(r.getDrivetime() * 100 / totalVehiclesDriveTime);
  }

  private static String convertSecondToHHMMSSString(int nSecondTime) {
    return LocalTime.MIN.plusSeconds(nSecondTime).toString();
  }

  public List<MapUsage> getMapUsages(int playerId) {
    Result<Record2<BigDecimal, String>> records = getDslContext().select(DSL.sum(SELECTBF_PLAYERSTATS.SCORE).as("score"),
        SELECTBF_GAMES.MAP.cast(String.class).as("map"))
        .from(SELECTBF_PLAYERSTATS)
        .join(SELECTBF_ROUNDS).on(SELECTBF_ROUNDS.ID.eq(SELECTBF_PLAYERSTATS.ROUND_ID))
        .join(SELECTBF_GAMES).on(SELECTBF_GAMES.ID.eq(SELECTBF_ROUNDS.GAME_ID))
        .where(SELECTBF_PLAYERSTATS.PLAYER_ID.eq(playerId))
        .groupBy(SELECTBF_GAMES.MAP)
        .having(SELECTBF_PLAYERSTATS.SCORE.notEqual(0))
        .orderBy(SELECTBF_PLAYERSTATS.SCORE.desc())
        .fetch();

    float totalMapsScore = records.stream().map(r -> r.get("score", Integer.class)).reduce(0, Integer::sum);
    return records.stream().map(r -> toMapUsage(r, totalMapsScore)).collect(Collectors.toList());
  }

  private static MapUsage toMapUsage(Record r, float totalMapsScore) {
    return new MapUsage()
        .setName(r.get("map", String.class))
        .setScore(r.get("score", Integer.class))
        .setPercentage(r.get("score", Integer.class) * 100 / totalMapsScore);
  }
}