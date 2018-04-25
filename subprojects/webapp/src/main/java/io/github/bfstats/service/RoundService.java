package io.github.bfstats.service;

import io.github.bfstats.dbstats.jooq.tables.RoundPlayerTeam;
import io.github.bfstats.dbstats.jooq.tables.records.RoundEndStatsPlayerRecord;
import io.github.bfstats.dbstats.jooq.tables.records.RoundEndStatsRecord;
import io.github.bfstats.dbstats.jooq.tables.records.RoundPlayerDeathRecord;
import io.github.bfstats.dbstats.jooq.tables.records.RoundRecord;
import io.github.bfstats.exceptions.NotFoundException;
import io.github.bfstats.model.*;
import io.github.bfstats.util.TranslationUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.TableField;
import org.jooq.impl.DSL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.github.bfstats.dbstats.jooq.Tables.*;
import static io.github.bfstats.util.DateTimeUtils.toUserZone;
import static io.github.bfstats.util.DbUtils.getDslContext;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.jooq.impl.DSL.trueCondition;

public class RoundService {
  private static final io.github.bfstats.dbstats.jooq.tables.Player KILLER_PLAYER_TABLE = PLAYER.as("killerPlayer");
  private static final RoundPlayerTeam KILLER_PLAYER_TEAM_TABLE = ROUND_PLAYER_TEAM.as("killerPlayerTeam");
  private static final String KILL = "Kill";
  private static final String TK = "TK";

  public List<Round> getActiveRounds(int page) {
    Map<RoundRecord, RoundEndStatsRecord> roundWithStats = getActiveRoundRecordsWithStats(page);

    return roundWithStats.entrySet().stream()
        .map(e -> toRound(e.getKey(), e.getValue()))
        .collect(toList());
  }

  private static Map<RoundRecord, RoundEndStatsRecord> getActiveRoundRecordsWithStats(int page) {
    int numberOfRows = 50;
    int firstRowIndex = (page - 1) * numberOfRows;

    return getDslContext()
        .select(ROUND.fields())
        .select(ROUND_END_STATS.fields())
        .from(ROUND)
        .leftJoin(ROUND_END_STATS).on(ROUND_END_STATS.ROUND_ID.eq(ROUND.ID))
        .where(DSL.exists(getDslContext().selectOne().from(ROUND_PLAYER_DEATH).where(ROUND_PLAYER_DEATH.ROUND_ID.eq(ROUND.ID))))
        .orderBy(ROUND.START_TIME.desc())
        .limit(firstRowIndex, numberOfRows)
        .fetchMap(
            r -> r.into(ROUND),
            r -> r.into(ROUND_END_STATS)
        );
  }

  public List<Round> getRoundsByGameId(int gameId) {
    Map<RoundRecord, RoundEndStatsRecord> roundWithStats = getRoundRecordsWithStatsByGameId(gameId);

    return roundWithStats.entrySet().stream()
        .map(e -> toRound(e.getKey(), e.getValue()))
        .collect(toList());
  }

  private static Map<RoundRecord, RoundEndStatsRecord> getRoundRecordsWithStatsByGameId(int gameId) {
    return getDslContext()
        .select(ROUND.fields())
        .select(ROUND_END_STATS.fields())
        .from(ROUND)
        .leftJoin(ROUND_END_STATS).on(ROUND_END_STATS.ROUND_ID.eq(ROUND.ID))
        .where(ROUND.GAME_ID.eq(gameId))
        .orderBy(ROUND.START_TIME.asc())
        .fetchMap(
            r -> r.into(ROUND),
            r -> r.into(ROUND_END_STATS)
        );
  }

  public List<Round> getRounds(int page) {
    Map<RoundRecord, RoundEndStatsRecord> roundWithStats = getRoundRecordsWithStats(null, page);

    return roundWithStats.entrySet().stream()
        .map(e -> toRound(e.getKey(), e.getValue()))
        .collect(toList());
  }

