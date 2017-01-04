/**
 * This class is generated by jOOQ
 */
package io.github.bfvstats.game.jooq;


import io.github.bfvstats.game.jooq.tables.Player;
import io.github.bfvstats.game.jooq.tables.PlayerNickname;
import io.github.bfvstats.game.jooq.tables.PlayerRank;
import io.github.bfvstats.game.jooq.tables.Round;
import io.github.bfvstats.game.jooq.tables.RoundChatLog;
import io.github.bfvstats.game.jooq.tables.RoundEndStats;
import io.github.bfvstats.game.jooq.tables.RoundEndStatsPlayer;
import io.github.bfvstats.game.jooq.tables.RoundPlayerDeath;
import io.github.bfvstats.game.jooq.tables.RoundPlayerScoreEvent;
import io.github.bfvstats.game.jooq.tables.SqliteSequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.8.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DefaultSchema extends SchemaImpl {

    private static final long serialVersionUID = -302239858;

    /**
     * The reference instance of <code></code>
     */
    public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();

    /**
     * The table <code>player</code>.
     */
    public final Player PLAYER = io.github.bfvstats.game.jooq.tables.Player.PLAYER;

    /**
     * The table <code>player_nickname</code>.
     */
    public final PlayerNickname PLAYER_NICKNAME = io.github.bfvstats.game.jooq.tables.PlayerNickname.PLAYER_NICKNAME;

    /**
     * The table <code>player_rank</code>.
     */
    public final PlayerRank PLAYER_RANK = io.github.bfvstats.game.jooq.tables.PlayerRank.PLAYER_RANK;

    /**
     * The table <code>round</code>.
     */
    public final Round ROUND = io.github.bfvstats.game.jooq.tables.Round.ROUND;

    /**
     * The table <code>round_chat_log</code>.
     */
    public final RoundChatLog ROUND_CHAT_LOG = io.github.bfvstats.game.jooq.tables.RoundChatLog.ROUND_CHAT_LOG;

    /**
     * The table <code>round_end_stats</code>.
     */
    public final RoundEndStats ROUND_END_STATS = io.github.bfvstats.game.jooq.tables.RoundEndStats.ROUND_END_STATS;

    /**
     * The table <code>round_end_stats_player</code>.
     */
    public final RoundEndStatsPlayer ROUND_END_STATS_PLAYER = io.github.bfvstats.game.jooq.tables.RoundEndStatsPlayer.ROUND_END_STATS_PLAYER;

    /**
     * The table <code>round_player_death</code>.
     */
    public final RoundPlayerDeath ROUND_PLAYER_DEATH = io.github.bfvstats.game.jooq.tables.RoundPlayerDeath.ROUND_PLAYER_DEATH;

    /**
     * The table <code>round_player_score_event</code>.
     */
    public final RoundPlayerScoreEvent ROUND_PLAYER_SCORE_EVENT = io.github.bfvstats.game.jooq.tables.RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT;

    /**
     * The table <code>sqlite_sequence</code>.
     */
    public final SqliteSequence SQLITE_SEQUENCE = io.github.bfvstats.game.jooq.tables.SqliteSequence.SQLITE_SEQUENCE;

    /**
     * No further instances allowed
     */
    private DefaultSchema() {
        super("", null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
            Player.PLAYER,
            PlayerNickname.PLAYER_NICKNAME,
            PlayerRank.PLAYER_RANK,
            Round.ROUND,
            RoundChatLog.ROUND_CHAT_LOG,
            RoundEndStats.ROUND_END_STATS,
            RoundEndStatsPlayer.ROUND_END_STATS_PLAYER,
            RoundPlayerDeath.ROUND_PLAYER_DEATH,
            RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT,
            SqliteSequence.SQLITE_SEQUENCE);
    }
}
