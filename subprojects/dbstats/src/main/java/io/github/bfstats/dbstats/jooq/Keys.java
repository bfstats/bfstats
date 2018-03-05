/*
 * This file is generated by jOOQ.
*/
package io.github.bfstats.dbstats.jooq;


import io.github.bfstats.dbstats.jooq.tables.Configuration;
import io.github.bfstats.dbstats.jooq.tables.Game;
import io.github.bfstats.dbstats.jooq.tables.GamePlayer;
import io.github.bfstats.dbstats.jooq.tables.Player;
import io.github.bfstats.dbstats.jooq.tables.PlayerNickname;
import io.github.bfstats.dbstats.jooq.tables.Round;
import io.github.bfstats.dbstats.jooq.tables.RoundChatLog;
import io.github.bfstats.dbstats.jooq.tables.RoundEndStats;
import io.github.bfstats.dbstats.jooq.tables.RoundEndStatsPlayer;
import io.github.bfstats.dbstats.jooq.tables.RoundPlayerDeath;
import io.github.bfstats.dbstats.jooq.tables.RoundPlayerDeployObject;
import io.github.bfstats.dbstats.jooq.tables.RoundPlayerMedpack;
import io.github.bfstats.dbstats.jooq.tables.RoundPlayerPickupKit;
import io.github.bfstats.dbstats.jooq.tables.RoundPlayerRepair;
import io.github.bfstats.dbstats.jooq.tables.RoundPlayerScoreEvent;
import io.github.bfstats.dbstats.jooq.tables.RoundPlayerTeam;
import io.github.bfstats.dbstats.jooq.tables.RoundPlayerVehicle;
import io.github.bfstats.dbstats.jooq.tables.Server;
import io.github.bfstats.dbstats.jooq.tables.records.ConfigurationRecord;
import io.github.bfstats.dbstats.jooq.tables.records.GamePlayerRecord;
import io.github.bfstats.dbstats.jooq.tables.records.GameRecord;
import io.github.bfstats.dbstats.jooq.tables.records.PlayerNicknameRecord;
import io.github.bfstats.dbstats.jooq.tables.records.PlayerRecord;
import io.github.bfstats.dbstats.jooq.tables.records.RoundChatLogRecord;
import io.github.bfstats.dbstats.jooq.tables.records.RoundEndStatsPlayerRecord;
import io.github.bfstats.dbstats.jooq.tables.records.RoundEndStatsRecord;
import io.github.bfstats.dbstats.jooq.tables.records.RoundPlayerDeathRecord;
import io.github.bfstats.dbstats.jooq.tables.records.RoundPlayerDeployObjectRecord;
import io.github.bfstats.dbstats.jooq.tables.records.RoundPlayerMedpackRecord;
import io.github.bfstats.dbstats.jooq.tables.records.RoundPlayerPickupKitRecord;
import io.github.bfstats.dbstats.jooq.tables.records.RoundPlayerRepairRecord;
import io.github.bfstats.dbstats.jooq.tables.records.RoundPlayerScoreEventRecord;
import io.github.bfstats.dbstats.jooq.tables.records.RoundPlayerTeamRecord;
import io.github.bfstats.dbstats.jooq.tables.records.RoundPlayerVehicleRecord;
import io.github.bfstats.dbstats.jooq.tables.records.RoundRecord;
import io.github.bfstats.dbstats.jooq.tables.records.ServerRecord;

