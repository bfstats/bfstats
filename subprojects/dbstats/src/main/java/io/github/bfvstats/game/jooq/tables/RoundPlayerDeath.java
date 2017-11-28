/*
 * This file is generated by jOOQ.
*/
package io.github.bfvstats.game.jooq.tables;


import io.github.bfvstats.game.jooq.DefaultSchema;
import io.github.bfvstats.game.jooq.Indexes;
import io.github.bfvstats.game.jooq.Keys;
import io.github.bfvstats.game.jooq.tables.records.RoundPlayerDeathRecord;

import java.math.BigDecimal;
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
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RoundPlayerDeath extends TableImpl<RoundPlayerDeathRecord> {

    private static final long serialVersionUID = -1057965310;

    /**
     * The reference instance of <code>round_player_death</code>
     */
    public static final RoundPlayerDeath ROUND_PLAYER_DEATH = new RoundPlayerDeath();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RoundPlayerDeathRecord> getRecordType() {
        return RoundPlayerDeathRecord.class;
    }

    /**
     * The column <code>round_player_death.id</code>.
     */
    public final TableField<RoundPlayerDeathRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>round_player_death.round_id</code>.
     */
    public final TableField<RoundPlayerDeathRecord, Integer> ROUND_ID = createField("round_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round_player_death.player_id</code>.
     */
    public final TableField<RoundPlayerDeathRecord, Integer> PLAYER_ID = createField("player_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round_player_death.player_location_x</code>.
     */
    public final TableField<RoundPlayerDeathRecord, BigDecimal> PLAYER_LOCATION_X = createField("player_location_x", org.jooq.impl.SQLDataType.NUMERIC(4, 4).nullable(false), this, "");

    /**
     * The column <code>round_player_death.player_location_y</code>.
     */
    public final TableField<RoundPlayerDeathRecord, BigDecimal> PLAYER_LOCATION_Y = createField("player_location_y", org.jooq.impl.SQLDataType.NUMERIC(4, 4).nullable(false), this, "");

    /**
     * The column <code>round_player_death.player_location_z</code>.
     */
    public final TableField<RoundPlayerDeathRecord, BigDecimal> PLAYER_LOCATION_Z = createField("player_location_z", org.jooq.impl.SQLDataType.NUMERIC(4, 4).nullable(false), this, "");

    /**
     * The column <code>round_player_death.event_time</code>.
     */
    public final TableField<RoundPlayerDeathRecord, Timestamp> EVENT_TIME = createField("event_time", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * The column <code>round_player_death.killer_player_id</code>.
     */
    public final TableField<RoundPlayerDeathRecord, Integer> KILLER_PLAYER_ID = createField("killer_player_id", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>round_player_death.killer_location_x</code>.
     */
    public final TableField<RoundPlayerDeathRecord, BigDecimal> KILLER_LOCATION_X = createField("killer_location_x", org.jooq.impl.SQLDataType.NUMERIC(4, 4), this, "");

    /**
     * The column <code>round_player_death.killer_location_y</code>.
     */
    public final TableField<RoundPlayerDeathRecord, BigDecimal> KILLER_LOCATION_Y = createField("killer_location_y", org.jooq.impl.SQLDataType.NUMERIC(4, 4), this, "");

    /**
     * The column <code>round_player_death.killer_location_z</code>.
     */
    public final TableField<RoundPlayerDeathRecord, BigDecimal> KILLER_LOCATION_Z = createField("killer_location_z", org.jooq.impl.SQLDataType.NUMERIC(4, 4), this, "");

    /**
     * The column <code>round_player_death.kill_type</code>.
     */
    public final TableField<RoundPlayerDeathRecord, String> KILL_TYPE = createField("kill_type", org.jooq.impl.SQLDataType.VARCHAR(30), this, "");

    /**
     * The column <code>round_player_death.kill_weapon</code>.
     */
    public final TableField<RoundPlayerDeathRecord, String> KILL_WEAPON = createField("kill_weapon", org.jooq.impl.SQLDataType.VARCHAR(50), this, "");

    /**
     * Create a <code>round_player_death</code> table reference
     */
    public RoundPlayerDeath() {
        this(DSL.name("round_player_death"), null);
    }

    /**
     * Create an aliased <code>round_player_death</code> table reference
     */
    public RoundPlayerDeath(String alias) {
        this(DSL.name(alias), ROUND_PLAYER_DEATH);
    }

    /**
     * Create an aliased <code>round_player_death</code> table reference
     */
    public RoundPlayerDeath(Name alias) {
        this(alias, ROUND_PLAYER_DEATH);
    }

    private RoundPlayerDeath(Name alias, Table<RoundPlayerDeathRecord> aliased) {
        this(alias, aliased, null);
    }

    private RoundPlayerDeath(Name alias, Table<RoundPlayerDeathRecord> aliased, Field<?>[] parameters) {
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
        return Arrays.<Index>asList(Indexes.ROUND_PLAYER_DEATH_PLAYER_ID_IDX, Indexes.ROUND_PLAYER_DEATH_ROUND_ID_IDX, Indexes.ROUND_PLAYER_KILLER_PLAYER_ID_IDX);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<RoundPlayerDeathRecord, Integer> getIdentity() {
        return Keys.IDENTITY_ROUND_PLAYER_DEATH;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<RoundPlayerDeathRecord> getPrimaryKey() {
        return Keys.PK_ROUND_PLAYER_DEATH;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<RoundPlayerDeathRecord>> getKeys() {
        return Arrays.<UniqueKey<RoundPlayerDeathRecord>>asList(Keys.PK_ROUND_PLAYER_DEATH);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<RoundPlayerDeathRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<RoundPlayerDeathRecord, ?>>asList(Keys.FK_ROUND_PLAYER_DEATH_ROUND_1, Keys.FK_ROUND_PLAYER_DEATH_PLAYER_2, Keys.FK_ROUND_PLAYER_DEATH_PLAYER_1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerDeath as(String alias) {
        return new RoundPlayerDeath(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerDeath as(Name alias) {
        return new RoundPlayerDeath(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public RoundPlayerDeath rename(String name) {
        return new RoundPlayerDeath(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public RoundPlayerDeath rename(Name name) {
        return new RoundPlayerDeath(name, null);
    }
}
