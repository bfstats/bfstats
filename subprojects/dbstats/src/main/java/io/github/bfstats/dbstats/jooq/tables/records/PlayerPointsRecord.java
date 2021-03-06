/*
 * This file is generated by jOOQ.
*/
package io.github.bfstats.dbstats.jooq.tables.records;


import io.github.bfstats.dbstats.jooq.tables.PlayerPoints;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.TableRecordImpl;


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
public class PlayerPointsRecord extends TableRecordImpl<PlayerPointsRecord> implements Record2<Integer, Object> {

    private static final long serialVersionUID = -749636189;

    /**
     * Setter for <code>player_points.player_id</code>.
     */
    public void setPlayerId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>player_points.player_id</code>.
     */
    public Integer getPlayerId() {
        return (Integer) get(0);
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using <deprecationOnUnknownTypes/> in your code generator configuration.
     */
    @java.lang.Deprecated
    public void setPoints(Object value) {
        set(1, value);
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using <deprecationOnUnknownTypes/> in your code generator configuration.
     */
    @java.lang.Deprecated
    public Object getPoints() {
        return get(1);
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row2<Integer, Object> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row2<Integer, Object> valuesRow() {
        return (Row2) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return PlayerPoints.PLAYER_POINTS.PLAYER_ID;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using <deprecationOnUnknownTypes/> in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public Field<Object> field2() {
        return PlayerPoints.PLAYER_POINTS.POINTS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component1() {
        return getPlayerId();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using <deprecationOnUnknownTypes/> in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public Object component2() {
        return getPoints();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getPlayerId();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using <deprecationOnUnknownTypes/> in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public Object value2() {
        return getPoints();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerPointsRecord value1(Integer value) {
        setPlayerId(value);
        return this;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using <deprecationOnUnknownTypes/> in your code generator configuration.
     */
    @java.lang.Deprecated
    @Override
    public PlayerPointsRecord value2(Object value) {
        setPoints(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerPointsRecord values(Integer value1, Object value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PlayerPointsRecord
     */
    public PlayerPointsRecord() {
        super(PlayerPoints.PLAYER_POINTS);
    }

    /**
     * Create a detached, initialised PlayerPointsRecord
     */
    public PlayerPointsRecord(Integer playerId, Object points) {
        super(PlayerPoints.PLAYER_POINTS);

        set(0, playerId);
        set(1, points);
    }
}
