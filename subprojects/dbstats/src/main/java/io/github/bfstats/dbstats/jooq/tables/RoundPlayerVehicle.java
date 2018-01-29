/*
 * This file is generated by jOOQ.
*/
package io.github.bfstats.dbstats.jooq.tables;


import io.github.bfstats.dbstats.jooq.DefaultSchema;
import io.github.bfstats.dbstats.jooq.Indexes;
import io.github.bfstats.dbstats.jooq.Keys;
import io.github.bfstats.dbstats.jooq.tables.records.RoundPlayerVehicleRecord;

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
        "jOOQ version:3.10.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RoundPlayerVehicle extends TableImpl<RoundPlayerVehicleRecord> {

    private static final long serialVersionUID = 1383593054;

    /**
     * The reference instance of <code>round_player_vehicle</code>
     */
    public static final RoundPlayerVehicle ROUND_PLAYER_VEHICLE = new RoundPlayerVehicle();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RoundPlayerVehicleRecord> getRecordType() {
        return RoundPlayerVehicleRecord.class;
    }

    /**
     * The column <code>round_player_vehicle.id</code>.
     */
    public final TableField<RoundPlayerVehicleRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>round_player_vehicle.round_id</code>.
     */
    public final TableField<RoundPlayerVehicleRecord, Integer> ROUND_ID = createField("round_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round_player_vehicle.player_id</code>.
     */
    public final TableField<RoundPlayerVehicleRecord, Integer> PLAYER_ID = createField("player_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round_player_vehicle.player_location_x</code>.
     */
    public final TableField<RoundPlayerVehicleRecord, BigDecimal> PLAYER_LOCATION_X = createField("player_location_x", org.jooq.impl.SQLDataType.NUMERIC(4, 4).nullable(false), this, "");

    /**
     * The column <code>round_player_vehicle.player_location_y</code>.
     */
    public final TableField<RoundPlayerVehicleRecord, BigDecimal> PLAYER_LOCATION_Y = createField("player_location_y", org.jooq.impl.SQLDataType.NUMERIC(4, 4).nullable(false), this, "");

    /**
     * The column <code>round_player_vehicle.player_location_z</code>.
     */
    public final TableField<RoundPlayerVehicleRecord, BigDecimal> PLAYER_LOCATION_Z = createField("player_location_z", org.jooq.impl.SQLDataType.NUMERIC(4, 4).nullable(false), this, "");

    /**
     * The column <code>round_player_vehicle.start_time</code>.
     */
    public final TableField<RoundPlayerVehicleRecord, Timestamp> START_TIME = createField("start_time", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * The column <code>round_player_vehicle.end_time</code>.
     */
    public final TableField<RoundPlayerVehicleRecord, Timestamp> END_TIME = createField("end_time", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * The column <code>round_player_vehicle.duration_seconds</code>.
     */
    public final TableField<RoundPlayerVehicleRecord, Integer> DURATION_SECONDS = createField("duration_seconds", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round_player_vehicle.vehicle</code>.
     */
    public final TableField<RoundPlayerVehicleRecord, String> VEHICLE = createField("vehicle", org.jooq.impl.SQLDataType.VARCHAR(50), this, "");

    /**
     * Create a <code>round_player_vehicle</code> table reference
     */
    public RoundPlayerVehicle() {
        this(DSL.name("round_player_vehicle"), null);
    }

    /**
     * Create an aliased <code>round_player_vehicle</code> table reference
     */
    public RoundPlayerVehicle(String alias) {
        this(DSL.name(alias), ROUND_PLAYER_VEHICLE);
    }

    /**
     * Create an aliased <code>round_player_vehicle</code> table reference
     */
    public RoundPlayerVehicle(Name alias) {
        this(alias, ROUND_PLAYER_VEHICLE);
    }

    private RoundPlayerVehicle(Name alias, Table<RoundPlayerVehicleRecord> aliased) {
        this(alias, aliased, null);
    }

    private RoundPlayerVehicle(Name alias, Table<RoundPlayerVehicleRecord> aliased, Field<?>[] parameters) {
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
        return Arrays.<Index>asList(Indexes.ROUND_PLAYER_VEHICLE_PLAYER_ID_IDX, Indexes.ROUND_PLAYER_VEHICLE_ROUND_ID_IDX);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<RoundPlayerVehicleRecord, Integer> getIdentity() {
        return Keys.IDENTITY_ROUND_PLAYER_VEHICLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<RoundPlayerVehicleRecord> getPrimaryKey() {
        return Keys.PK_ROUND_PLAYER_VEHICLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<RoundPlayerVehicleRecord>> getKeys() {
        return Arrays.<UniqueKey<RoundPlayerVehicleRecord>>asList(Keys.PK_ROUND_PLAYER_VEHICLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<RoundPlayerVehicleRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<RoundPlayerVehicleRecord, ?>>asList(Keys.FK_ROUND_PLAYER_VEHICLE_ROUND_1, Keys.FK_ROUND_PLAYER_VEHICLE_PLAYER_1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerVehicle as(String alias) {
        return new RoundPlayerVehicle(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerVehicle as(Name alias) {
        return new RoundPlayerVehicle(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public RoundPlayerVehicle rename(String name) {
        return new RoundPlayerVehicle(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public RoundPlayerVehicle rename(Name name) {
        return new RoundPlayerVehicle(name, null);
    }
}