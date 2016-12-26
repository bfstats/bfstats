package io.github.bfvstats.service;


import io.github.bfvstats.Player;
import io.github.bfvstats.jpa.tables.records.SelectbfKillsWeaponRecord;
import io.github.bfvstats.jpa.tables.records.SelectbfNicknamesRecord;
import io.github.bfvstats.jpa.tables.records.SelectbfPlayersRecord;
import io.github.bfvstats.model.NicknameUsage;
import io.github.bfvstats.model.WeaponUsage;
import org.jooq.Result;

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
}