  public Round getRound(int roundId) {
    Map<RoundRecord, RoundEndStatsRecord> roundWithStats = getRoundRecordsWithStats(roundId, 1);
    if (roundWithStats.isEmpty()) {
      throw new NotFoundException("round with id " + roundId + " not found");
    }
    Map.Entry<RoundRecord, RoundEndStatsRecord> onlyEntry = roundWithStats.entrySet().iterator().next();
    RoundRecord roundRecord = onlyEntry.getKey();
    Round round = toRound(roundRecord, onlyEntry.getValue());
    round.setServerSettings(toServerSettings(roundRecord));
    return round;
  }

  private static Map<RoundRecord, RoundEndStatsRecord> getRoundRecordsWithStats(@Nullable Integer roundId, int page) {
    int numberOfRows = 50;
    int firstRowIndex = (page - 1) * numberOfRows;

    return getDslContext()
        .select(ROUND.fields())
        .select(ROUND_END_STATS.fields())
        .from(ROUND)
        .leftJoin(ROUND_END_STATS).on(ROUND_END_STATS.ROUND_ID.eq(ROUND.ID))
        .where(roundId == null ? DSL.trueCondition() : ROUND.ID.eq(roundId))
        .orderBy(ROUND.START_TIME.desc())
        .limit(firstRowIndex, numberOfRows)
        .fetchMap(
            r -> r.into(ROUND),
            r -> r.into(ROUND_END_STATS)
        );
  }

  public List<Round> getRoundsForPlayer(int playerId) {
    Map<RoundRecord, RoundEndStatsRecord> roundWithStats = getRoundRecordsWithStatsForPlayer(playerId);

    return roundWithStats.entrySet().stream()
        .map(e -> toRound(e.getKey(), e.getValue()))
        .collect(toList());
  }

  private static Map<RoundRecord, RoundEndStatsRecord> getRoundRecordsWithStatsForPlayer(int playerId) {
    int numberOfRows = 10;
    int firstRowIndex = 0;

    return getDslContext()
        .select(ROUND.fields())
        .select(ROUND_END_STATS.fields())
        .from(ROUND)
        .leftJoin(ROUND_END_STATS).on(ROUND_END_STATS.ROUND_ID.eq(ROUND.ID))
        .where(ROUND.ID.in(getDslContext().selectDistinct(ROUND_PLAYER_TEAM.ROUND_ID).from(ROUND_PLAYER_TEAM).where(ROUND_PLAYER_TEAM.PLAYER_ID.eq(playerId))))
        .orderBy(ROUND.START_TIME.desc())
        .limit(firstRowIndex, numberOfRows)
        .fetchMap(
            r -> r.into(ROUND),
            r -> r.into(ROUND_END_STATS)
        );
  }

  private static ServerSettings toServerSettings(RoundRecord roundRecord) {
    return new ServerSettings()
        .setServerName(roundRecord.getServerName())
        .setServerPort(roundRecord.getServerPort())
        .setGameCode(roundRecord.getGameCode())
        .setModId(roundRecord.getModId())
        .setGameModeCode(roundRecord.getGameMode())
        .setGameModeName(TranslationUtil.getModeName(roundRecord.getGameCode(), roundRecord.getGameMode()))
        .setMaxGameTime(roundRecord.getMaxGameTime())
        .setMaxPlayers(roundRecord.getMaxPlayers())
        .setScoreLimit(roundRecord.getScoreLimit())
        .setNumberOfRoundsPerMap(roundRecord.getNoOfRounds())
        .setSpawnTime(roundRecord.getSpawnTime())
        .setSpawnDelay(roundRecord.getSpawnDelay())
        .setGameStartDelay(roundRecord.getGameStartDelay())
        .setRoundStartDelay(roundRecord.getRoundStartDelay())
        .setSoldierFriendlyFire(roundRecord.getSoldierFf())
        .setVehicleFriendlyFire(roundRecord.getVehicleFf())
        .setTicketRatio(roundRecord.getTicketRatio())
        .setTeamKillPunish(roundRecord.getTeamKillPunish() == 1)
        .setPunkbusterEnabled(roundRecord.getPunkbusterEnabled() == 1);
  }