import javax.annotation.Generated;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code></code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<GameRecord, Integer> IDENTITY_GAME = Identities0.IDENTITY_GAME;
    public static final Identity<GamePlayerRecord, Integer> IDENTITY_GAME_PLAYER = Identities0.IDENTITY_GAME_PLAYER;
    public static final Identity<PlayerRecord, Integer> IDENTITY_PLAYER = Identities0.IDENTITY_PLAYER;
    public static final Identity<PlayerNicknameRecord, Integer> IDENTITY_PLAYER_NICKNAME = Identities0.IDENTITY_PLAYER_NICKNAME;
    public static final Identity<RoundRecord, Integer> IDENTITY_ROUND = Identities0.IDENTITY_ROUND;
    public static final Identity<RoundChatLogRecord, Integer> IDENTITY_ROUND_CHAT_LOG = Identities0.IDENTITY_ROUND_CHAT_LOG;
    public static final Identity<RoundEndStatsPlayerRecord, Integer> IDENTITY_ROUND_END_STATS_PLAYER = Identities0.IDENTITY_ROUND_END_STATS_PLAYER;
    public static final Identity<RoundPlayerDeathRecord, Integer> IDENTITY_ROUND_PLAYER_DEATH = Identities0.IDENTITY_ROUND_PLAYER_DEATH;
    public static final Identity<RoundPlayerDeployObjectRecord, Integer> IDENTITY_ROUND_PLAYER_DEPLOY_OBJECT = Identities0.IDENTITY_ROUND_PLAYER_DEPLOY_OBJECT;
    public static final Identity<RoundPlayerMedpackRecord, Integer> IDENTITY_ROUND_PLAYER_MEDPACK = Identities0.IDENTITY_ROUND_PLAYER_MEDPACK;
    public static final Identity<RoundPlayerPickupKitRecord, Integer> IDENTITY_ROUND_PLAYER_PICKUP_KIT = Identities0.IDENTITY_ROUND_PLAYER_PICKUP_KIT;
    public static final Identity<RoundPlayerRepairRecord, Integer> IDENTITY_ROUND_PLAYER_REPAIR = Identities0.IDENTITY_ROUND_PLAYER_REPAIR;
    public static final Identity<RoundPlayerScoreEventRecord, Integer> IDENTITY_ROUND_PLAYER_SCORE_EVENT = Identities0.IDENTITY_ROUND_PLAYER_SCORE_EVENT;
    public static final Identity<RoundPlayerTeamRecord, Integer> IDENTITY_ROUND_PLAYER_TEAM = Identities0.IDENTITY_ROUND_PLAYER_TEAM;
    public static final Identity<RoundPlayerVehicleRecord, Integer> IDENTITY_ROUND_PLAYER_VEHICLE = Identities0.IDENTITY_ROUND_PLAYER_VEHICLE;
    public static final Identity<ServerRecord, Integer> IDENTITY_SERVER = Identities0.IDENTITY_SERVER;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ConfigurationRecord> PK_CONFIGURATION = UniqueKeys0.PK_CONFIGURATION;
    public static final UniqueKey<GameRecord> PK_GAME = UniqueKeys0.PK_GAME;
    public static final UniqueKey<GamePlayerRecord> PK_GAME_PLAYER = UniqueKeys0.PK_GAME_PLAYER;
    public static final UniqueKey<PlayerRecord> PK_PLAYER = UniqueKeys0.PK_PLAYER;
    public static final UniqueKey<PlayerNicknameRecord> PK_PLAYER_NICKNAME = UniqueKeys0.PK_PLAYER_NICKNAME;
    public static final UniqueKey<RoundRecord> PK_ROUND = UniqueKeys0.PK_ROUND;
    public static final UniqueKey<RoundChatLogRecord> PK_ROUND_CHAT_LOG = UniqueKeys0.PK_ROUND_CHAT_LOG;
    public static final UniqueKey<RoundEndStatsRecord> PK_ROUND_END_STATS = UniqueKeys0.PK_ROUND_END_STATS;
    public static final UniqueKey<RoundEndStatsPlayerRecord> PK_ROUND_END_STATS_PLAYER = UniqueKeys0.PK_ROUND_END_STATS_PLAYER;
    public static final UniqueKey<RoundPlayerDeathRecord> PK_ROUND_PLAYER_DEATH = UniqueKeys0.PK_ROUND_PLAYER_DEATH;
    public static final UniqueKey<RoundPlayerDeployObjectRecord> PK_ROUND_PLAYER_DEPLOY_OBJECT = UniqueKeys0.PK_ROUND_PLAYER_DEPLOY_OBJECT;
    public static final UniqueKey<RoundPlayerMedpackRecord> PK_ROUND_PLAYER_MEDPACK = UniqueKeys0.PK_ROUND_PLAYER_MEDPACK;
    public static final UniqueKey<RoundPlayerPickupKitRecord> PK_ROUND_PLAYER_PICKUP_KIT = UniqueKeys0.PK_ROUND_PLAYER_PICKUP_KIT;
    public static final UniqueKey<RoundPlayerRepairRecord> PK_ROUND_PLAYER_REPAIR = UniqueKeys0.PK_ROUND_PLAYER_REPAIR;
    public static final UniqueKey<RoundPlayerScoreEventRecord> PK_ROUND_PLAYER_SCORE_EVENT = UniqueKeys0.PK_ROUND_PLAYER_SCORE_EVENT;
    public static final UniqueKey<RoundPlayerTeamRecord> PK_ROUND_PLAYER_TEAM = UniqueKeys0.PK_ROUND_PLAYER_TEAM;
    public static final UniqueKey<RoundPlayerVehicleRecord> PK_ROUND_PLAYER_VEHICLE = UniqueKeys0.PK_ROUND_PLAYER_VEHICLE;
    public static final UniqueKey<ServerRecord> PK_SERVER = UniqueKeys0.PK_SERVER;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<GameRecord, ServerRecord> FK_GAME_SERVER_1 = ForeignKeys0.FK_GAME_SERVER_1;
    public static final ForeignKey<GamePlayerRecord, GameRecord> FK_GAME_PLAYER_GAME_1 = ForeignKeys0.FK_GAME_PLAYER_GAME_1;
    public static final ForeignKey<GamePlayerRecord, RoundRecord> FK_GAME_PLAYER_ROUND_2 = ForeignKeys0.FK_GAME_PLAYER_ROUND_2;
    public static final ForeignKey<GamePlayerRecord, RoundRecord> FK_GAME_PLAYER_ROUND_1 = ForeignKeys0.FK_GAME_PLAYER_ROUND_1;
    public static final ForeignKey<GamePlayerRecord, PlayerRecord> FK_GAME_PLAYER_PLAYER_1 = ForeignKeys0.FK_GAME_PLAYER_PLAYER_1;
    public static final ForeignKey<PlayerNicknameRecord, PlayerRecord> FK_PLAYER_NICKNAME_PLAYER_1 = ForeignKeys0.FK_PLAYER_NICKNAME_PLAYER_1;
    public static final ForeignKey<RoundRecord, GameRecord> FK_ROUND_GAME_1 = ForeignKeys0.FK_ROUND_GAME_1;
    public static final ForeignKey<RoundChatLogRecord, RoundRecord> FK_ROUND_CHAT_LOG_ROUND_1 = ForeignKeys0.FK_ROUND_CHAT_LOG_ROUND_1;
    public static final ForeignKey<RoundChatLogRecord, PlayerRecord> FK_ROUND_CHAT_LOG_PLAYER_1 = ForeignKeys0.FK_ROUND_CHAT_LOG_PLAYER_1;
    public static final ForeignKey<RoundEndStatsRecord, RoundRecord> FK_ROUND_END_STATS_ROUND_1 = ForeignKeys0.FK_ROUND_END_STATS_ROUND_1;
    public static final ForeignKey<RoundEndStatsPlayerRecord, RoundEndStatsRecord> FK_ROUND_END_STATS_PLAYER_ROUND_END_STATS_1 = ForeignKeys0.FK_ROUND_END_STATS_PLAYER_ROUND_END_STATS_1;
    public static final ForeignKey<RoundEndStatsPlayerRecord, PlayerRecord> FK_ROUND_END_STATS_PLAYER_PLAYER_1 = ForeignKeys0.FK_ROUND_END_STATS_PLAYER_PLAYER_1;
    public static final ForeignKey<RoundPlayerDeathRecord, RoundRecord> FK_ROUND_PLAYER_DEATH_ROUND_1 = ForeignKeys0.FK_ROUND_PLAYER_DEATH_ROUND_1;
    public static final ForeignKey<RoundPlayerDeathRecord, PlayerRecord> FK_ROUND_PLAYER_DEATH_PLAYER_2 = ForeignKeys0.FK_ROUND_PLAYER_DEATH_PLAYER_2;
    public static final ForeignKey<RoundPlayerDeathRecord, PlayerRecord> FK_ROUND_PLAYER_DEATH_PLAYER_1 = ForeignKeys0.FK_ROUND_PLAYER_DEATH_PLAYER_1;
    public static final ForeignKey<RoundPlayerDeployObjectRecord, RoundRecord> FK_ROUND_PLAYER_DEPLOY_OBJECT_ROUND_1 = ForeignKeys0.FK_ROUND_PLAYER_DEPLOY_OBJECT_ROUND_1;
    public static final ForeignKey<RoundPlayerDeployObjectRecord, PlayerRecord> FK_ROUND_PLAYER_DEPLOY_OBJECT_PLAYER_1 = ForeignKeys0.FK_ROUND_PLAYER_DEPLOY_OBJECT_PLAYER_1;
    public static final ForeignKey<RoundPlayerMedpackRecord, RoundRecord> FK_ROUND_PLAYER_MEDPACK_ROUND_1 = ForeignKeys0.FK_ROUND_PLAYER_MEDPACK_ROUND_1;
    public static final ForeignKey<RoundPlayerMedpackRecord, PlayerRecord> FK_ROUND_PLAYER_MEDPACK_PLAYER_2 = ForeignKeys0.FK_ROUND_PLAYER_MEDPACK_PLAYER_2;
    public static final ForeignKey<RoundPlayerMedpackRecord, PlayerRecord> FK_ROUND_PLAYER_MEDPACK_PLAYER_1 = ForeignKeys0.FK_ROUND_PLAYER_MEDPACK_PLAYER_1;
    public static final ForeignKey<RoundPlayerPickupKitRecord, RoundRecord> FK_ROUND_PLAYER_PICKUP_KIT_ROUND_1 = ForeignKeys0.FK_ROUND_PLAYER_PICKUP_KIT_ROUND_1;
    public static final ForeignKey<RoundPlayerPickupKitRecord, PlayerRecord> FK_ROUND_PLAYER_PICKUP_KIT_PLAYER_1 = ForeignKeys0.FK_ROUND_PLAYER_PICKUP_KIT_PLAYER_1;
    public static final ForeignKey<RoundPlayerRepairRecord, RoundRecord> FK_ROUND_PLAYER_REPAIR_ROUND_1 = ForeignKeys0.FK_ROUND_PLAYER_REPAIR_ROUND_1;
    public static final ForeignKey<RoundPlayerRepairRecord, PlayerRecord> FK_ROUND_PLAYER_REPAIR_PLAYER_2 = ForeignKeys0.FK_ROUND_PLAYER_REPAIR_PLAYER_2;
    public static final ForeignKey<RoundPlayerRepairRecord, PlayerRecord> FK_ROUND_PLAYER_REPAIR_PLAYER_1 = ForeignKeys0.FK_ROUND_PLAYER_REPAIR_PLAYER_1;
    public static final ForeignKey<RoundPlayerScoreEventRecord, RoundRecord> FK_ROUND_PLAYER_SCORE_EVENT_ROUND_1 = ForeignKeys0.FK_ROUND_PLAYER_SCORE_EVENT_ROUND_1;
    public static final ForeignKey<RoundPlayerScoreEventRecord, PlayerRecord> FK_ROUND_PLAYER_SCORE_EVENT_PLAYER_1 = ForeignKeys0.FK_ROUND_PLAYER_SCORE_EVENT_PLAYER_1;
    public static final ForeignKey<RoundPlayerTeamRecord, RoundRecord> FK_ROUND_PLAYER_TEAM_ROUND_1 = ForeignKeys0.FK_ROUND_PLAYER_TEAM_ROUND_1;
    public static final ForeignKey<RoundPlayerTeamRecord, PlayerRecord> FK_ROUND_PLAYER_TEAM_PLAYER_1 = ForeignKeys0.FK_ROUND_PLAYER_TEAM_PLAYER_1;
    public static final ForeignKey<RoundPlayerVehicleRecord, RoundRecord> FK_ROUND_PLAYER_VEHICLE_ROUND_1 = ForeignKeys0.FK_ROUND_PLAYER_VEHICLE_ROUND_1;
    public static final ForeignKey<RoundPlayerVehicleRecord, PlayerRecord> FK_ROUND_PLAYER_VEHICLE_PLAYER_1 = ForeignKeys0.FK_ROUND_PLAYER_VEHICLE_PLAYER_1;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 extends AbstractKeys {
        public static Identity<GameRecord, Integer> IDENTITY_GAME = createIdentity(Game.GAME, Game.GAME.ID);
        public static Identity<GamePlayerRecord, Integer> IDENTITY_GAME_PLAYER = createIdentity(GamePlayer.GAME_PLAYER, GamePlayer.GAME_PLAYER.ID);
        public static Identity<PlayerRecord, Integer> IDENTITY_PLAYER = createIdentity(Player.PLAYER, Player.PLAYER.ID);
        public static Identity<PlayerNicknameRecord, Integer> IDENTITY_PLAYER_NICKNAME = createIdentity(PlayerNickname.PLAYER_NICKNAME, PlayerNickname.PLAYER_NICKNAME.ID);
        public static Identity<RoundRecord, Integer> IDENTITY_ROUND = createIdentity(Round.ROUND, Round.ROUND.ID);
        public static Identity<RoundChatLogRecord, Integer> IDENTITY_ROUND_CHAT_LOG = createIdentity(RoundChatLog.ROUND_CHAT_LOG, RoundChatLog.ROUND_CHAT_LOG.ID);
        public static Identity<RoundEndStatsPlayerRecord, Integer> IDENTITY_ROUND_END_STATS_PLAYER = createIdentity(RoundEndStatsPlayer.ROUND_END_STATS_PLAYER, RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.ID);
        public static Identity<RoundPlayerDeathRecord, Integer> IDENTITY_ROUND_PLAYER_DEATH = createIdentity(RoundPlayerDeath.ROUND_PLAYER_DEATH, RoundPlayerDeath.ROUND_PLAYER_DEATH.ID);
        public static Identity<RoundPlayerDeployObjectRecord, Integer> IDENTITY_ROUND_PLAYER_DEPLOY_OBJECT = createIdentity(RoundPlayerDeployObject.ROUND_PLAYER_DEPLOY_OBJECT, RoundPlayerDeployObject.ROUND_PLAYER_DEPLOY_OBJECT.ID);
        public static Identity<RoundPlayerMedpackRecord, Integer> IDENTITY_ROUND_PLAYER_MEDPACK = createIdentity(RoundPlayerMedpack.ROUND_PLAYER_MEDPACK, RoundPlayerMedpack.ROUND_PLAYER_MEDPACK.ID);
        public static Identity<RoundPlayerPickupKitRecord, Integer> IDENTITY_ROUND_PLAYER_PICKUP_KIT = createIdentity(RoundPlayerPickupKit.ROUND_PLAYER_PICKUP_KIT, RoundPlayerPickupKit.ROUND_PLAYER_PICKUP_KIT.ID);
        public static Identity<RoundPlayerRepairRecord, Integer> IDENTITY_ROUND_PLAYER_REPAIR = createIdentity(RoundPlayerRepair.ROUND_PLAYER_REPAIR, RoundPlayerRepair.ROUND_PLAYER_REPAIR.ID);
        public static Identity<RoundPlayerScoreEventRecord, Integer> IDENTITY_ROUND_PLAYER_SCORE_EVENT = createIdentity(RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT, RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT.ID);
        public static Identity<RoundPlayerTeamRecord, Integer> IDENTITY_ROUND_PLAYER_TEAM = createIdentity(RoundPlayerTeam.ROUND_PLAYER_TEAM, RoundPlayerTeam.ROUND_PLAYER_TEAM.ID);
        public static Identity<RoundPlayerVehicleRecord, Integer> IDENTITY_ROUND_PLAYER_VEHICLE = createIdentity(RoundPlayerVehicle.ROUND_PLAYER_VEHICLE, RoundPlayerVehicle.ROUND_PLAYER_VEHICLE.ID);
        public static Identity<ServerRecord, Integer> IDENTITY_SERVER = createIdentity(Server.SERVER, Server.SERVER.ID);
    }

    private static class UniqueKeys0 extends AbstractKeys {
        public static final UniqueKey<ConfigurationRecord> PK_CONFIGURATION = createUniqueKey(Configuration.CONFIGURATION, "pk_configuration", Configuration.CONFIGURATION.LOCK);
        public static final UniqueKey<GameRecord> PK_GAME = createUniqueKey(Game.GAME, "pk_game", Game.GAME.ID);
        public static final UniqueKey<GamePlayerRecord> PK_GAME_PLAYER = createUniqueKey(GamePlayer.GAME_PLAYER, "pk_game_player", GamePlayer.GAME_PLAYER.ID);
        public static final UniqueKey<PlayerRecord> PK_PLAYER = createUniqueKey(Player.PLAYER, "pk_player", Player.PLAYER.ID);
        public static final UniqueKey<PlayerNicknameRecord> PK_PLAYER_NICKNAME = createUniqueKey(PlayerNickname.PLAYER_NICKNAME, "pk_player_nickname", PlayerNickname.PLAYER_NICKNAME.ID);
        public static final UniqueKey<RoundRecord> PK_ROUND = createUniqueKey(Round.ROUND, "pk_round", Round.ROUND.ID);
        public static final UniqueKey<RoundChatLogRecord> PK_ROUND_CHAT_LOG = createUniqueKey(RoundChatLog.ROUND_CHAT_LOG, "pk_round_chat_log", RoundChatLog.ROUND_CHAT_LOG.ID);
        public static final UniqueKey<RoundEndStatsRecord> PK_ROUND_END_STATS = createUniqueKey(RoundEndStats.ROUND_END_STATS, "pk_round_end_stats", RoundEndStats.ROUND_END_STATS.ROUND_ID);
        public static final UniqueKey<RoundEndStatsPlayerRecord> PK_ROUND_END_STATS_PLAYER = createUniqueKey(RoundEndStatsPlayer.ROUND_END_STATS_PLAYER, "pk_round_end_stats_player", RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.ID);
        public static final UniqueKey<RoundPlayerDeathRecord> PK_ROUND_PLAYER_DEATH = createUniqueKey(RoundPlayerDeath.ROUND_PLAYER_DEATH, "pk_round_player_death", RoundPlayerDeath.ROUND_PLAYER_DEATH.ID);
        public static final UniqueKey<RoundPlayerDeployObjectRecord> PK_ROUND_PLAYER_DEPLOY_OBJECT = createUniqueKey(RoundPlayerDeployObject.ROUND_PLAYER_DEPLOY_OBJECT, "pk_round_player_deploy_object", RoundPlayerDeployObject.ROUND_PLAYER_DEPLOY_OBJECT.ID);
        public static final UniqueKey<RoundPlayerMedpackRecord> PK_ROUND_PLAYER_MEDPACK = createUniqueKey(RoundPlayerMedpack.ROUND_PLAYER_MEDPACK, "pk_round_player_medpack", RoundPlayerMedpack.ROUND_PLAYER_MEDPACK.ID);
        public static final UniqueKey<RoundPlayerPickupKitRecord> PK_ROUND_PLAYER_PICKUP_KIT = createUniqueKey(RoundPlayerPickupKit.ROUND_PLAYER_PICKUP_KIT, "pk_round_player_pickup_kit", RoundPlayerPickupKit.ROUND_PLAYER_PICKUP_KIT.ID);
        public static final UniqueKey<RoundPlayerRepairRecord> PK_ROUND_PLAYER_REPAIR = createUniqueKey(RoundPlayerRepair.ROUND_PLAYER_REPAIR, "pk_round_player_repair", RoundPlayerRepair.ROUND_PLAYER_REPAIR.ID);
        public static final UniqueKey<RoundPlayerScoreEventRecord> PK_ROUND_PLAYER_SCORE_EVENT = createUniqueKey(RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT, "pk_round_player_score_event", RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT.ID);
        public static final UniqueKey<RoundPlayerTeamRecord> PK_ROUND_PLAYER_TEAM = createUniqueKey(RoundPlayerTeam.ROUND_PLAYER_TEAM, "pk_round_player_team", RoundPlayerTeam.ROUND_PLAYER_TEAM.ID);
        public static final UniqueKey<RoundPlayerVehicleRecord> PK_ROUND_PLAYER_VEHICLE = createUniqueKey(RoundPlayerVehicle.ROUND_PLAYER_VEHICLE, "pk_round_player_vehicle", RoundPlayerVehicle.ROUND_PLAYER_VEHICLE.ID);
        public static final UniqueKey<ServerRecord> PK_SERVER = createUniqueKey(Server.SERVER, "pk_server", Server.SERVER.ID);
    }

    private static class ForeignKeys0 extends AbstractKeys {
        public static final ForeignKey<GameRecord, ServerRecord> FK_GAME_SERVER_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_SERVER, Game.GAME, "fk_game_server_1", Game.GAME.SERVER_ID);
        public static final ForeignKey<GamePlayerRecord, GameRecord> FK_GAME_PLAYER_GAME_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_GAME, GamePlayer.GAME_PLAYER, "fk_game_player_game_1", GamePlayer.GAME_PLAYER.GAME_ID);
        public static final ForeignKey<GamePlayerRecord, RoundRecord> FK_GAME_PLAYER_ROUND_2 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_ROUND, GamePlayer.GAME_PLAYER, "fk_game_player_round_2", GamePlayer.GAME_PLAYER.JOINED_ROUND_ID);
        public static final ForeignKey<GamePlayerRecord, RoundRecord> FK_GAME_PLAYER_ROUND_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_ROUND, GamePlayer.GAME_PLAYER, "fk_game_player_round_1", GamePlayer.GAME_PLAYER.END_ROUND_ID);
        public static final ForeignKey<GamePlayerRecord, PlayerRecord> FK_GAME_PLAYER_PLAYER_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_PLAYER, GamePlayer.GAME_PLAYER, "fk_game_player_player_1", GamePlayer.GAME_PLAYER.PLAYER_ID);
        public static final ForeignKey<PlayerNicknameRecord, PlayerRecord> FK_PLAYER_NICKNAME_PLAYER_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_PLAYER, PlayerNickname.PLAYER_NICKNAME, "fk_player_nickname_player_1", PlayerNickname.PLAYER_NICKNAME.PLAYER_ID);
        public static final ForeignKey<RoundRecord, GameRecord> FK_ROUND_GAME_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_GAME, Round.ROUND, "fk_round_game_1", Round.ROUND.GAME_ID);
        public static final ForeignKey<RoundChatLogRecord, RoundRecord> FK_ROUND_CHAT_LOG_ROUND_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_ROUND, RoundChatLog.ROUND_CHAT_LOG, "fk_round_chat_log_round_1", RoundChatLog.ROUND_CHAT_LOG.ROUND_ID);
        public static final ForeignKey<RoundChatLogRecord, PlayerRecord> FK_ROUND_CHAT_LOG_PLAYER_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_PLAYER, RoundChatLog.ROUND_CHAT_LOG, "fk_round_chat_log_player_1", RoundChatLog.ROUND_CHAT_LOG.PLAYER_ID);
        public static final ForeignKey<RoundEndStatsRecord, RoundRecord> FK_ROUND_END_STATS_ROUND_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_ROUND, RoundEndStats.ROUND_END_STATS, "fk_round_end_stats_round_1", RoundEndStats.ROUND_END_STATS.ROUND_ID);
        public static final ForeignKey<RoundEndStatsPlayerRecord, RoundEndStatsRecord> FK_ROUND_END_STATS_PLAYER_ROUND_END_STATS_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_ROUND_END_STATS, RoundEndStatsPlayer.ROUND_END_STATS_PLAYER, "fk_round_end_stats_player_round_end_stats_1", RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.ROUND_ID);
        public static final ForeignKey<RoundEndStatsPlayerRecord, PlayerRecord> FK_ROUND_END_STATS_PLAYER_PLAYER_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_PLAYER, RoundEndStatsPlayer.ROUND_END_STATS_PLAYER, "fk_round_end_stats_player_player_1", RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.PLAYER_ID);
        public static final ForeignKey<RoundPlayerDeathRecord, RoundRecord> FK_ROUND_PLAYER_DEATH_ROUND_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_ROUND, RoundPlayerDeath.ROUND_PLAYER_DEATH, "fk_round_player_death_round_1", RoundPlayerDeath.ROUND_PLAYER_DEATH.ROUND_ID);
        public static final ForeignKey<RoundPlayerDeathRecord, PlayerRecord> FK_ROUND_PLAYER_DEATH_PLAYER_2 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_PLAYER, RoundPlayerDeath.ROUND_PLAYER_DEATH, "fk_round_player_death_player_2", RoundPlayerDeath.ROUND_PLAYER_DEATH.PLAYER_ID);
        public static final ForeignKey<RoundPlayerDeathRecord, PlayerRecord> FK_ROUND_PLAYER_DEATH_PLAYER_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_PLAYER, RoundPlayerDeath.ROUND_PLAYER_DEATH, "fk_round_player_death_player_1", RoundPlayerDeath.ROUND_PLAYER_DEATH.KILLER_PLAYER_ID);
        public static final ForeignKey<RoundPlayerDeployObjectRecord, RoundRecord> FK_ROUND_PLAYER_DEPLOY_OBJECT_ROUND_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_ROUND, RoundPlayerDeployObject.ROUND_PLAYER_DEPLOY_OBJECT, "fk_round_player_deploy_object_round_1", RoundPlayerDeployObject.ROUND_PLAYER_DEPLOY_OBJECT.ROUND_ID);
        public static final ForeignKey<RoundPlayerDeployObjectRecord, PlayerRecord> FK_ROUND_PLAYER_DEPLOY_OBJECT_PLAYER_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_PLAYER, RoundPlayerDeployObject.ROUND_PLAYER_DEPLOY_OBJECT, "fk_round_player_deploy_object_player_1", RoundPlayerDeployObject.ROUND_PLAYER_DEPLOY_OBJECT.PLAYER_ID);
        public static final ForeignKey<RoundPlayerMedpackRecord, RoundRecord> FK_ROUND_PLAYER_MEDPACK_ROUND_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_ROUND, RoundPlayerMedpack.ROUND_PLAYER_MEDPACK, "fk_round_player_medpack_round_1", RoundPlayerMedpack.ROUND_PLAYER_MEDPACK.ROUND_ID);
        public static final ForeignKey<RoundPlayerMedpackRecord, PlayerRecord> FK_ROUND_PLAYER_MEDPACK_PLAYER_2 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_PLAYER, RoundPlayerMedpack.ROUND_PLAYER_MEDPACK, "fk_round_player_medpack_player_2", RoundPlayerMedpack.ROUND_PLAYER_MEDPACK.PLAYER_ID);
        public static final ForeignKey<RoundPlayerMedpackRecord, PlayerRecord> FK_ROUND_PLAYER_MEDPACK_PLAYER_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_PLAYER, RoundPlayerMedpack.ROUND_PLAYER_MEDPACK, "fk_round_player_medpack_player_1", RoundPlayerMedpack.ROUND_PLAYER_MEDPACK.HEALED_PLAYER_ID);
        public static final ForeignKey<RoundPlayerPickupKitRecord, RoundRecord> FK_ROUND_PLAYER_PICKUP_KIT_ROUND_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_ROUND, RoundPlayerPickupKit.ROUND_PLAYER_PICKUP_KIT, "fk_round_player_pickup_kit_round_1", RoundPlayerPickupKit.ROUND_PLAYER_PICKUP_KIT.ROUND_ID);
        public static final ForeignKey<RoundPlayerPickupKitRecord, PlayerRecord> FK_ROUND_PLAYER_PICKUP_KIT_PLAYER_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_PLAYER, RoundPlayerPickupKit.ROUND_PLAYER_PICKUP_KIT, "fk_round_player_pickup_kit_player_1", RoundPlayerPickupKit.ROUND_PLAYER_PICKUP_KIT.PLAYER_ID);
        public static final ForeignKey<RoundPlayerRepairRecord, RoundRecord> FK_ROUND_PLAYER_REPAIR_ROUND_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_ROUND, RoundPlayerRepair.ROUND_PLAYER_REPAIR, "fk_round_player_repair_round_1", RoundPlayerRepair.ROUND_PLAYER_REPAIR.ROUND_ID);
        public static final ForeignKey<RoundPlayerRepairRecord, PlayerRecord> FK_ROUND_PLAYER_REPAIR_PLAYER_2 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_PLAYER, RoundPlayerRepair.ROUND_PLAYER_REPAIR, "fk_round_player_repair_player_2", RoundPlayerRepair.ROUND_PLAYER_REPAIR.PLAYER_ID);
        public static final ForeignKey<RoundPlayerRepairRecord, PlayerRecord> FK_ROUND_PLAYER_REPAIR_PLAYER_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_PLAYER, RoundPlayerRepair.ROUND_PLAYER_REPAIR, "fk_round_player_repair_player_1", RoundPlayerRepair.ROUND_PLAYER_REPAIR.VEHICLE_PLAYER_ID);
        public static final ForeignKey<RoundPlayerScoreEventRecord, RoundRecord> FK_ROUND_PLAYER_SCORE_EVENT_ROUND_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_ROUND, RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT, "fk_round_player_score_event_round_1", RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT.ROUND_ID);
        public static final ForeignKey<RoundPlayerScoreEventRecord, PlayerRecord> FK_ROUND_PLAYER_SCORE_EVENT_PLAYER_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_PLAYER, RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT, "fk_round_player_score_event_player_1", RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT.PLAYER_ID);
        public static final ForeignKey<RoundPlayerTeamRecord, RoundRecord> FK_ROUND_PLAYER_TEAM_ROUND_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_ROUND, RoundPlayerTeam.ROUND_PLAYER_TEAM, "fk_round_player_team_round_1", RoundPlayerTeam.ROUND_PLAYER_TEAM.ROUND_ID);
        public static final ForeignKey<RoundPlayerTeamRecord, PlayerRecord> FK_ROUND_PLAYER_TEAM_PLAYER_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_PLAYER, RoundPlayerTeam.ROUND_PLAYER_TEAM, "fk_round_player_team_player_1", RoundPlayerTeam.ROUND_PLAYER_TEAM.PLAYER_ID);
        public static final ForeignKey<RoundPlayerVehicleRecord, RoundRecord> FK_ROUND_PLAYER_VEHICLE_ROUND_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_ROUND, RoundPlayerVehicle.ROUND_PLAYER_VEHICLE, "fk_round_player_vehicle_round_1", RoundPlayerVehicle.ROUND_PLAYER_VEHICLE.ROUND_ID);
        public static final ForeignKey<RoundPlayerVehicleRecord, PlayerRecord> FK_ROUND_PLAYER_VEHICLE_PLAYER_1 = createForeignKey(io.github.bfstats.dbstats.jooq.Keys.PK_PLAYER, RoundPlayerVehicle.ROUND_PLAYER_VEHICLE, "fk_round_player_vehicle_player_1", RoundPlayerVehicle.ROUND_PLAYER_VEHICLE.PLAYER_ID);
    }
}
