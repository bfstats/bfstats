/*
 * This file is generated by jOOQ.
*/
package io.github.bfstats.dbstats.jooq.tables;


import io.github.bfstats.dbstats.jooq.DefaultSchema;
import io.github.bfstats.dbstats.jooq.Indexes;
import io.github.bfstats.dbstats.jooq.Keys;
import io.github.bfstats.dbstats.jooq.tables.records.GameRecord;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Game extends TableImpl<GameRecord> {

    private static final long serialVersionUID = -371377392;

    /**
     * The reference instance of <code>game</code>
     */
    public static final Game GAME = new Game();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<GameRecord> getRecordType() {
        return GameRecord.class;
    }

    /**
     * The column <code>game.id</code>.
     */
    public final TableField<GameRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>game.game_code</code>.
     */
    public final TableField<GameRecord, String> GAME_CODE = createField("game_code", org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>game.server_id</code>.
     */
    public final TableField<GameRecord, Integer> SERVER_ID = createField("server_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.start_time</code>.
     */
    public final TableField<GameRecord, Timestamp> START_TIME = createField("start_time", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * The column <code>game.server_name</code>.
     */
    public final TableField<GameRecord, String> SERVER_NAME = createField("server_name", org.jooq.impl.SQLDataType.VARCHAR(150).nullable(false), this, "");

    /**
     * The column <code>game.server_port</code>.
     */
    public final TableField<GameRecord, Integer> SERVER_PORT = createField("server_port", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.mod_id</code>.
     */
    public final TableField<GameRecord, String> MOD_ID = createField("mod_id", org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>game.map_code</code>.
     */
    public final TableField<GameRecord, String> MAP_CODE = createField("map_code", org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>game.game_mode</code>.
     */
    public final TableField<GameRecord, String> GAME_MODE = createField("game_mode", org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>game.max_game_time</code>.
     */
    public final TableField<GameRecord, Integer> MAX_GAME_TIME = createField("max_game_time", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.max_players</code>.
     */
    public final TableField<GameRecord, Integer> MAX_PLAYERS = createField("max_players", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.score_limit</code>.
     */
    public final TableField<GameRecord, Integer> SCORE_LIMIT = createField("score_limit", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.no_of_rounds</code>.
     */
    public final TableField<GameRecord, Integer> NO_OF_ROUNDS = createField("no_of_rounds", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.spawn_time</code>.
     */
    public final TableField<GameRecord, Integer> SPAWN_TIME = createField("spawn_time", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.spawn_delay</code>.
     */
    public final TableField<GameRecord, Integer> SPAWN_DELAY = createField("spawn_delay", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.game_start_delay</code>.
     */
    public final TableField<GameRecord, Integer> GAME_START_DELAY = createField("game_start_delay", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.round_start_delay</code>.
     */
    public final TableField<GameRecord, Integer> ROUND_START_DELAY = createField("round_start_delay", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.soldier_ff</code>.
     */
    public final TableField<GameRecord, Integer> SOLDIER_FF = createField("soldier_ff", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.soldier_ff_on_splash</code>.
     */
    public final TableField<GameRecord, Integer> SOLDIER_FF_ON_SPLASH = createField("soldier_ff_on_splash", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.vehicle_ff</code>.
     */
    public final TableField<GameRecord, Integer> VEHICLE_FF = createField("vehicle_ff", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.vehicle_ff_on_splash</code>.
     */
    public final TableField<GameRecord, Integer> VEHICLE_FF_ON_SPLASH = createField("vehicle_ff_on_splash", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.ff_kickback</code>.
     */
    public final TableField<GameRecord, Integer> FF_KICKBACK = createField("ff_kickback", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.ff_kickback_on_splash</code>.
     */
    public final TableField<GameRecord, Integer> FF_KICKBACK_ON_SPLASH = createField("ff_kickback_on_splash", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.ticket_ratio</code>.
     */
    public final TableField<GameRecord, Integer> TICKET_RATIO = createField("ticket_ratio", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.team_kill_punish</code>.
     */
    public final TableField<GameRecord, Integer> TEAM_KILL_PUNISH = createField("team_kill_punish", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.punkbuster_enabled</code>.
     */
    public final TableField<GameRecord, Integer> PUNKBUSTER_ENABLED = createField("punkbuster_enabled", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.auto_balance_enabled</code>.
     */
    public final TableField<GameRecord, Integer> AUTO_BALANCE_ENABLED = createField("auto_balance_enabled", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.tag_distance</code>.
     */
    public final TableField<GameRecord, Integer> TAG_DISTANCE = createField("tag_distance", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.tag_distance_scope</code>.
     */
    public final TableField<GameRecord, Integer> TAG_DISTANCE_SCOPE = createField("tag_distance_scope", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.nose_camera_allowed</code>.
     */
    public final TableField<GameRecord, Integer> NOSE_CAMERA_ALLOWED = createField("nose_camera_allowed", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.free_camera_allowed</code>.
     */
    public final TableField<GameRecord, Integer> FREE_CAMERA_ALLOWED = createField("free_camera_allowed", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.external_views_allowed</code>.
     */
    public final TableField<GameRecord, Integer> EXTERNAL_VIEWS_ALLOWED = createField("external_views_allowed", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.hit_indication_enabled</code>.
     */
    public final TableField<GameRecord, Integer> HIT_INDICATION_ENABLED = createField("hit_indication_enabled", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.internet</code>.
     */
    public final TableField<GameRecord, Integer> INTERNET = createField("internet", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.coop_cpu</code>.
     */
    public final TableField<GameRecord, Integer> COOP_CPU = createField("coop_cpu", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.coop_skill</code>.
     */
    public final TableField<GameRecord, Integer> COOP_SKILL = createField("coop_skill", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.allied_player_count_ratio</code>.
     */
    public final TableField<GameRecord, Integer> ALLIED_PLAYER_COUNT_RATIO = createField("allied_player_count_ratio", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>game.axis_player_count_ratio</code>.
     */
    public final TableField<GameRecord, Integer> AXIS_PLAYER_COUNT_RATIO = createField("axis_player_count_ratio", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * Create a <code>game</code> table reference
     */
    public Game() {
        this(DSL.name("game"), null);
    }

    /**
     * Create an aliased <code>game</code> table reference
     */
    public Game(String alias) {
        this(DSL.name(alias), GAME);
    }

    /**
     * Create an aliased <code>game</code> table reference
     */
    public Game(Name alias) {
        this(alias, GAME);
    }

    private Game(Name alias, Table<GameRecord> aliased) {
        this(alias, aliased, null);
    }

    private Game(Name alias, Table<GameRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.GAME_SERVER_ID_IDX);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<GameRecord, Integer> getIdentity() {
        return Keys.IDENTITY_GAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<GameRecord> getPrimaryKey() {
        return Keys.PK_GAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<GameRecord>> getKeys() {
        return Arrays.<UniqueKey<GameRecord>>asList(Keys.PK_GAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<GameRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<GameRecord, ?>>asList(Keys.FK_GAME_SERVER_1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Game as(String alias) {
        return new Game(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Game as(Name alias) {
        return new Game(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Game rename(String name) {
        return new Game(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Game rename(Name name) {
        return new Game(name, null);
    }
}