  private static Round toRound(@Nonnull RoundRecord roundRecord, @Nonnull RoundEndStatsRecord roundEndStatsRecord) {
    String gameCode = roundRecord.getGameCode();
    String mapCode = roundRecord.getMapCode();
    String mapName = TranslationUtil.getMapName(gameCode, mapCode);
    String modeName = TranslationUtil.getModeName(gameCode, roundRecord.getGameMode());

    LocalDateTime startTime = toUserZone(roundRecord.getStartTime().toLocalDateTime());

    Round round = new Round()
        .setId(roundRecord.getId())
        .setGameCode(gameCode)
        .setMapCode(mapCode)
        .setMapName(mapName)
        .setModeName(modeName)
        .setStartTime(startTime)
        .setGameId(roundRecord.getGameId());

    // though roundEndStatsRecord is not null, it might be empty
    if (roundEndStatsRecord.getRoundId() != null) {
      LocalDateTime endTime = toUserZone(roundEndStatsRecord.getEndTime().toLocalDateTime());
      long durationMinutes = startTime.until(endTime, ChronoUnit.MINUTES);

      round.setEndTime(endTime)
          .setDurationInMinutes(durationMinutes)
          .setWinningTeam(roundEndStatsRecord.getWinningTeam())
          .setVictoryType(roundEndStatsRecord.getVictoryType())
          .setEndTicketsTeam1(roundEndStatsRecord.getEndTicketsTeam_1())
          .setEndTicketsTeam2(roundEndStatsRecord.getEndTicketsTeam_2());
    } else {
      // round was restarted (or server crashed) before it could be finished, so no end stats present
      round
          .setEndTime(startTime) // actually it can be longer
          .setDurationInMinutes(0)
          .setWinningTeam(-1)
          .setVictoryType(-1)
          .setEndTicketsTeam1(roundRecord.getStartTicketsTeam_1())
          .setEndTicketsTeam2(roundRecord.getStartTicketsTeam_2());
    }

    round.setMapEventsUrlPath("rounds/json/" + round.getId() + "/events");

    return round;
  }

  public List<RoundPlayerStats> getRoundPlayerStats(int roundId) {
    Result<RoundEndStatsPlayerRecord> roundEndStatsRecords = getDslContext()
        .selectFrom(ROUND_END_STATS_PLAYER)
        .where(ROUND_END_STATS_PLAYER.ROUND_ID.eq(roundId))
        .fetch();

    return roundEndStatsRecords.stream().map(RoundService::toRoundPlayerStats)
        .collect(toList());
  }

  private static RoundPlayerStats toRoundPlayerStats(RoundEndStatsPlayerRecord r) {
    return new RoundPlayerStats()
        .setRoundId(r.getRoundId())
        .setPlayerId(r.getPlayerId())
        .setPlayerName(r.getPlayerName())
        .setRank(r.getRank())
        .setBot(r.getIsAi() == 1)
        .setTeam(r.getTeam())
        .setScore(r.getScore())
        .setKills(r.getKills())
        .setDeaths(r.getDeaths())
        .setTks(r.getTks())
        .setCaptures(r.getCaptures())
        .setAttacks(r.getAttacks())
        .setDefences(r.getDefences());

  }

  public int getTotalActiveRoundsCount() {
    return getDslContext().selectCount().from(ROUND)
        .where(DSL.exists(getDslContext().selectOne().from(ROUND_PLAYER_DEATH).where(ROUND_PLAYER_DEATH.ROUND_ID.eq(ROUND.ID))))
        .fetchOne(0, int.class);
  }

  public int getTotalRoundsCount() {
    return getDslContext().selectCount().from(ROUND).fetchOne(0, int.class);
  }

  public List<RoundEvent> getRoundEvents(String gameCode, int roundId) {
    Result<Record> records = fetchDeathRecords(null, null, roundId);
    return records.stream()
        .map(r -> toDeathEvent(gameCode, r))
        .collect(toList());
  }

