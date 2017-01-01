/**
 * This class is generated by jOOQ
 */
package io.github.bfvstats.game.jooq.tables.records;


import io.github.bfvstats.game.jooq.tables.RoundChatLog;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.UpdatableRecordImpl;


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
public class RoundChatLogRecord extends UpdatableRecordImpl<RoundChatLogRecord> implements Record9<Integer, Integer, Integer, Float, Float, Float, Integer, String, Timestamp> {

    private static final long serialVersionUID = 1333372964;

    /**
     * Setter for <code>round_chat_log.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>round_chat_log.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>round_chat_log.round_id</code>.
     */
    public void setRoundId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>round_chat_log.round_id</code>.
     */
    public Integer getRoundId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>round_chat_log.player_id</code>.
     */
    public void setPlayerId(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>round_chat_log.player_id</code>.
     */
    public Integer getPlayerId() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>round_chat_log.player_location_x</code>.
     */
    public void setPlayerLocationX(Float value) {
        set(3, value);
    }

    /**
     * Getter for <code>round_chat_log.player_location_x</code>.
     */
    public Float getPlayerLocationX() {
        return (Float) get(3);
    }

    /**
     * Setter for <code>round_chat_log.player_location_y</code>.
     */
    public void setPlayerLocationY(Float value) {
        set(4, value);
    }

    /**
     * Getter for <code>round_chat_log.player_location_y</code>.
     */
    public Float getPlayerLocationY() {
        return (Float) get(4);
    }

    /**
     * Setter for <code>round_chat_log.player_location_z</code>.
     */
    public void setPlayerLocationZ(Float value) {
        set(5, value);
    }

    /**
     * Getter for <code>round_chat_log.player_location_z</code>.
     */
    public Float getPlayerLocationZ() {
        return (Float) get(5);
    }

    /**
     * Setter for <code>round_chat_log.team</code>.
     */
    public void setTeam(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>round_chat_log.team</code>.
     */
    public Integer getTeam() {
        return (Integer) get(6);
    }

    /**
     * Setter for <code>round_chat_log.message</code>.
     */
    public void setMessage(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>round_chat_log.message</code>.
     */
    public String getMessage() {
        return (String) get(7);
    }

    /**
     * Setter for <code>round_chat_log.event_time</code>.
     */
    public void setEventTime(Timestamp value) {
        set(8, value);
    }

    /**
     * Getter for <code>round_chat_log.event_time</code>.
     */
    public Timestamp getEventTime() {
        return (Timestamp) get(8);
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
    // Record9 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row9<Integer, Integer, Integer, Float, Float, Float, Integer, String, Timestamp> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row9<Integer, Integer, Integer, Float, Float, Float, Integer, String, Timestamp> valuesRow() {
        return (Row9) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return RoundChatLog.ROUND_CHAT_LOG.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field2() {
        return RoundChatLog.ROUND_CHAT_LOG.ROUND_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field3() {
        return RoundChatLog.ROUND_CHAT_LOG.PLAYER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Float> field4() {
        return RoundChatLog.ROUND_CHAT_LOG.PLAYER_LOCATION_X;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Float> field5() {
        return RoundChatLog.ROUND_CHAT_LOG.PLAYER_LOCATION_Y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Float> field6() {
        return RoundChatLog.ROUND_CHAT_LOG.PLAYER_LOCATION_Z;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field7() {
        return RoundChatLog.ROUND_CHAT_LOG.TEAM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return RoundChatLog.ROUND_CHAT_LOG.MESSAGE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field9() {
        return RoundChatLog.ROUND_CHAT_LOG.EVENT_TIME;
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
    public Float value4() {
        return getPlayerLocationX();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Float value5() {
        return getPlayerLocationY();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Float value6() {
        return getPlayerLocationZ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value7() {
        return getTeam();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value9() {
        return getEventTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundChatLogRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundChatLogRecord value2(Integer value) {
        setRoundId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundChatLogRecord value3(Integer value) {
        setPlayerId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundChatLogRecord value4(Float value) {
        setPlayerLocationX(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundChatLogRecord value5(Float value) {
        setPlayerLocationY(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundChatLogRecord value6(Float value) {
        setPlayerLocationZ(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundChatLogRecord value7(Integer value) {
        setTeam(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundChatLogRecord value8(String value) {
        setMessage(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundChatLogRecord value9(Timestamp value) {
        setEventTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundChatLogRecord values(Integer value1, Integer value2, Integer value3, Float value4, Float value5, Float value6, Integer value7, String value8, Timestamp value9) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RoundChatLogRecord
     */
    public RoundChatLogRecord() {
        super(RoundChatLog.ROUND_CHAT_LOG);
    }

    /**
     * Create a detached, initialised RoundChatLogRecord
     */
    public RoundChatLogRecord(Integer id, Integer roundId, Integer playerId, Float playerLocationX, Float playerLocationY, Float playerLocationZ, Integer team, String message, Timestamp eventTime) {
        super(RoundChatLog.ROUND_CHAT_LOG);

        set(0, id);
        set(1, roundId);
        set(2, playerId);
        set(3, playerLocationX);
        set(4, playerLocationY);
        set(5, playerLocationZ);
        set(6, team);
        set(7, message);
        set(8, eventTime);
    }
}