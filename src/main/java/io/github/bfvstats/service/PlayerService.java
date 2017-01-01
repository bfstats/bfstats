package io.github.bfvstats.service;

import io.github.bfvstats.Player;
import io.github.bfvstats.game.jooq.tables.records.PlayerNicknameRecord;
import io.github.bfvstats.game.jooq.tables.records.PlayerRecord;
import io.github.bfvstats.logparser.xml.enums.event.ScoreType;
import io.github.bfvstats.model.NicknameUsage;
import io.github.bfvstats.model.VehicleUsage;
import io.github.bfvstats.model.WeaponUsage;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SelectHavingStep;
import org.jooq.impl.DSL;

import java.util.ArrayList;
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
    SelectHavingStep<Record2<String, Integer>> records = getDslContext().select(ROUND_PLAYER_SCORE_EVENT.WEAPON, DSL.count().as("times_used"))
        .from(ROUND_PLAYER_SCORE_EVENT)
        .where(ROUND_PLAYER_SCORE_EVENT.SCORE_TYPE.eq(ScoreType.Kill.name()))
        .and(ROUND_PLAYER_SCORE_EVENT.WEAPON.isNotNull())
        .and(ROUND_PLAYER_SCORE_EVENT.PLAYER_ID.eq(playerId))
        .groupBy(ROUND_PLAYER_SCORE_EVENT.WEAPON);


    Integer totalWeaponsTimesUsed = records.stream()
        .map(r -> r.get("times_used", Integer.class))
        .reduce(0, Integer::sum);

    return records.stream()
        .map(r -> toWeaponUsage(r, totalWeaponsTimesUsed))
        .collect(Collectors.toList());
  }

  private static WeaponUsage toWeaponUsage(Record r, int totalWeaponsTimesUsed) {
    Integer timesUsed = r.get("times_used", Integer.class);
    return new WeaponUsage()
        .setName(r.get(ROUND_PLAYER_SCORE_EVENT.WEAPON))
        .setTimesUsed(timesUsed)
        .setPercentage(timesUsed * 100 / totalWeaponsTimesUsed);
  }

  public List<VehicleUsage> getVehicleUsages(int playerId) {
    return new ArrayList<>();
  }
    /*
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
  }*/

}