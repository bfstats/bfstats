package io.github.bfvstats.service;

import io.github.bfvstats.Player;
import io.github.bfvstats.game.jooq.tables.records.PlayerRecord;
import io.github.bfvstats.jpa.tables.records.SelectbfDrivesRecord;
import io.github.bfvstats.jpa.tables.records.SelectbfKillsWeaponRecord;
import io.github.bfvstats.jpa.tables.records.SelectbfNicknamesRecord;
import io.github.bfvstats.model.NicknameUsage;
import io.github.bfvstats.model.VehicleUsage;
import io.github.bfvstats.model.WeaponUsage;
import org.jooq.Result;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.bfvstats.game.jooq.Tables.PLAYER;
import static io.github.bfvstats.jpa.Tables.*;
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

}