  public List<ScoreEvent> getScoreEvents(int roundId) {
    return fetchScoreEventRecords(null, null, roundId).stream()
        .map(RoundService::toScoreEvent)
        .collect(toList());
  }

  public List<Record> fetchScoreEventRecords(String mapCode, Integer playerId, Integer roundId) {
    if (mapCode == null && roundId == null) {
      throw new IllegalArgumentException("Either mapCode or roundId has to be set");
    }
    return getDslContext()
        .select(ROUND_PLAYER_SCORE_EVENT.fields())
        .select(PLAYER.NAME)
        .select(ROUND_PLAYER_TEAM.TEAM)
        .from(ROUND_PLAYER_SCORE_EVENT)
        .join(ROUND).on(ROUND.ID.eq(ROUND_PLAYER_SCORE_EVENT.ROUND_ID))
        .join(PLAYER).on(PLAYER.ID.eq(ROUND_PLAYER_SCORE_EVENT.PLAYER_ID))
        .leftJoin(ROUND_PLAYER_TEAM).on(ROUND_PLAYER_TEAM.ROUND_ID.eq(ROUND_PLAYER_SCORE_EVENT.ROUND_ID)
            .and(ROUND_PLAYER_TEAM.PLAYER_ID.eq(ROUND_PLAYER_SCORE_EVENT.PLAYER_ID))
            .and(ROUND_PLAYER_SCORE_EVENT.EVENT_TIME.between(ROUND_PLAYER_TEAM.START_TIME, ROUND_PLAYER_TEAM.END_TIME))
        )
        .where(mapCode == null ? trueCondition() : ROUND.MAP_CODE.eq(mapCode))
        .and(playerId == null ? trueCondition() : ROUND_PLAYER_SCORE_EVENT.PLAYER_ID.eq(playerId))
        .and(roundId == null ? trueCondition() : ROUND_PLAYER_SCORE_EVENT.ROUND_ID.eq(roundId))
        .fetch();
  }

  private static ScoreEvent toScoreEvent(Record scoreRecord) {
    BigDecimal playerX = scoreRecord.get(ROUND_PLAYER_SCORE_EVENT.PLAYER_LOCATION_X);
    BigDecimal playerY = scoreRecord.get(ROUND_PLAYER_SCORE_EVENT.PLAYER_LOCATION_Y);
    BigDecimal playerZ = scoreRecord.get(ROUND_PLAYER_SCORE_EVENT.PLAYER_LOCATION_Z);
    Location location = new Location(playerX.floatValue(), playerY.floatValue(), playerZ.floatValue());

    Integer playerId = scoreRecord.get(ROUND_PLAYER_SCORE_EVENT.PLAYER_ID);
    String playerName = scoreRecord.get(PLAYER.NAME);
    Integer playerTeam = scoreRecord.get(ROUND_PLAYER_TEAM.TEAM);

    String scoreType = scoreRecord.get(ROUND_PLAYER_SCORE_EVENT.SCORE_TYPE);

    LocalDateTime eventTime = toUserZone(scoreRecord.get(ROUND_PLAYER_SCORE_EVENT.EVENT_TIME).toLocalDateTime());

    return new ScoreEvent()
        .setLocation(location)
        .setTime(eventTime)
        .setPlayerId(playerId)
        .setPlayerName(playerName)
        .setPlayerTeam(playerTeam)
        .setScoreType(scoreType); // FlagCapture, Defence
  }

  public List<VehicleEvent> getVehicleEvents(String gameCode, int roundId) {
    return fetchVehicleEventRecords(null, null, roundId).stream()
        .map(r -> toVehicleEvent(gameCode, r))
        .collect(toList());
  }

