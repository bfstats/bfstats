/*
 * This file is generated by jOOQ.
*/
package io.github.bfstats.dbstats.jooq;


import io.github.bfstats.dbstats.jooq.tables.Game;
import io.github.bfstats.dbstats.jooq.tables.PlayerNickname;
import io.github.bfstats.dbstats.jooq.tables.PlayerRank;
import io.github.bfstats.dbstats.jooq.tables.Round;
import io.github.bfstats.dbstats.jooq.tables.RoundChatLog;
import io.github.bfstats.dbstats.jooq.tables.RoundEndStats;
import io.github.bfstats.dbstats.jooq.tables.RoundEndStatsPlayer;
import io.github.bfstats.dbstats.jooq.tables.RoundPlayer;
import io.github.bfstats.dbstats.jooq.tables.RoundPlayerDeath;
import io.github.bfstats.dbstats.jooq.tables.RoundPlayerDeployObject;
import io.github.bfstats.dbstats.jooq.tables.RoundPlayerMedpack;
import io.github.bfstats.dbstats.jooq.tables.RoundPlayerPickupKit;
import io.github.bfstats.dbstats.jooq.tables.RoundPlayerRepair;
import io.github.bfstats.dbstats.jooq.tables.RoundPlayerScoreEvent;
import io.github.bfstats.dbstats.jooq.tables.RoundPlayerTeam;
import io.github.bfstats.dbstats.jooq.tables.RoundPlayerVehicle;

