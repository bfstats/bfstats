package io.github.bfstats.service;

import io.github.bfstats.dbstats.jooq.tables.records.GameRecord;
import io.github.bfstats.model.Game;
import io.github.bfstats.model.ServerSettings;
import io.github.bfstats.util.TranslationUtil;
import org.jooq.Result;
import org.jooq.impl.DSL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.List;

import static io.github.bfstats.dbstats.jooq.Tables.*;
import static io.github.bfstats.util.DateTimeUtils.toUserZone;
import static io.github.bfstats.util.DbUtils.getDslContext;
import static java.util.stream.Collectors.toList;

public class GameService {
  public List<Game> getActiveGames(int page) {
    Result<GameRecord> gameRecords = getActiveGameRecords(page);
    return gameRecords.stream()
        .map(GameService::toGame)
        .collect(toList());
  }

  private static Result<GameRecord> getActiveGameRecords(int page) {
    int numberOfRows = 50;
    int firstRowIndex = (page - 1) * numberOfRows;

    return getDslContext()
        .selectFrom(GAME)
        .where(DSL.exists(getDslContext().selectOne().from(ROUND_PLAYER_DEATH)
            .innerJoin(ROUND).on(ROUND.GAME_ID.eq(GAME.ID))
            .where(ROUND_PLAYER_DEATH.ROUND_ID.eq(ROUND.ID))))
        .orderBy(GAME.START_TIME.desc())
        .limit(firstRowIndex, numberOfRows)
        .fetch();
  }

  public List<Game> getGames(int page) {
    Result<GameRecord> gameRecords = getGameRecords(null, page);
    return gameRecords.stream()
        .map(GameService::toGame)
        .collect(toList());
  }

  public Game getGame(int gameId) {
    Result<GameRecord> gameRecords = getGameRecords(gameId, 1);
    if (gameRecords.isEmpty()) {
      throw new IllegalArgumentException("round with id " + gameId + " not found");
    }
    GameRecord gameRecord = gameRecords.get(0);
    Game game = toGame(gameRecord);
    game.setServerSettings(toServerSettings(gameRecord));
    return game;
  }

  private static Result<GameRecord> getGameRecords(@Nullable Integer gameId, int page) {
    int numberOfRows = 50;
    int firstRowIndex = (page - 1) * numberOfRows;

    return getDslContext()
        .selectFrom(GAME)
        .where(gameId == null ? DSL.trueCondition() : GAME.ID.eq(gameId))
        .orderBy(GAME.START_TIME.desc())
        .limit(firstRowIndex, numberOfRows)
        .fetch();
  }

  private static Game toGame(@Nonnull GameRecord gameRecord) {
    String gameCode = gameRecord.getGameCode();
    String mapCode = gameRecord.getMapCode();
    String mapName = TranslationUtil.getMapName(gameCode, mapCode);
    String modeName = TranslationUtil.getModeName(gameCode, gameRecord.getGameMode());

    LocalDateTime startTime = toUserZone(gameRecord.getStartTime().toLocalDateTime());

    return new Game()
        .setId(gameRecord.getId())
        .setGameCode(gameCode)
        .setMapCode(mapCode)
        .setMapName(mapName)
        .setModeName(modeName)
        .setStartTime(startTime);
  }

  private static ServerSettings toServerSettings(GameRecord gameRecord) {
    return new ServerSettings()
        .setServerName(gameRecord.getServerName())
        .setServerPort(gameRecord.getServerPort())
        .setModId(gameRecord.getModId())
        .setGameCode(gameRecord.getGameCode())
        .setGameModeCode(gameRecord.getGameMode())
        .setGameModeName(TranslationUtil.getModeName(gameRecord.getGameCode(), gameRecord.getGameMode()))
        .setMaxGameTime(gameRecord.getMaxGameTime())
        .setMaxPlayers(gameRecord.getMaxPlayers())
        .setScoreLimit(gameRecord.getScoreLimit())
        .setNumberOfRoundsPerMap(gameRecord.getNoOfRounds())
        .setSpawnTime(gameRecord.getSpawnTime())
        .setSpawnDelay(gameRecord.getSpawnDelay())
        .setGameStartDelay(gameRecord.getGameStartDelay())
        .setRoundStartDelay(gameRecord.getRoundStartDelay())
        .setSoldierFriendlyFire(gameRecord.getSoldierFf())
        .setVehicleFriendlyFire(gameRecord.getVehicleFf())
        .setTicketRatio(gameRecord.getTicketRatio())
        .setTeamKillPunish(gameRecord.getTeamKillPunish() == 1)
        .setPunkbusterEnabled(gameRecord.getPunkbusterEnabled() == 1);
  }

  public int getTotalActiveGamesCount() {
    return getDslContext().selectCount().from(GAME)
        .where(DSL.exists(getDslContext().selectOne()
            .from(ROUND_PLAYER_DEATH)
            .innerJoin(ROUND).on(ROUND.GAME_ID.eq(GAME.ID))
            .where(ROUND_PLAYER_DEATH.ROUND_ID.eq(ROUND.ID))))
        .fetchOne(0, int.class);
  }

  public int getTotalGamesCount() {
    return getDslContext().selectCount().from(GAME).fetchOne(0, int.class);
  }
}
