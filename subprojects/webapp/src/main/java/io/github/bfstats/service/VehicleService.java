package io.github.bfstats.service;

import io.github.bfstats.model.VehicleUsage;
import io.github.bfstats.util.TranslationUtil;
import org.jooq.Record;
import org.jooq.Record4;
import org.jooq.Result;
import org.jooq.impl.DSL;
import ro.pippo.core.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

import static io.github.bfstats.dbstats.jooq.Tables.ROUND;
import static io.github.bfstats.dbstats.jooq.Tables.ROUND_PLAYER_VEHICLE;
import static io.github.bfstats.service.PlayerService.HM_FORMAT;
import static io.github.bfstats.service.PlayerService.LIMIT_PLAYER_STATS;
import static io.github.bfstats.util.DbUtils.getDslContext;
import static io.github.bfstats.util.Utils.percentage;
import static java.util.stream.Collectors.toList;

public class VehicleService {

  public VehicleUsage getVehicle(String gameCode, String vehicleCode) {
    return new VehicleUsage()
        .setGameCode(gameCode)
        .setCode(vehicleCode)
        .setName(TranslationUtil.getVehicleName(gameCode, vehicleCode));
  }

  public List<VehicleUsage> getVehicleUsages() {
    Result<Record4<String, String, BigDecimal, Integer>> records = getDslContext()
        .select(
            ROUND.GAME_CODE,
            ROUND_PLAYER_VEHICLE.VEHICLE,
            DSL.sum(ROUND_PLAYER_VEHICLE.DURATION_SECONDS).as("total_duration"),
            DSL.count().as("times_used")
        )
        .from(ROUND_PLAYER_VEHICLE)
        .innerJoin(ROUND).on(ROUND.ID.eq(ROUND_PLAYER_VEHICLE.ROUND_ID))
        .groupBy(ROUND_PLAYER_VEHICLE.VEHICLE)
        .orderBy(DSL.field("total_duration").desc())
        .fetch();

    int totalVehiclesDriveTimeInSeconds = records.stream()
        .map(r -> r.get("total_duration", Integer.class))
        .reduce(0, Integer::sum);

    return records.stream().map(r -> toVehicleUsage(r, totalVehiclesDriveTimeInSeconds))
        .collect(toList());
  }

  public List<VehicleUsage> getVehicleUsagesForPlayer(int playerId) {
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
