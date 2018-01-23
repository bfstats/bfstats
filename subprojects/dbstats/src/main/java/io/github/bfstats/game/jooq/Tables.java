/*
 * This file is generated by jOOQ.
*/
package io.github.bfstats.game.jooq;


import io.github.bfstats.game.jooq.tables.Configuration;
import io.github.bfstats.game.jooq.tables.Player;
import io.github.bfstats.game.jooq.tables.PlayerNickname;
import io.github.bfstats.game.jooq.tables.PlayerRank;
import io.github.bfstats.game.jooq.tables.Round;
import io.github.bfstats.game.jooq.tables.RoundChatLog;
import io.github.bfstats.game.jooq.tables.RoundEndStats;
import io.github.bfstats.game.jooq.tables.RoundEndStatsPlayer;
import io.github.bfstats.game.jooq.tables.RoundPlayer;
import io.github.bfstats.game.jooq.tables.RoundPlayerDeath;
import io.github.bfstats.game.jooq.tables.RoundPlayerMedpack;
import io.github.bfstats.game.jooq.tables.RoundPlayerPickupKit;
import io.github.bfstats.game.jooq.tables.RoundPlayerRepair;
import io.github.bfstats.game.jooq.tables.RoundPlayerScoreEvent;
import io.github.bfstats.game.jooq.tables.RoundPlayerTeam;
import io.github.bfstats.game.jooq.tables.RoundPlayerVehicle;
import io.github.bfstats.game.jooq.tables.SqliteSequence;

import javax.annotation.Generated;


/**
 * Convenience access to all tables in 
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>configuration</code>.
     */
    public static final Configuration CONFIGURATION = io.github.bfstats.game.jooq.tables.Configuration.CONFIGURATION;

    /**
     * The table <code>player</code>.
     */
    public static final Player PLAYER = io.github.bfstats.game.jooq.tables.Player.PLAYER;

    /**
     * The table <code>player_nickname</code>.
     */
    public static final PlayerNickname PLAYER_NICKNAME = io.github.bfstats.game.jooq.tables.PlayerNickname.PLAYER_NICKNAME;

    /**
     * The table <code>player_rank</code>.
     */
    public static final PlayerRank PLAYER_RANK = io.github.bfstats.game.jooq.tables.PlayerRank.PLAYER_RANK;

    /**
     * The table <code>round</code>.
     */
    public static final Round ROUND = io.github.bfstats.game.jooq.tables.Round.ROUND;

    /**
     * The table <code>round_chat_log</code>.
     */
    public static final RoundChatLog ROUND_CHAT_LOG = io.github.bfstats.game.jooq.tables.RoundChatLog.ROUND_CHAT_LOG;

    /**
     * The table <code>round_end_stats</code>.
     */
    public static final RoundEndStats ROUND_END_STATS = io.github.bfstats.game.jooq.tables.RoundEndStats.ROUND_END_STATS;

    /**
     * The table <code>round_end_stats_player</code>.
     */
    public static final RoundEndStatsPlayer ROUND_END_STATS_PLAYER = io.github.bfstats.game.jooq.tables.RoundEndStatsPlayer.ROUND_END_STATS_PLAYER;

    /**
     * The table <code>round_player</code>.
     */
    public static final RoundPlayer ROUND_PLAYER = io.github.bfstats.game.jooq.tables.RoundPlayer.ROUND_PLAYER;

    /**
     * The table <code>round_player_death</code>.
     */
    public static final RoundPlayerDeath ROUND_PLAYER_DEATH = io.github.bfstats.game.jooq.tables.RoundPlayerDeath.ROUND_PLAYER_DEATH;

    /**
     * The table <code>round_player_medpack</code>.
     */
    public static final RoundPlayerMedpack ROUND_PLAYER_MEDPACK = io.github.bfstats.game.jooq.tables.RoundPlayerMedpack.ROUND_PLAYER_MEDPACK;

    /**
     * The table <code>round_player_pickup_kit</code>.
     */
    public static final RoundPlayerPickupKit ROUND_PLAYER_PICKUP_KIT = io.github.bfstats.game.jooq.tables.RoundPlayerPickupKit.ROUND_PLAYER_PICKUP_KIT;

    /**
     * The table <code>round_player_repair</code>.
     */
    public static final RoundPlayerRepair ROUND_PLAYER_REPAIR = io.github.bfstats.game.jooq.tables.RoundPlayerRepair.ROUND_PLAYER_REPAIR;

    /**
     * The table <code>round_player_score_event</code>.
     */
    public static final RoundPlayerScoreEvent ROUND_PLAYER_SCORE_EVENT = io.github.bfstats.game.jooq.tables.RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT;

    /**
     * The table <code>round_player_team</code>.
     */
    public static final RoundPlayerTeam ROUND_PLAYER_TEAM = io.github.bfstats.game.jooq.tables.RoundPlayerTeam.ROUND_PLAYER_TEAM;

    /**
     * The table <code>round_player_vehicle</code>.
     */
    public static final RoundPlayerVehicle ROUND_PLAYER_VEHICLE = io.github.bfstats.game.jooq.tables.RoundPlayerVehicle.ROUND_PLAYER_VEHICLE;

    /**
     * The table <code>sqlite_sequence</code>.
     */
    public static final SqliteSequence SQLITE_SEQUENCE = io.github.bfstats.game.jooq.tables.SqliteSequence.SQLITE_SEQUENCE;
}