  public List<Record> fetchVehicleEventRecords(String mapCode, Integer playerId, Integer roundId) {
    if (mapCode == null && roundId == null) {
      throw new IllegalArgumentException("Either mapCode or roundId has to be set");
    }
    return getDslContext()
        .select(ROUND_PLAYER_VEHICLE.fields())
        .select(PLAYER.NAME)
        .select(ROUND_PLAYER_TEAM.TEAM)
        .from(ROUND_PLAYER_VEHICLE)
        .join(ROUND).on(ROUND.ID.eq(ROUND_PLAYER_VEHICLE.ROUND_ID))
        .join(PLAYER).on(PLAYER.ID.eq(ROUND_PLAYER_VEHICLE.PLAYER_ID))
        .leftJoin(ROUND_PLAYER_TEAM).on(ROUND_PLAYER_TEAM.ROUND_ID.eq(ROUND_PLAYER_VEHICLE.ROUND_ID)
            .and(ROUND_PLAYER_TEAM.PLAYER_ID.eq(ROUND_PLAYER_VEHICLE.PLAYER_ID))
            .and(ROUND_PLAYER_VEHICLE.START_TIME.between(ROUND_PLAYER_TEAM.START_TIME, ROUND_PLAYER_TEAM.END_TIME))
        )
        .where(mapCode == null ? trueCondition() : ROUND.MAP_CODE.eq(mapCode))
        .and(playerId == null ? trueCondition() : ROUND_PLAYER_VEHICLE.PLAYER_ID.eq(playerId))
        .and(roundId == null ? trueCondition() : ROUND_PLAYER_VEHICLE.ROUND_ID.eq(roundId))
        .fetch();
  }

  private static VehicleEvent toVehicleEvent(String gameCode, Record vehicleEvent) {
    BigDecimal playerX = vehicleEvent.get(ROUND_PLAYER_VEHICLE.PLAYER_LOCATION_X);
    BigDecimal playerY = vehicleEvent.get(ROUND_PLAYER_VEHICLE.PLAYER_LOCATION_Y);
    BigDecimal playerZ = vehicleEvent.get(ROUND_PLAYER_VEHICLE.PLAYER_LOCATION_Z);
    Location startLocation = new Location(playerX.floatValue(), playerY.floatValue(), playerZ.floatValue());

    Integer playerId = vehicleEvent.get(ROUND_PLAYER_VEHICLE.PLAYER_ID);
    String playerName = vehicleEvent.get(PLAYER.NAME);
    Integer playerTeam = vehicleEvent.get(ROUND_PLAYER_TEAM.TEAM);

    String vehicleCode = vehicleEvent.get(ROUND_PLAYER_VEHICLE.VEHICLE);

    LocalDateTime startTime = toUserZone(vehicleEvent.get(ROUND_PLAYER_VEHICLE.START_TIME).toLocalDateTime());
    LocalDateTime endTime = toUserZone(vehicleEvent.get(ROUND_PLAYER_VEHICLE.END_TIME).toLocalDateTime());
    Integer durationSeconds = vehicleEvent.get(ROUND_PLAYER_VEHICLE.DURATION_SECONDS);

    String vehicleName = VehicleService.vehicleNameAndSeat(gameCode, vehicleCode);

    return new VehicleEvent()
        .setStartLocation(startLocation)
        .setPlayerId(playerId)
        .setPlayerName(playerName)
        .setPlayerTeam(playerTeam)
        .setStartTime(startTime)
        .setEndTime(endTime)
        .setVehicleCode(vehicleCode)
        .setVehicleName(vehicleName);
  }

  public Result<Record> fetchKillRecords(String mapCode, Integer playerId, Integer roundId) {
    return fetchDeathRecordsCommon(mapCode, playerId, roundId, true);
  }

  public Result<Record> fetchDeathRecords(String mapCode, Integer playerId, Integer roundId) {
    return fetchDeathRecordsCommon(mapCode, playerId, roundId, false);
  }

