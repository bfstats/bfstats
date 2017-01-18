/*
 * This file is generated by jOOQ.
*/
package io.github.bfvstats.game.jooq;


import io.github.bfvstats.game.jooq.tables.Player;
import io.github.bfvstats.game.jooq.tables.PlayerNickname;
import io.github.bfvstats.game.jooq.tables.PlayerRank;
import io.github.bfvstats.game.jooq.tables.Round;
import io.github.bfvstats.game.jooq.tables.RoundChatLog;
import io.github.bfvstats.game.jooq.tables.RoundEndStats;
import io.github.bfvstats.game.jooq.tables.RoundEndStatsPlayer;
import io.github.bfvstats.game.jooq.tables.RoundPlayer;
import io.github.bfvstats.game.jooq.tables.RoundPlayerDeath;
import io.github.bfvstats.game.jooq.tables.RoundPlayerMedpack;
import io.github.bfvstats.game.jooq.tables.RoundPlayerPickupKit;
import io.github.bfvstats.game.jooq.tables.RoundPlayerRepair;
import io.github.bfvstats.game.jooq.tables.RoundPlayerScoreEvent;
import io.github.bfvstats.game.jooq.tables.RoundPlayerTeam;
import io.github.bfvstats.game.jooq.tables.RoundPlayerVehicle;
import io.github.bfvstats.game.jooq.tables.records.PlayerNicknameRecord;
import io.github.bfvstats.game.jooq.tables.records.PlayerRankRecord;
import io.github.bfvstats.game.jooq.tables.records.PlayerRecord;
import io.github.bfvstats.game.jooq.tables.records.RoundChatLogRecord;
import io.github.bfvstats.game.jooq.tables.records.RoundEndStatsPlayerRecord;
import io.github.bfvstats.game.jooq.tables.records.RoundEndStatsRecord;
import io.github.bfvstats.game.jooq.tables.records.RoundPlayerDeathRecord;
import io.github.bfvstats.game.jooq.tables.records.RoundPlayerMedpackRecord;
import io.github.bfvstats.game.jooq.tables.records.RoundPlayerPickupKitRecord;
import io.github.bfvstats.game.jooq.tables.records.RoundPlayerRecord;
import io.github.bfvstats.game.jooq.tables.records.RoundPlayerRepairRecord;
import io.github.bfvstats.game.jooq.tables.records.RoundPlayerScoreEventRecord;
import io.github.bfvstats.game.jooq.tables.records.RoundPlayerTeamRecord;
import io.github.bfvstats.game.jooq.tables.records.RoundPlayerVehicleRecord;
import io.github.bfvstats.game.jooq.tables.records.RoundRecord;

import javax.annotation.Generated;

