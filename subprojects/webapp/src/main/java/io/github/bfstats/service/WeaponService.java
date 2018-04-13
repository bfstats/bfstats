package io.github.bfstats.service;

import io.github.bfstats.model.WeaponUsage;
import io.github.bfstats.util.TranslationUtil;
import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.util.ArrayList;
import java.util.List;

import static io.github.bfstats.dbstats.jooq.Tables.ROUND;
import static io.github.bfstats.dbstats.jooq.Tables.ROUND_PLAYER_DEATH;
import static io.github.bfstats.service.PlayerService.LIMIT_PLAYER_STATS;
import static io.github.bfstats.util.DbUtils.getDslContext;
import static io.github.bfstats.util.Utils.percentage;
import static java.util.stream.Collectors.toList;

public class WeaponService {
  public WeaponUsage getWeapon(String gameCode, String weaponCode) {
    return new WeaponUsage()
        .setGameCode(gameCode)
        .setCode(weaponCode)
        .setName(TranslationUtil.getWeaponName(gameCode, weaponCode));
  }

  public List<WeaponUsage> getWeaponUsages() {
    return new ArrayList<>();
  }

  public List<WeaponUsage> getWeaponUsagesForPlayer(int playerId) {
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

  public List<WeaponUsage> getKilledByWeaponsForPlayer(int playerId) {
    Result<Record3<String, String, Integer>> records = getDslContext().select(ROUND.GAME_CODE, ROUND_PLAYER_DEATH.KILL_WEAPON, DSL.count().as("times_used"))
        .from(ROUND_PLAYER_DEATH)
        .innerJoin(ROUND).on(ROUND.ID.eq(ROUND_PLAYER_DEATH.ROUND_ID))
        .where(ROUND_PLAYER_DEATH.KILL_WEAPON.isNotNull())
        .and(ROUND_PLAYER_DEATH.PLAYER_ID.eq(playerId))
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
}
