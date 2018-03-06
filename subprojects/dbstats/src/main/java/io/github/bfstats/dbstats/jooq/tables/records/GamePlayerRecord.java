/*
 * This file is generated by jOOQ.
*/
package io.github.bfstats.dbstats.jooq.tables.records;


import io.github.bfstats.dbstats.jooq.tables.GamePlayer;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
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
public class GamePlayerRecord extends UpdatableRecordImpl<GamePlayerRecord> implements Record7<Integer, Integer, Integer, Integer, Integer, Timestamp, Timestamp> {

    private static final long serialVersionUID = -1052924850;

    /**
     * Setter for <code>game_player.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>game_player.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>game_player.game_id</code>.
     */
    public void setGameId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>game_player.game_id</code>.
     */
    public Integer getGameId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>game_player.joined_round_id</code>.
     */
    public void setJoinedRoundId(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>game_player.joined_round_id</code>.
     */
    public Integer getJoinedRoundId() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>game_player.end_round_id</code>.
     */
    public void setEndRoundId(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>game_player.end_round_id</code>.
     */
    public Integer getEndRoundId() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>game_player.player_id</code>.
     */
    public void setPlayerId(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>game_player.player_id</code>.
     */
    public Integer getPlayerId() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>game_player.start_time</code>.
     */
    public void setStartTime(Timestamp value) {
        set(5, value);
    }

    /**
     * Getter for <code>game_player.start_time</code>.
     */
    public Timestamp getStartTime() {
        return (Timestamp) get(5);
    }

    /**
     * Setter for <code>game_player.end_time</code>.
     */
    public void setEndTime(Timestamp value) {
        set(6, value);
    }

    /**
     * Getter for <code>game_player.end_time</code>.
     */
    public Timestamp getEndTime() {
        return (Timestamp) get(6);
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
    // Record7 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row7<Integer, Integer, Integer, Integer, Integer, Timestamp, Timestamp> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row7<Integer, Integer, Integer, Integer, Integer, Timestamp, Timestamp> valuesRow() {
        return (Row7) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return GamePlayer.GAME_PLAYER.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field2() {
        return GamePlayer.GAME_PLAYER.GAME_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field3() {
        return GamePlayer.GAME_PLAYER.JOINED_ROUND_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field4() {
        return GamePlayer.GAME_PLAYER.END_ROUND_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field5() {
        return GamePlayer.GAME_PLAYER.PLAYER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field6() {
        return GamePlayer.GAME_PLAYER.START_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field7() {
        return GamePlayer.GAME_PLAYER.END_TIME;
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
        return getGameId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component3() {
        return getJoinedRoundId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component4() {
        return getEndRoundId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component5() {
        return getPlayerId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component6() {
        return getStartTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component7() {
        return getEndTime();
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
        return getGameId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value3() {
        return getJoinedRoundId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value4() {
        return getEndRoundId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value5() {
        return getPlayerId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value6() {
        return getStartTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value7() {
        return getEndTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GamePlayerRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GamePlayerRecord value2(Integer value) {
        setGameId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GamePlayerRecord value3(Integer value) {
        setJoinedRoundId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GamePlayerRecord value4(Integer value) {
        setEndRoundId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GamePlayerRecord value5(Integer value) {
        setPlayerId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GamePlayerRecord value6(Timestamp value) {
        setStartTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GamePlayerRecord value7(Timestamp value) {
        setEndTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GamePlayerRecord values(Integer value1, Integer value2, Integer value3, Integer value4, Integer value5, Timestamp value6, Timestamp value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached GamePlayerRecord
     */
    public GamePlayerRecord() {
        super(GamePlayer.GAME_PLAYER);
    }

    /**
     * Create a detached, initialised GamePlayerRecord
     */
    public GamePlayerRecord(Integer id, Integer gameId, Integer joinedRoundId, Integer endRoundId, Integer playerId, Timestamp startTime, Timestamp endTime) {
        super(GamePlayer.GAME_PLAYER);

        set(0, id);
        set(1, gameId);
        set(2, joinedRoundId);
        set(3, endRoundId);
        set(4, playerId);
        set(5, startTime);
        set(6, endTime);
    }
}