import org.jooq.ForeignKey;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling foreign key relationships between tables of the <code></code> 
 * schema
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<PlayerRecord> PK_PLAYER = UniqueKeys0.PK_PLAYER;
    public static final UniqueKey<PlayerNicknameRecord> PK_PLAYER_NICKNAME = UniqueKeys0.PK_PLAYER_NICKNAME;
    public static final UniqueKey<PlayerRankRecord> PK_PLAYER_RANK = UniqueKeys0.PK_PLAYER_RANK;
    public static final UniqueKey<RoundRecord> PK_ROUND = UniqueKeys0.PK_ROUND;
    public static final UniqueKey<RoundChatLogRecord> PK_ROUND_CHAT_LOG = UniqueKeys0.PK_ROUND_CHAT_LOG;
    public static final UniqueKey<RoundEndStatsRecord> PK_ROUND_END_STATS = UniqueKeys0.PK_ROUND_END_STATS;
    public static final UniqueKey<RoundEndStatsPlayerRecord> PK_ROUND_END_STATS_PLAYER = UniqueKeys0.PK_ROUND_END_STATS_PLAYER;
    public static final UniqueKey<RoundPlayerRecord> PK_ROUND_PLAYER = UniqueKeys0.PK_ROUND_PLAYER;
    public static final UniqueKey<RoundPlayerDeathRecord> PK_ROUND_PLAYER_DEATH = UniqueKeys0.PK_ROUND_PLAYER_DEATH;
    public static final UniqueKey<RoundPlayerMedpackRecord> PK_ROUND_PLAYER_MEDPACK = UniqueKeys0.PK_ROUND_PLAYER_MEDPACK;
    public static final UniqueKey<RoundPlayerPickupKitRecord> PK_ROUND_PLAYER_PICKUP_KIT = UniqueKeys0.PK_ROUND_PLAYER_PICKUP_KIT;
    public static final UniqueKey<RoundPlayerRepairRecord> PK_ROUND_PLAYER_REPAIR = UniqueKeys0.PK_ROUND_PLAYER_REPAIR;
    public static final UniqueKey<RoundPlayerScoreEventRecord> PK_ROUND_PLAYER_SCORE_EVENT = UniqueKeys0.PK_ROUND_PLAYER_SCORE_EVENT;
    public static final UniqueKey<RoundPlayerTeamRecord> PK_ROUND_PLAYER_TEAM = UniqueKeys0.PK_ROUND_PLAYER_TEAM;
    public static final UniqueKey<RoundPlayerVehicleRecord> PK_ROUND_PLAYER_VEHICLE = UniqueKeys0.PK_ROUND_PLAYER_VEHICLE;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<PlayerNicknameRecord, PlayerRecord> FK_PLAYER_NICKNAME_PLAYER_1 = ForeignKeys0.FK_PLAYER_NICKNAME_PLAYER_1;
    public static final ForeignKey<PlayerRankRecord, PlayerRecord> FK_PLAYER_RANK_PLAYER_1 = ForeignKeys0.FK_PLAYER_RANK_PLAYER_1;
    public static final ForeignKey<RoundChatLogRecord, RoundRecord> FK_ROUND_CHAT_LOG_ROUND_1 = ForeignKeys0.FK_ROUND_CHAT_LOG_ROUND_1;
    public static final ForeignKey<RoundChatLogRecord, PlayerRecord> FK_ROUND_CHAT_LOG_PLAYER_1 = ForeignKeys0.FK_ROUND_CHAT_LOG_PLAYER_1;
    public static final ForeignKey<RoundEndStatsRecord, RoundRecord> FK_ROUND_END_STATS_ROUND_1 = ForeignKeys0.FK_ROUND_END_STATS_ROUND_1;
    public static final ForeignKey<RoundEndStatsPlayerRecord, RoundEndStatsRecord> FK_ROUND_END_STATS_PLAYER_ROUND_END_STATS_1 = ForeignKeys0.FK_ROUND_END_STATS_PLAYER_ROUND_END_STATS_1;
    public static final ForeignKey<RoundEndStatsPlayerRecord, PlayerRecord> FK_ROUND_END_STATS_PLAYER_PLAYER_1 = ForeignKeys0.FK_ROUND_END_STATS_PLAYER_PLAYER_1;
    public static final ForeignKey<RoundPlayerRecord, RoundRecord> FK_ROUND_PLAYER_ROUND_2 = ForeignKeys0.FK_ROUND_PLAYER_ROUND_2;
    public static final ForeignKey<RoundPlayerRecord, RoundRecord> FK_ROUND_PLAYER_ROUND_1 = ForeignKeys0.FK_ROUND_PLAYER_ROUND_1;
    public static final ForeignKey<RoundPlayerRecord, PlayerRecord> FK_ROUND_PLAYER_PLAYER_1 = ForeignKeys0.FK_ROUND_PLAYER_PLAYER_1;
    public static final ForeignKey<RoundPlayerDeathRecord, RoundRecord> FK_ROUND_PLAYER_DEATH_ROUND_1 = ForeignKeys0.FK_ROUND_PLAYER_DEATH_ROUND_1;
    public static final ForeignKey<RoundPlayerDeathRecord, PlayerRecord> FK_ROUND_PLAYER_DEATH_PLAYER_2 = ForeignKeys0.FK_ROUND_PLAYER_DEATH_PLAYER_2;
    public static final ForeignKey<RoundPlayerDeathRecord, PlayerRecord> FK_ROUND_PLAYER_DEATH_PLAYER_1 = ForeignKeys0.FK_ROUND_PLAYER_DEATH_PLAYER_1;
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

    private static class UniqueKeys0 extends AbstractKeys {
        public static final UniqueKey<PlayerRecord> PK_PLAYER = createUniqueKey(Player.PLAYER, "pk_player", Player.PLAYER.ID);
        public static final UniqueKey<PlayerNicknameRecord> PK_PLAYER_NICKNAME = createUniqueKey(PlayerNickname.PLAYER_NICKNAME, "pk_player_nickname", PlayerNickname.PLAYER_NICKNAME.ID);
        public static final UniqueKey<PlayerRankRecord> PK_PLAYER_RANK = createUniqueKey(PlayerRank.PLAYER_RANK, "pk_player_rank", PlayerRank.PLAYER_RANK.RANK);
        public static final UniqueKey<RoundRecord> PK_ROUND = createUniqueKey(Round.ROUND, "pk_round", Round.ROUND.ID);
        public static final UniqueKey<RoundChatLogRecord> PK_ROUND_CHAT_LOG = createUniqueKey(RoundChatLog.ROUND_CHAT_LOG, "pk_round_chat_log", RoundChatLog.ROUND_CHAT_LOG.ID);
        public static final UniqueKey<RoundEndStatsRecord> PK_ROUND_END_STATS = createUniqueKey(RoundEndStats.ROUND_END_STATS, "pk_round_end_stats", RoundEndStats.ROUND_END_STATS.ROUND_ID);
        public static final UniqueKey<RoundEndStatsPlayerRecord> PK_ROUND_END_STATS_PLAYER = createUniqueKey(RoundEndStatsPlayer.ROUND_END_STATS_PLAYER, "pk_round_end_stats_player", RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.ID);
        public static final UniqueKey<RoundPlayerRecord> PK_ROUND_PLAYER = createUniqueKey(RoundPlayer.ROUND_PLAYER, "pk_round_player", RoundPlayer.ROUND_PLAYER.ID);
        public static final UniqueKey<RoundPlayerDeathRecord> PK_ROUND_PLAYER_DEATH = createUniqueKey(RoundPlayerDeath.ROUND_PLAYER_DEATH, "pk_round_player_death", RoundPlayerDeath.ROUND_PLAYER_DEATH.ID);
        public static final UniqueKey<RoundPlayerMedpackRecord> PK_ROUND_PLAYER_MEDPACK = createUniqueKey(RoundPlayerMedpack.ROUND_PLAYER_MEDPACK, "pk_round_player_medpack", RoundPlayerMedpack.ROUND_PLAYER_MEDPACK.ID);
        public static final UniqueKey<RoundPlayerPickupKitRecord> PK_ROUND_PLAYER_PICKUP_KIT = createUniqueKey(RoundPlayerPickupKit.ROUND_PLAYER_PICKUP_KIT, "pk_round_player_pickup_kit", RoundPlayerPickupKit.ROUND_PLAYER_PICKUP_KIT.ID);
        public static final UniqueKey<RoundPlayerRepairRecord> PK_ROUND_PLAYER_REPAIR = createUniqueKey(RoundPlayerRepair.ROUND_PLAYER_REPAIR, "pk_round_player_repair", RoundPlayerRepair.ROUND_PLAYER_REPAIR.ID);
        public static final UniqueKey<RoundPlayerScoreEventRecord> PK_ROUND_PLAYER_SCORE_EVENT = createUniqueKey(RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT, "pk_round_player_score_event", RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT.ID);
        public static final UniqueKey<RoundPlayerTeamRecord> PK_ROUND_PLAYER_TEAM = createUniqueKey(RoundPlayerTeam.ROUND_PLAYER_TEAM, "pk_round_player_team", RoundPlayerTeam.ROUND_PLAYER_TEAM.ID);
        public static final UniqueKey<RoundPlayerVehicleRecord> PK_ROUND_PLAYER_VEHICLE = createUniqueKey(RoundPlayerVehicle.ROUND_PLAYER_VEHICLE, "pk_round_player_vehicle", RoundPlayerVehicle.ROUND_PLAYER_VEHICLE.ID);
    }

    private static class ForeignKeys0 extends AbstractKeys {
        public static final ForeignKey<PlayerNicknameRecord, PlayerRecord> FK_PLAYER_NICKNAME_PLAYER_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_PLAYER, PlayerNickname.PLAYER_NICKNAME, "fk_player_nickname_player_1", PlayerNickname.PLAYER_NICKNAME.PLAYER_ID);
        public static final ForeignKey<PlayerRankRecord, PlayerRecord> FK_PLAYER_RANK_PLAYER_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_PLAYER, PlayerRank.PLAYER_RANK, "fk_player_rank_player_1", PlayerRank.PLAYER_RANK.PLAYER_ID);
        public static final ForeignKey<RoundChatLogRecord, RoundRecord> FK_ROUND_CHAT_LOG_ROUND_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_ROUND, RoundChatLog.ROUND_CHAT_LOG, "fk_round_chat_log_round_1", RoundChatLog.ROUND_CHAT_LOG.ROUND_ID);
        public static final ForeignKey<RoundChatLogRecord, PlayerRecord> FK_ROUND_CHAT_LOG_PLAYER_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_PLAYER, RoundChatLog.ROUND_CHAT_LOG, "fk_round_chat_log_player_1", RoundChatLog.ROUND_CHAT_LOG.PLAYER_ID);
        public static final ForeignKey<RoundEndStatsRecord, RoundRecord> FK_ROUND_END_STATS_ROUND_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_ROUND, RoundEndStats.ROUND_END_STATS, "fk_round_end_stats_round_1", RoundEndStats.ROUND_END_STATS.ROUND_ID);
        public static final ForeignKey<RoundEndStatsPlayerRecord, RoundEndStatsRecord> FK_ROUND_END_STATS_PLAYER_ROUND_END_STATS_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_ROUND_END_STATS, RoundEndStatsPlayer.ROUND_END_STATS_PLAYER, "fk_round_end_stats_player_round_end_stats_1", RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.ROUND_ID);
        public static final ForeignKey<RoundEndStatsPlayerRecord, PlayerRecord> FK_ROUND_END_STATS_PLAYER_PLAYER_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_PLAYER, RoundEndStatsPlayer.ROUND_END_STATS_PLAYER, "fk_round_end_stats_player_player_1", RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.PLAYER_ID);
        public static final ForeignKey<RoundPlayerRecord, RoundRecord> FK_ROUND_PLAYER_ROUND_2 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_ROUND, RoundPlayer.ROUND_PLAYER, "fk_round_player_round_2", RoundPlayer.ROUND_PLAYER.JOINED_ROUND_ID);
        public static final ForeignKey<RoundPlayerRecord, RoundRecord> FK_ROUND_PLAYER_ROUND_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_ROUND, RoundPlayer.ROUND_PLAYER, "fk_round_player_round_1", RoundPlayer.ROUND_PLAYER.END_ROUND_ID);
        public static final ForeignKey<RoundPlayerRecord, PlayerRecord> FK_ROUND_PLAYER_PLAYER_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_PLAYER, RoundPlayer.ROUND_PLAYER, "fk_round_player_player_1", RoundPlayer.ROUND_PLAYER.PLAYER_ID);
        public static final ForeignKey<RoundPlayerDeathRecord, RoundRecord> FK_ROUND_PLAYER_DEATH_ROUND_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_ROUND, RoundPlayerDeath.ROUND_PLAYER_DEATH, "fk_round_player_death_round_1", RoundPlayerDeath.ROUND_PLAYER_DEATH.ROUND_ID);
        public static final ForeignKey<RoundPlayerDeathRecord, PlayerRecord> FK_ROUND_PLAYER_DEATH_PLAYER_2 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_PLAYER, RoundPlayerDeath.ROUND_PLAYER_DEATH, "fk_round_player_death_player_2", RoundPlayerDeath.ROUND_PLAYER_DEATH.PLAYER_ID);
        public static final ForeignKey<RoundPlayerDeathRecord, PlayerRecord> FK_ROUND_PLAYER_DEATH_PLAYER_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_PLAYER, RoundPlayerDeath.ROUND_PLAYER_DEATH, "fk_round_player_death_player_1", RoundPlayerDeath.ROUND_PLAYER_DEATH.KILLER_PLAYER_ID);
        public static final ForeignKey<RoundPlayerMedpackRecord, RoundRecord> FK_ROUND_PLAYER_MEDPACK_ROUND_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_ROUND, RoundPlayerMedpack.ROUND_PLAYER_MEDPACK, "fk_round_player_medpack_round_1", RoundPlayerMedpack.ROUND_PLAYER_MEDPACK.ROUND_ID);
        public static final ForeignKey<RoundPlayerMedpackRecord, PlayerRecord> FK_ROUND_PLAYER_MEDPACK_PLAYER_2 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_PLAYER, RoundPlayerMedpack.ROUND_PLAYER_MEDPACK, "fk_round_player_medpack_player_2", RoundPlayerMedpack.ROUND_PLAYER_MEDPACK.PLAYER_ID);
        public static final ForeignKey<RoundPlayerMedpackRecord, PlayerRecord> FK_ROUND_PLAYER_MEDPACK_PLAYER_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_PLAYER, RoundPlayerMedpack.ROUND_PLAYER_MEDPACK, "fk_round_player_medpack_player_1", RoundPlayerMedpack.ROUND_PLAYER_MEDPACK.HEALED_PLAYER_ID);
        public static final ForeignKey<RoundPlayerPickupKitRecord, RoundRecord> FK_ROUND_PLAYER_PICKUP_KIT_ROUND_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_ROUND, RoundPlayerPickupKit.ROUND_PLAYER_PICKUP_KIT, "fk_round_player_pickup_kit_round_1", RoundPlayerPickupKit.ROUND_PLAYER_PICKUP_KIT.ROUND_ID);
        public static final ForeignKey<RoundPlayerPickupKitRecord, PlayerRecord> FK_ROUND_PLAYER_PICKUP_KIT_PLAYER_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_PLAYER, RoundPlayerPickupKit.ROUND_PLAYER_PICKUP_KIT, "fk_round_player_pickup_kit_player_1", RoundPlayerPickupKit.ROUND_PLAYER_PICKUP_KIT.PLAYER_ID);
        public static final ForeignKey<RoundPlayerRepairRecord, RoundRecord> FK_ROUND_PLAYER_REPAIR_ROUND_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_ROUND, RoundPlayerRepair.ROUND_PLAYER_REPAIR, "fk_round_player_repair_round_1", RoundPlayerRepair.ROUND_PLAYER_REPAIR.ROUND_ID);
        public static final ForeignKey<RoundPlayerRepairRecord, PlayerRecord> FK_ROUND_PLAYER_REPAIR_PLAYER_2 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_PLAYER, RoundPlayerRepair.ROUND_PLAYER_REPAIR, "fk_round_player_repair_player_2", RoundPlayerRepair.ROUND_PLAYER_REPAIR.PLAYER_ID);
        public static final ForeignKey<RoundPlayerRepairRecord, PlayerRecord> FK_ROUND_PLAYER_REPAIR_PLAYER_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_PLAYER, RoundPlayerRepair.ROUND_PLAYER_REPAIR, "fk_round_player_repair_player_1", RoundPlayerRepair.ROUND_PLAYER_REPAIR.VEHICLE_PLAYER_ID);
        public static final ForeignKey<RoundPlayerScoreEventRecord, RoundRecord> FK_ROUND_PLAYER_SCORE_EVENT_ROUND_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_ROUND, RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT, "fk_round_player_score_event_round_1", RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT.ROUND_ID);
        public static final ForeignKey<RoundPlayerScoreEventRecord, PlayerRecord> FK_ROUND_PLAYER_SCORE_EVENT_PLAYER_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_PLAYER, RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT, "fk_round_player_score_event_player_1", RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT.PLAYER_ID);
        public static final ForeignKey<RoundPlayerTeamRecord, RoundRecord> FK_ROUND_PLAYER_TEAM_ROUND_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_ROUND, RoundPlayerTeam.ROUND_PLAYER_TEAM, "fk_round_player_team_round_1", RoundPlayerTeam.ROUND_PLAYER_TEAM.ROUND_ID);
        public static final ForeignKey<RoundPlayerTeamRecord, PlayerRecord> FK_ROUND_PLAYER_TEAM_PLAYER_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_PLAYER, RoundPlayerTeam.ROUND_PLAYER_TEAM, "fk_round_player_team_player_1", RoundPlayerTeam.ROUND_PLAYER_TEAM.PLAYER_ID);
        public static final ForeignKey<RoundPlayerVehicleRecord, RoundRecord> FK_ROUND_PLAYER_VEHICLE_ROUND_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_ROUND, RoundPlayerVehicle.ROUND_PLAYER_VEHICLE, "fk_round_player_vehicle_round_1", RoundPlayerVehicle.ROUND_PLAYER_VEHICLE.ROUND_ID);
        public static final ForeignKey<RoundPlayerVehicleRecord, PlayerRecord> FK_ROUND_PLAYER_VEHICLE_PLAYER_1 = createForeignKey(io.github.bfvstats.game.jooq.Keys.PK_PLAYER, RoundPlayerVehicle.ROUND_PLAYER_VEHICLE, "fk_round_player_vehicle_player_1", RoundPlayerVehicle.ROUND_PLAYER_VEHICLE.PLAYER_ID);
    }
}
