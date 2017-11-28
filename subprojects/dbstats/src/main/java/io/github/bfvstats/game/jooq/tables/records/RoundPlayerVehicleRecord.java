/*
 * This file is generated by jOOQ.
*/
package io.github.bfvstats.game.jooq.tables.records;


import io.github.bfvstats.game.jooq.tables.RoundPlayerVehicle;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record10;
import org.jooq.Row10;
import org.jooq.impl.UpdatableRecordImpl;


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
public class RoundPlayerVehicleRecord extends UpdatableRecordImpl<RoundPlayerVehicleRecord> implements Record10<Integer, Integer, Integer, BigDecimal, BigDecimal, BigDecimal, Timestamp, Timestamp, Integer, String> {

    private static final long serialVersionUID = 1117731685;

    /**
     * Setter for <code>round_player_vehicle.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>round_player_vehicle.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>round_player_vehicle.round_id</code>.
     */
    public void setRoundId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>round_player_vehicle.round_id</code>.
     */
    public Integer getRoundId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>round_player_vehicle.player_id</code>.
     */
    public void setPlayerId(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>round_player_vehicle.player_id</code>.
     */
    public Integer getPlayerId() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>round_player_vehicle.player_location_x</code>.
     */
    public void setPlayerLocationX(BigDecimal value) {
        set(3, value);
    }

    /**
     * Getter for <code>round_player_vehicle.player_location_x</code>.
     */
    public BigDecimal getPlayerLocationX() {
        return (BigDecimal) get(3);
    }

    /**
     * Setter for <code>round_player_vehicle.player_location_y</code>.
     */
    public void setPlayerLocationY(BigDecimal value) {
        set(4, value);
    }

    /**
     * Getter for <code>round_player_vehicle.player_location_y</code>.
     */
    public BigDecimal getPlayerLocationY() {
        return (BigDecimal) get(4);
    }

    /**
     * Setter for <code>round_player_vehicle.player_location_z</code>.
     */
    public void setPlayerLocationZ(BigDecimal value) {
        set(5, value);
    }

    /**
     * Getter for <code>round_player_vehicle.player_location_z</code>.
     */
    public BigDecimal getPlayerLocationZ() {
        return (BigDecimal) get(5);
    }

    /**
     * Setter for <code>round_player_vehicle.start_time</code>.
     */
    public void setStartTime(Timestamp value) {
        set(6, value);
    }

    /**
     * Getter for <code>round_player_vehicle.start_time</code>.
     */
    public Timestamp getStartTime() {
        return (Timestamp) get(6);
    }

    /**
     * Setter for <code>round_player_vehicle.end_time</code>.
     */
    public void setEndTime(Timestamp value) {
        set(7, value);
    }

    /**
     * Getter for <code>round_player_vehicle.end_time</code>.
     */
    public Timestamp getEndTime() {
        return (Timestamp) get(7);
    }

    /**
     * Setter for <code>round_player_vehicle.duration_seconds</code>.
     */
    public void setDurationSeconds(Integer value) {
        set(8, value);
    }

    /**
     * Getter for <code>round_player_vehicle.duration_seconds</code>.
     */
    public Integer getDurationSeconds() {
        return (Integer) get(8);
    }

    /**
     * Setter for <code>round_player_vehicle.vehicle</code>.
     */
    public void setVehicle(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>round_player_vehicle.vehicle</code>.
     */
    public String getVehicle() {
        return (String) get(9);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record10 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row10<Integer, Integer, Integer, BigDecimal, BigDecimal, BigDecimal, Timestamp, Timestamp, Integer, String> fieldsRow() {
        return (Row10) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row10<Integer, Integer, Integer, BigDecimal, BigDecimal, BigDecimal, Timestamp, Timestamp, Integer, String> valuesRow() {
        return (Row10) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return RoundPlayerVehicle.ROUND_PLAYER_VEHICLE.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field2() {
        return RoundPlayerVehicle.ROUND_PLAYER_VEHICLE.ROUND_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field3() {
        return RoundPlayerVehicle.ROUND_PLAYER_VEHICLE.PLAYER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field4() {
        return RoundPlayerVehicle.ROUND_PLAYER_VEHICLE.PLAYER_LOCATION_X;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field5() {
        return RoundPlayerVehicle.ROUND_PLAYER_VEHICLE.PLAYER_LOCATION_Y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field6() {
        return RoundPlayerVehicle.ROUND_PLAYER_VEHICLE.PLAYER_LOCATION_Z;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field7() {
        return RoundPlayerVehicle.ROUND_PLAYER_VEHICLE.START_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field8() {
        return RoundPlayerVehicle.ROUND_PLAYER_VEHICLE.END_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field9() {
        return RoundPlayerVehicle.ROUND_PLAYER_VEHICLE.DURATION_SECONDS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field10() {
        return RoundPlayerVehicle.ROUND_PLAYER_VEHICLE.VEHICLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component2() {
        return getRoundId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component3() {
        return getPlayerId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal component4() {
        return getPlayerLocationX();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal component5() {
        return getPlayerLocationY();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal component6() {
        return getPlayerLocationZ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component7() {
        return getStartTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component8() {
        return getEndTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component9() {
        return getDurationSeconds();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component10() {
        return getVehicle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value2() {
        return getRoundId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value3() {
        return getPlayerId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value4() {
        return getPlayerLocationX();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value5() {
        return getPlayerLocationY();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value6() {
        return getPlayerLocationZ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value7() {
        return getStartTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value8() {
        return getEndTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value9() {
        return getDurationSeconds();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value10() {
        return getVehicle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerVehicleRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerVehicleRecord value2(Integer value) {
        setRoundId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerVehicleRecord value3(Integer value) {
        setPlayerId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerVehicleRecord value4(BigDecimal value) {
        setPlayerLocationX(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerVehicleRecord value5(BigDecimal value) {
        setPlayerLocationY(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerVehicleRecord value6(BigDecimal value) {
        setPlayerLocationZ(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerVehicleRecord value7(Timestamp value) {
        setStartTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerVehicleRecord value8(Timestamp value) {
        setEndTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerVehicleRecord value9(Integer value) {
        setDurationSeconds(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerVehicleRecord value10(String value) {
        setVehicle(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerVehicleRecord values(Integer value1, Integer value2, Integer value3, BigDecimal value4, BigDecimal value5, BigDecimal value6, Timestamp value7, Timestamp value8, Integer value9, String value10) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RoundPlayerVehicleRecord
     */
    public RoundPlayerVehicleRecord() {
        super(RoundPlayerVehicle.ROUND_PLAYER_VEHICLE);
    }

    /**
     * Create a detached, initialised RoundPlayerVehicleRecord
     */
    public RoundPlayerVehicleRecord(Integer id, Integer roundId, Integer playerId, BigDecimal playerLocationX, BigDecimal playerLocationY, BigDecimal playerLocationZ, Timestamp startTime, Timestamp endTime, Integer durationSeconds, String vehicle) {
        super(RoundPlayerVehicle.ROUND_PLAYER_VEHICLE);

        set(0, id);
        set(1, roundId);
        set(2, playerId);
        set(3, playerLocationX);
        set(4, playerLocationY);
        set(5, playerLocationZ);
        set(6, startTime);
        set(7, endTime);
        set(8, durationSeconds);
        set(9, vehicle);
    }
}