  private Result<Record> fetchDeathRecordsCommon(String mapCode, Integer playerId, Integer roundId, boolean killer) {
    if (mapCode == null && roundId == null) {
      throw new IllegalArgumentException("Either mapCode or roundId has to be set");
    }
    TableField<RoundPlayerDeathRecord, Integer> playerIdField = killer ?
        ROUND_PLAYER_DEATH.KILLER_PLAYER_ID :
        ROUND_PLAYER_DEATH.PLAYER_ID;

    return getDslContext()
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
        .where(mapCode == null ? trueCondition() : ROUND.MAP_CODE.eq(mapCode))
        .and(playerId == null ? playerIdField.isNotNull() : playerIdField.eq(playerId))
        .and(roundId == null ? trueCondition() : ROUND_PLAYER_DEATH.ROUND_ID.eq(roundId))
        .fetch();
  }

  public static RoundEvent toDeathEvent(String gameCode, Record deathRecord) {
    Location deathLocation = toDeathLocation(deathRecord);
    BigDecimal killerX = deathRecord.get(ROUND_PLAYER_DEATH.KILLER_LOCATION_X);
    BigDecimal killerY = deathRecord.get(ROUND_PLAYER_DEATH.KILLER_LOCATION_Y);
    BigDecimal killerZ = deathRecord.get(ROUND_PLAYER_DEATH.KILLER_LOCATION_Z);
    Location killerLocation = killerX != null ?
        new Location(killerX.floatValue(), killerY.floatValue(), killerZ.floatValue()) :
        null;

    String killType = deathRecord.get(ROUND_PLAYER_DEATH.KILL_TYPE);

    Integer killerPlayerId = deathRecord.get(ROUND_PLAYER_DEATH.KILLER_PLAYER_ID);
    Integer playerId = deathRecord.get(ROUND_PLAYER_DEATH.PLAYER_ID);
    String playerName = deathRecord.get(PLAYER.NAME);
    String killerPlayerName = deathRecord.get(KILLER_PLAYER_TABLE.NAME);
    Integer playerTeam = findPlayerTeam(deathRecord);
    Integer killerPlayerTeam = findKillerTeam(deathRecord);

    LocalDateTime deathTime = toUserZone(deathRecord.get(ROUND_PLAYER_DEATH.EVENT_TIME).toLocalDateTime());

    String killWeaponCode = deathRecord.get(ROUND_PLAYER_DEATH.KILL_WEAPON);

    Weapon killWeapon = ofNullable(killWeaponCode)
        .map(c -> new Weapon(gameCode, killWeaponCode, TranslationUtil.getWeaponOrVehicleName(gameCode, killWeaponCode)))
        .orElse(null);

    return new RoundEvent()
        .setLocation(deathLocation)
        .setRelatedLocation(killerLocation)
        .setTime(deathTime)
        .setKillerPlayerId(killerPlayerId)
        .setKillerPlayerName(killerPlayerName)
        .setKillerPlayerTeam(killerPlayerTeam)
        .setPlayerId(playerId)
        .setPlayerName(playerName)
        .setPlayerTeam(playerTeam)
        .setKillWeapon(killWeapon)
        .setKillType(killType);
  }

  public static Location toDeathLocation(Record deathRecord) {
    BigDecimal deathX = deathRecord.get(ROUND_PLAYER_DEATH.PLAYER_LOCATION_X);
    BigDecimal deathY = deathRecord.get(ROUND_PLAYER_DEATH.PLAYER_LOCATION_Y);
    BigDecimal deathZ = deathRecord.get(ROUND_PLAYER_DEATH.PLAYER_LOCATION_Z);
    return new Location(deathX.floatValue(), deathY.floatValue(), deathZ.floatValue());
  }

  public static Location toKillerLocation(Record deathRecord) {
    BigDecimal killerX = deathRecord.get(ROUND_PLAYER_DEATH.KILLER_LOCATION_X);
    BigDecimal killerY = deathRecord.get(ROUND_PLAYER_DEATH.KILLER_LOCATION_Y);
    BigDecimal killerZ = deathRecord.get(ROUND_PLAYER_DEATH.KILLER_LOCATION_Z);
    return new Location(killerX.floatValue(), killerY.floatValue(), killerZ.floatValue());
  }

