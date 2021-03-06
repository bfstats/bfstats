/*
 * This file is generated by jOOQ.
*/
package io.github.bfstats.dbstats.jooq.tables;


import io.github.bfstats.dbstats.jooq.DefaultSchema;
import io.github.bfstats.dbstats.jooq.Indexes;
import io.github.bfstats.dbstats.jooq.Keys;
import io.github.bfstats.dbstats.jooq.tables.records.RoundRecord;

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
public class Round extends TableImpl<RoundRecord> {

    private static final long serialVersionUID = -228137310;

    /**
     * The reference instance of <code>round</code>
     */
    public static final Round ROUND = new Round();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RoundRecord> getRecordType() {
        return RoundRecord.class;
    }

    /**
     * The column <code>round.id</code>.
     */
    public final TableField<RoundRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>round.game_id</code>.
     */
    public final TableField<RoundRecord, Integer> GAME_ID = createField("game_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.start_time</code>.
     */
    public final TableField<RoundRecord, Timestamp> START_TIME = createField("start_time", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * The column <code>round.start_tickets_team_1</code>.
     */
    public final TableField<RoundRecord, Integer> START_TICKETS_TEAM_1 = createField("start_tickets_team_1", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.start_tickets_team_2</code>.
     */
    public final TableField<RoundRecord, Integer> START_TICKETS_TEAM_2 = createField("start_tickets_team_2", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.server_name</code>.
     */
    public final TableField<RoundRecord, String> SERVER_NAME = createField("server_name", org.jooq.impl.SQLDataType.VARCHAR(150).nullable(false), this, "");

    /**
     * The column <code>round.server_port</code>.
     */
    public final TableField<RoundRecord, Integer> SERVER_PORT = createField("server_port", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.game_code</code>.
     */
    public final TableField<RoundRecord, String> GAME_CODE = createField("game_code", org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>round.mod_id</code>.
     */
    public final TableField<RoundRecord, String> MOD_ID = createField("mod_id", org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>round.map_code</code>.
     */
    public final TableField<RoundRecord, String> MAP_CODE = createField("map_code", org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>round.game_mode</code>.
     */
    public final TableField<RoundRecord, String> GAME_MODE = createField("game_mode", org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>round.max_game_time</code>.
     */
    public final TableField<RoundRecord, Integer> MAX_GAME_TIME = createField("max_game_time", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.max_players</code>.
     */
    public final TableField<RoundRecord, Integer> MAX_PLAYERS = createField("max_players", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.score_limit</code>.
     */
    public final TableField<RoundRecord, Integer> SCORE_LIMIT = createField("score_limit", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.no_of_rounds</code>.
     */
    public final TableField<RoundRecord, Integer> NO_OF_ROUNDS = createField("no_of_rounds", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.spawn_time</code>.
     */
    public final TableField<RoundRecord, Integer> SPAWN_TIME = createField("spawn_time", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.spawn_delay</code>.
     */
    public final TableField<RoundRecord, Integer> SPAWN_DELAY = createField("spawn_delay", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.game_start_delay</code>.
     */
    public final TableField<RoundRecord, Integer> GAME_START_DELAY = createField("game_start_delay", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.round_start_delay</code>.
     */
    public final TableField<RoundRecord, Integer> ROUND_START_DELAY = createField("round_start_delay", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.soldier_ff</code>.
     */
    public final TableField<RoundRecord, Integer> SOLDIER_FF = createField("soldier_ff", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.soldier_ff_on_splash</code>.
     */
    public final TableField<RoundRecord, Integer> SOLDIER_FF_ON_SPLASH = createField("soldier_ff_on_splash", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.vehicle_ff</code>.
     */
    public final TableField<RoundRecord, Integer> VEHICLE_FF = createField("vehicle_ff", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.vehicle_ff_on_splash</code>.
     */
    public final TableField<RoundRecord, Integer> VEHICLE_FF_ON_SPLASH = createField("vehicle_ff_on_splash", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.ff_kickback</code>.
     */
    public final TableField<RoundRecord, Integer> FF_KICKBACK = createField("ff_kickback", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.ff_kickback_on_splash</code>.
     */
    public final TableField<RoundRecord, Integer> FF_KICKBACK_ON_SPLASH = createField("ff_kickback_on_splash", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.ticket_ratio</code>.
     */
    public final TableField<RoundRecord, Integer> TICKET_RATIO = createField("ticket_ratio", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.team_kill_punish</code>.
     */
    public final TableField<RoundRecord, Integer> TEAM_KILL_PUNISH = createField("team_kill_punish", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.punkbuster_enabled</code>.
     */
    public final TableField<RoundRecord, Integer> PUNKBUSTER_ENABLED = createField("punkbuster_enabled", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.auto_balance_enabled</code>.
     */
    public final TableField<RoundRecord, Integer> AUTO_BALANCE_ENABLED = createField("auto_balance_enabled", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.tag_distance</code>.
     */
    public final TableField<RoundRecord, Integer> TAG_DISTANCE = createField("tag_distance", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.tag_distance_scope</code>.
     */
    public final TableField<RoundRecord, Integer> TAG_DISTANCE_SCOPE = createField("tag_distance_scope", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.nose_camera_allowed</code>.
     */
    public final TableField<RoundRecord, Integer> NOSE_CAMERA_ALLOWED = createField("nose_camera_allowed", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.free_camera_allowed</code>.
     */
    public final TableField<RoundRecord, Integer> FREE_CAMERA_ALLOWED = createField("free_camera_allowed", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.external_views_allowed</code>.
     */
    public final TableField<RoundRecord, Integer> EXTERNAL_VIEWS_ALLOWED = createField("external_views_allowed", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.hit_indication_enabled</code>.
     */
    public final TableField<RoundRecord, Integer> HIT_INDICATION_ENABLED = createField("hit_indication_enabled", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.internet</code>.
     */
    public final TableField<RoundRecord, Integer> INTERNET = createField("internet", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.coop_cpu</code>.
     */
    public final TableField<RoundRecord, Integer> COOP_CPU = createField("coop_cpu", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.coop_skill</code>.
     */
    public final TableField<RoundRecord, Integer> COOP_SKILL = createField("coop_skill", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.allied_player_count_ratio</code>.
     */
    public final TableField<RoundRecord, Integer> ALLIED_PLAYER_COUNT_RATIO = createField("allied_player_count_ratio", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round.axis_player_count_ratio</code>.
     */
    public final TableField<RoundRecord, Integer> AXIS_PLAYER_COUNT_RATIO = createField("axis_player_count_ratio", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * Create a <code>round</code> table reference
     */
    public Round() {
        this(DSL.name("round"), null);
    }

    /**
     * Create an aliased <code>round</code> table reference
     */
    public Round(String alias) {
        this(DSL.name(alias), ROUND);
    }

    /**
     * Create an aliased <code>round</code> table reference
     */
    public Round(Name alias) {
        this(alias, ROUND);
    }

    private Round(Name alias, Table<RoundRecord> aliased) {
        this(alias, aliased, null);
    }

    private Round(Name alias, Table<RoundRecord> aliased, Field<?>[] parameters) {
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
        return Arrays.<Index>asList(Indexes.ROUND_GAME_ID_IDX);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<RoundRecord, Integer> getIdentity() {
        return Keys.IDENTITY_ROUND;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<RoundRecord> getPrimaryKey() {
        return Keys.PK_ROUND;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<RoundRecord>> getKeys() {
        return Arrays.<UniqueKey<RoundRecord>>asList(Keys.PK_ROUND);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<RoundRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<RoundRecord, ?>>asList(Keys.FK_ROUND_GAME_1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Round as(String alias) {
        return new Round(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Round as(Name alias) {
        return new Round(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Round rename(String name) {
        return new Round(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Round rename(Name name) {
        return new Round(name, null);
    }
}
