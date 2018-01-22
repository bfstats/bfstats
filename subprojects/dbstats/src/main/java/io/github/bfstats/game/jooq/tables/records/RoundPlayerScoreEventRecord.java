/*
 * This file is generated by jOOQ.
*/
package io.github.bfstats.game.jooq.tables.records;


import io.github.bfstats.game.jooq.tables.RoundPlayerScoreEvent;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;


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
public class RoundPlayerScoreEventRecord extends UpdatableRecordImpl<RoundPlayerScoreEventRecord> implements Record8<Integer, Integer, Integer, BigDecimal, BigDecimal, BigDecimal, Timestamp, String> {

    private static final long serialVersionUID = -158466470;

    /**
     * Setter for <code>round_player_score_event.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>round_player_score_event.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>round_player_score_event.round_id</code>.
     */
    public void setRoundId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>round_player_score_event.round_id</code>.
     */
    public Integer getRoundId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>round_player_score_event.player_id</code>.
     */
    public void setPlayerId(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>round_player_score_event.player_id</code>.
     */
    public Integer getPlayerId() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>round_player_score_event.player_location_x</code>.
     */
    public void setPlayerLocationX(BigDecimal value) {
        set(3, value);
    }

    /**
     * Getter for <code>round_player_score_event.player_location_x</code>.
     */
    public BigDecimal getPlayerLocationX() {
        return (BigDecimal) get(3);
    }

    /**
     * Setter for <code>round_player_score_event.player_location_y</code>.
     */
    public void setPlayerLocationY(BigDecimal value) {
        set(4, value);
    }

    /**
     * Getter for <code>round_player_score_event.player_location_y</code>.
     */
    public BigDecimal getPlayerLocationY() {
        return (BigDecimal) get(4);
    }

    /**
     * Setter for <code>round_player_score_event.player_location_z</code>.
     */
    public void setPlayerLocationZ(BigDecimal value) {
        set(5, value);
    }

    /**
     * Getter for <code>round_player_score_event.player_location_z</code>.
     */
    public BigDecimal getPlayerLocationZ() {
        return (BigDecimal) get(5);
    }

    /**
     * Setter for <code>round_player_score_event.event_time</code>.
     */
    public void setEventTime(Timestamp value) {
        set(6, value);
    }

    /**
     * Getter for <code>round_player_score_event.event_time</code>.
     */
    public Timestamp getEventTime() {
        return (Timestamp) get(6);
    }

    /**
     * Setter for <code>round_player_score_event.score_type</code>.
     */
    public void setScoreType(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>round_player_score_event.score_type</code>.
     */
    public String getScoreType() {
        return (String) get(7);
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
    // Record8 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<Integer, Integer, Integer, BigDecimal, BigDecimal, BigDecimal, Timestamp, String> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<Integer, Integer, Integer, BigDecimal, BigDecimal, BigDecimal, Timestamp, String> valuesRow() {
        return (Row8) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field2() {
        return RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT.ROUND_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field3() {
        return RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT.PLAYER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field4() {
        return RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT.PLAYER_LOCATION_X;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field5() {
        return RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT.PLAYER_LOCATION_Y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field6() {
        return RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT.PLAYER_LOCATION_Z;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field7() {
        return RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT.EVENT_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT.SCORE_TYPE;
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
        return getEventTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component8() {
        return getScoreType();
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
        return getEventTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getScoreType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerScoreEventRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerScoreEventRecord value2(Integer value) {
        setRoundId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerScoreEventRecord value3(Integer value) {
        setPlayerId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerScoreEventRecord value4(BigDecimal value) {
        setPlayerLocationX(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerScoreEventRecord value5(BigDecimal value) {
        setPlayerLocationY(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerScoreEventRecord value6(BigDecimal value) {
        setPlayerLocationZ(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerScoreEventRecord value7(Timestamp value) {
        setEventTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerScoreEventRecord value8(String value) {
        setScoreType(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerScoreEventRecord values(Integer value1, Integer value2, Integer value3, BigDecimal value4, BigDecimal value5, BigDecimal value6, Timestamp value7, String value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RoundPlayerScoreEventRecord
     */
    public RoundPlayerScoreEventRecord() {
        super(RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT);
    }

    /**
     * Create a detached, initialised RoundPlayerScoreEventRecord
     */
    public RoundPlayerScoreEventRecord(Integer id, Integer roundId, Integer playerId, BigDecimal playerLocationX, BigDecimal playerLocationY, BigDecimal playerLocationZ, Timestamp eventTime, String scoreType) {
        super(RoundPlayerScoreEvent.ROUND_PLAYER_SCORE_EVENT);

        set(0, id);
        set(1, roundId);
        set(2, playerId);
        set(3, playerLocationX);
        set(4, playerLocationY);
        set(5, playerLocationZ);
        set(6, eventTime);
        set(7, scoreType);
    }
}