import javax.annotation.Generated;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling indexes of tables of the <code></code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index GAME_SERVER_ID_IDX = Indexes0.GAME_SERVER_ID_IDX;
    public static final Index PLAYER_NICKNAME_PLAYER_ID_IDX = Indexes0.PLAYER_NICKNAME_PLAYER_ID_IDX;
    public static final Index PLAYER_RANK_PLAYER_ID_IDX = Indexes0.PLAYER_RANK_PLAYER_ID_IDX;
    public static final Index ROUND_GAME_ID_IDX = Indexes0.ROUND_GAME_ID_IDX;
    public static final Index ROUND_CHAT_LOG_PLAYER_ID_IDX = Indexes0.ROUND_CHAT_LOG_PLAYER_ID_IDX;
    public static final Index ROUND_CHAT_LOG_ROUND_ID_IDX = Indexes0.ROUND_CHAT_LOG_ROUND_ID_IDX;
    public static final Index ROUND_END_STATS_ROUND_ID_IDX = Indexes0.ROUND_END_STATS_ROUND_ID_IDX;
    public static final Index ROUND_END_STATS_PLAYER_PLAYER_ID_IDX = Indexes0.ROUND_END_STATS_PLAYER_PLAYER_ID_IDX;
    public static final Index ROUND_END_STATS_PLAYER_ROUND_ID_IDX = Indexes0.ROUND_END_STATS_PLAYER_ROUND_ID_IDX;
    public static final Index ROUND_PLAYER_END_ROUND_ID_IDX = Indexes0.ROUND_PLAYER_END_ROUND_ID_IDX;
    public static final Index ROUND_PLAYER_JOINED_ROUND_ID_IDX = Indexes0.ROUND_PLAYER_JOINED_ROUND_ID_IDX;
    public static final Index ROUND_PLAYER_PLAYER_ID_IDX = Indexes0.ROUND_PLAYER_PLAYER_ID_IDX;
    public static final Index ROUND_PLAYER_DEATH_PLAYER_ID_IDX = Indexes0.ROUND_PLAYER_DEATH_PLAYER_ID_IDX;
    public static final Index ROUND_PLAYER_DEATH_ROUND_ID_IDX = Indexes0.ROUND_PLAYER_DEATH_ROUND_ID_IDX;
    public static final Index ROUND_PLAYER_KILLER_PLAYER_ID_IDX = Indexes0.ROUND_PLAYER_KILLER_PLAYER_ID_IDX;
    public static final Index ROUND_PLAYER_DEPLOY_OBJECT_PLAYER_ID_IDX = Indexes0.ROUND_PLAYER_DEPLOY_OBJECT_PLAYER_ID_IDX;
    public static final Index ROUND_PLAYER_DEPLOY_OBJECT_ROUND_ID_IDX = Indexes0.ROUND_PLAYER_DEPLOY_OBJECT_ROUND_ID_IDX;
    public static final Index ROUND_PLAYER_MEDPACK_PLAYER_ID_IDX = Indexes0.ROUND_PLAYER_MEDPACK_PLAYER_ID_IDX;
    public static final Index ROUND_PLAYER_MEDPACK_ROUND_ID_IDX = Indexes0.ROUND_PLAYER_MEDPACK_ROUND_ID_IDX;
    public static final Index ROUND_PLAYER_MEDPACK_VEHICLE_HEALED_PLAYER_ID_IDX = Indexes0.ROUND_PLAYER_MEDPACK_VEHICLE_HEALED_PLAYER_ID_IDX;
    public static final Index ROUND_PLAYER_PICKUP_KIT_PLAYER_ID_IDX = Indexes0.ROUND_PLAYER_PICKUP_KIT_PLAYER_ID_IDX;
    public static final Index ROUND_PLAYER_PICKUP_KIT_ROUND_ID_IDX = Indexes0.ROUND_PLAYER_PICKUP_KIT_ROUND_ID_IDX;
    public static final Index ROUND_PLAYER_REPAIR_PLAYER_ID_IDX = Indexes0.ROUND_PLAYER_REPAIR_PLAYER_ID_IDX;
    public static final Index ROUND_PLAYER_REPAIR_ROUND_ID_IDX = Indexes0.ROUND_PLAYER_REPAIR_ROUND_ID_IDX;
    public static final Index ROUND_PLAYER_REPAIR_VEHICLE_PLAYER_ID_IDX = Indexes0.ROUND_PLAYER_REPAIR_VEHICLE_PLAYER_ID_IDX;
    public static final Index ROUND_PLAYER_SCORE_PLAYER_ID_IDX = Indexes0.ROUND_PLAYER_SCORE_PLAYER_ID_IDX;
    public static final Index ROUND_PLAYER_SCORE_ROUND_ID_IDX = Indexes0.ROUND_PLAYER_SCORE_ROUND_ID_IDX;
    public static final Index ROUND_PLAYER_SCORE_TYPE_IDX = Indexes0.ROUND_PLAYER_SCORE_TYPE_IDX;
    public static final Index ROUND_PLAYER_START_END_TIME_IDX = Indexes0.ROUND_PLAYER_START_END_TIME_IDX;
    public static final Index ROUND_PLAYER_TEAM_ID_IDX = Indexes0.ROUND_PLAYER_TEAM_ID_IDX;
    public static final Index ROUND_PLAYER_VEHICLE_PLAYER_ID_IDX = Indexes0.ROUND_PLAYER_VEHICLE_PLAYER_ID_IDX;
    public static final Index ROUND_PLAYER_VEHICLE_ROUND_ID_IDX = Indexes0.ROUND_PLAYER_VEHICLE_ROUND_ID_IDX;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 extends AbstractKeys {
        public static Index GAME_SERVER_ID_IDX = createIndex("game_server_id_idx", Game.GAME, new OrderField[] { Game.GAME.SERVER_ID }, false);
        public static Index PLAYER_NICKNAME_PLAYER_ID_IDX = createIndex("player_nickname_player_id_idx", PlayerNickname.PLAYER_NICKNAME, new OrderField[] { PlayerNickname.PLAYER_NICKNAME.PLAYER_ID }, false);
        public static Index PLAYER_RANK_PLAYER_ID_IDX = createIndex("player_rank_player_id_idx", PlayerRank.PLAYER_RANK, new OrderField[] { PlayerRank.PLAYER_RANK.PLAYER_ID }, false);
        public static Index ROUND_GAME_ID_IDX = createIndex("round_game_id_idx", Round.ROUND, new OrderField[] { Round.ROUND.GAME_ID }, false);
        public static Index ROUND_CHAT_LOG_PLAYER_ID_IDX = createIndex("round_chat_log_player_id_idx", RoundChatLog.ROUND_CHAT_LOG, new OrderField[] { RoundChatLog.ROUND_CHAT_LOG.PLAYER_ID }, false);
        public static Index ROUND_CHAT_LOG_ROUND_ID_IDX = createIndex("round_chat_log_round_id_idx", RoundChatLog.ROUND_CHAT_LOG, new OrderField[] { RoundChatLog.ROUND_CHAT_LOG.ROUND_ID }, false);
        public static Index ROUND_END_STATS_ROUND_ID_IDX = createIndex("round_end_stats_round_id_idx", RoundEndStats.ROUND_END_STATS, new OrderField[] { RoundEndStats.ROUND_END_STATS.ROUND_ID }, false);
        public static Index ROUND_END_STATS_PLAYER_PLAYER_ID_IDX = createIndex("round_end_stats_player_player_id_idx", RoundEndStatsPlayer.ROUND_END_STATS_PLAYER, new OrderField[] { RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.PLAYER_ID }, false);
        public static Index ROUND_END_STATS_PLAYER_ROUND_ID_IDX = createIndex("round_end_stats_player_round_id_idx", RoundEndStatsPlayer.ROUND_END_STATS_PLAYER, new OrderField[] { RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.ROUND_ID }, false);
        public static Index ROUND_PLAYER_END_ROUND_ID_IDX = createIndex("round_player_end_round_id_idx", RoundPlayer.ROUND_PLAYER, new OrderField[] { RoundPlayer.ROUND_PLAYER.END_ROUND_ID }, false);
        public static Index ROUND_PLAYER_JOINED_ROUND_ID_IDX = createIndex("round_player_joined_round_id_idx", RoundPlayer.ROUND_PLAYER, new OrderField[] { RoundPlayer.ROUND_PLAYER.JOINED_ROUND_ID }, false);
        public static Index ROUND_PLAYER_PLAYER_ID_IDX = createIndex("round_player_player_id_idx", RoundPlayer.ROUND_PLAYER, new OrderField[] { RoundPlayer.ROUND_PLAYER.PLAYER_ID }, false);
        public static Index ROUND_PLAYER_DEATH_PLAYER_ID_IDX = createIndex("round_player_death_player_id_idx", RoundPlayerDeath.ROUND_PLAYER_DEATH, new OrderField[] { RoundPlayerDeath.ROUND_PLAYER_DEATH.PLAYER_ID }, false);
        public static Index ROUND_PLAYER_DEATH_ROUND_ID_IDX = createIndex("round_player_death_round_id_idx", RoundPlayerDeath.ROUND_PLAYER_DEATH, new OrderField[] { RoundPlayerDeath.ROUND_PLAYER_DEATH.ROUND_ID }, false);
        public static Index ROUND_PLAYER_KILLER_PLAYER_ID_IDX = createIndex("round_player_killer_player_id_idx", RoundPlayerDeath.ROUND_PLAYER_DEATH, new OrderField[] { RoundPlayerDeath.ROUND_PLAYER_DEATH.KILLER_PLAYER_ID }, false);
        public static Index ROUND_PLAYER_DEPLOY_OBJECT_PLAYER_ID_IDX = createIndex("round_player_deploy_object_player_id_idx", RoundPlayerDeployObject.ROUND_PLAYER_DEPLOY_OBJECT, new OrderField[] { RoundPlayerDeployObject.ROUND_PLAYER_DEPLOY_OBJECT.PLAYER_ID }, false);
        public static Index ROUND_PLAYER_DEPLOY_OBJECT_ROUND_ID_IDX = createIndex("round_player_deploy_object_round_id_idx", RoundPlayerDeployObject.ROUND_PLAYER_DEPLOY_OBJECT, new OrderField[] { RoundPlayerDeployObject.ROUND_PLAYER_DEPLOY_OBJECT.ROUND_ID }, false);
        public static Index ROUND_PLAYER_MEDPACK_PLAYER_ID_IDX = createIndex("round_player_medpack_player_id_idx", RoundPlayerMedpack.ROUND_PLAYER_MEDPACK, new OrderField[] { RoundPlayerMedpack.ROUND_PLAYER_MEDPACK.PLAYER_ID }, false);
        public static Index ROUND_PLAYER_MEDPACK_ROUND_ID_IDX = createIndex("round_player_medpack_round_id_idx", RoundPlayerMedpack.ROUND_PLAYER_MEDPACK, new OrderField[] { RoundPlayerMedpack.ROUND_PLAYER_MEDPACK.ROUND_ID }, false);
        public static Index ROUND_PLAYER_MEDPACK_VEHICLE_HEALED_PLAYER_ID_IDX = createIndex("round_player_medpack_vehicle_healed_player_id_idx", RoundPlayerMedpack.ROUND_PLAYER_MEDPACK, new OrderField[] { RoundPlayerMedpack.ROUND_PLAYER_MEDPACK.HEALED_PLAYER_ID }, false);
        public static Index ROUND_PLAYER_PICKUP_KIT_PLAYER_ID_IDX = createIndex("round_player_pickup_kit_player_id_idx", RoundPlayerPickupKit.ROUND_PLAYER_PICKUP_KIT, new OrderField[] { RoundPlayerPickupKit.ROUND_PLAYER_PICKUP_KIT.PLAYER_ID }, false);
        public static Index ROUND_PLAYER_PICKUP_KIT_ROUND_ID_IDX = createIndex("round_player_pickup_kit_round_id_idx", RoundPlayerPickupKit.ROUND_PLAYER_PICKUP_KIT, new OrderField[] { RoundPlayerPickupKit.ROUND_PLAYER_PICKUP_KIT.ROUND_ID }, false);
        public static Index ROUND_PLAYER_REPAIR_PLAYER_ID_IDX = createIndex("round_player_repair_player_id_idx", RoundPlayerRepair.ROUND_PLAYER_REPAIR, new OrderField[] { RoundPlayerRepair.ROUND_PLAYER_REPAIR.PLAYER_ID }, false);
        public static Index ROUND_PLAYER_REPAIR_ROUND_ID_IDX = createIndex("round_player_repair_round_id_idx", RoundPlayerRepair.ROUND_PLAYER_REPAIR, new OrderField[] { RoundPlayerRepair.ROUND_PLAYER_REPAIR.ROUND_ID }, false);
        public static Index ROUND_PLAYER_REPAIR_VEHICLE_PLAYER_ID_IDX = createIndex("round_player_repair_vehicle_player_id_idx", RoundPlayerRepair.ROUND_PLAYER_REPAIR, new OrderField[] { RoundPlayerRepair.ROUND_PLAYER_REPAIR.VEHICLE_PLAYER_ID }, false);
        public static Index ROUND_PLAYER_SCORE_PLAYER_ID_IDX = createIndex("round_player_score_player_id_idx", RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT, new OrderField[] { RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT.PLAYER_ID }, false);
        public static Index ROUND_PLAYER_SCORE_ROUND_ID_IDX = createIndex("round_player_score_round_id_idx", RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT, new OrderField[] { RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT.ROUND_ID }, false);
        public static Index ROUND_PLAYER_SCORE_TYPE_IDX = createIndex("round_player_score_type_idx", RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT, new OrderField[] { RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT.SCORE_TYPE }, false);
        public static Index ROUND_PLAYER_START_END_TIME_IDX = createIndex("round_player_start_end_time_idx", RoundPlayerTeam.ROUND_PLAYER_TEAM, new OrderField[] { RoundPlayerTeam.ROUND_PLAYER_TEAM.START_TIME, RoundPlayerTeam.ROUND_PLAYER_TEAM.END_TIME }, false);
        public static Index ROUND_PLAYER_TEAM_ID_IDX = createIndex("round_player_team_id_idx", RoundPlayerTeam.ROUND_PLAYER_TEAM, new OrderField[] { RoundPlayerTeam.ROUND_PLAYER_TEAM.ROUND_ID }, false);
        public static Index ROUND_PLAYER_VEHICLE_PLAYER_ID_IDX = createIndex("round_player_vehicle_player_id_idx", RoundPlayerVehicle.ROUND_PLAYER_VEHICLE, new OrderField[] { RoundPlayerVehicle.ROUND_PLAYER_VEHICLE.PLAYER_ID }, false);
        public static Index ROUND_PLAYER_VEHICLE_ROUND_ID_IDX = createIndex("round_player_vehicle_round_id_idx", RoundPlayerVehicle.ROUND_PLAYER_VEHICLE, new OrderField[] { RoundPlayerVehicle.ROUND_PLAYER_VEHICLE.ROUND_ID }, false);
    }
}