  public static RoundEvent toKillEvent(String gameCode, Record deathRecord) {
    Location killerLocation = RoundService.toKillerLocation(deathRecord);
    Location deathLocation = toDeathLocation(deathRecord);

    String killType = deathRecord.get(ROUND_PLAYER_DEATH.KILL_TYPE);

    Integer killerPlayerId = deathRecord.get(ROUND_PLAYER_DEATH.KILLER_PLAYER_ID);
    Integer playerId = deathRecord.get(ROUND_PLAYER_DEATH.PLAYER_ID);
    String playerName = deathRecord.get(PLAYER.NAME);
    String killerPlayerName = deathRecord.get(KILLER_PLAYER_TABLE.NAME);

    Integer playerTeam = findPlayerTeam(deathRecord);
    Integer killerPlayerTeam = findKillerTeam(deathRecord);

    LocalDateTime deathTime = toUserZone(deathRecord.get(ROUND_PLAYER_DEATH.EVENT_TIME).toLocalDateTime());
    String killWeaponCode = deathRecord.get(ROUND_PLAYER_DEATH.KILL_WEAPON);

    Weapon killWeapon = ofNullable(killWeaponCode)
        .map(c -> new Weapon(gameCode, killWeaponCode, TranslationUtil.getWeaponOrVehicleName(gameCode, killWeaponCode)))
        .orElse(null);

    return new RoundEvent()
        .setLocation(killerLocation)
        .setRelatedLocation(deathLocation)
        .setTime(deathTime)
        .setKillerPlayerId(killerPlayerId)
        .setKillerPlayerName(killerPlayerName)
        .setKillerPlayerTeam(killerPlayerTeam)
        .setPlayerId(playerId)
        .setPlayerName(playerName)
        .setPlayerTeam(playerTeam)
        .setKillWeapon(killWeapon)
        .setKillType(killType);
  }

  private static Integer findPlayerTeam(Record deathRecord) {
    Integer playerTeam = deathRecord.get(ROUND_PLAYER_TEAM.TEAM);
    if (playerTeam == null && deathRecord.get(PLAYER.NAME) != null) {
      String killType = deathRecord.get(ROUND_PLAYER_DEATH.KILL_TYPE);
      // we don't store bot team, so guessing it instead
      if (KILL.equals(killType)) {
        Integer killerTeam = deathRecord.get(KILLER_PLAYER_TEAM_TABLE.TEAM);
        playerTeam = Objects.equals(killerTeam, 1) ? 2 : 1;
      } else if (TK.equals(killType)) {
        Integer killerTeam = deathRecord.get(KILLER_PLAYER_TEAM_TABLE.TEAM);
        playerTeam = Objects.equals(killerTeam, 1) ? 1 : 2;
      }
    }
    return playerTeam;
  }

  private static Integer findKillerTeam(Record deathRecord) {
    Integer killerPlayerTeam = deathRecord.get(KILLER_PLAYER_TEAM_TABLE.TEAM);
    if (killerPlayerTeam == null && deathRecord.get(KILLER_PLAYER_TABLE.NAME) != null) {
      String killType = deathRecord.get(ROUND_PLAYER_DEATH.KILL_TYPE);
      // we don't store bot team, so guessing it instead
      if (KILL.equals(killType)) {
        Integer playerTeam = deathRecord.get(ROUND_PLAYER_TEAM.TEAM);
        killerPlayerTeam = Objects.equals(playerTeam, 1) ? 2 : 1;
      } else if (TK.equals(killType)) {
        Integer playerTeam = deathRecord.get(ROUND_PLAYER_TEAM.TEAM);
        killerPlayerTeam = Objects.equals(playerTeam, 1) ? 1 : 2;
      }
    }
    return killerPlayerTeam;
  }

  @Data
  @Accessors(chain = true)
  public static class RoundPlayerStats {
    private int roundId;
    private int playerId;
    private String playerName;
    private int rank;
    private boolean bot;
    private int team;
    private int score;
    private int kills;
    private int deaths;
    private int tks;
    private int captures;
    private int attacks;
    private int defences;
  }
}
