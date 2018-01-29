/*
 * This file is generated by jOOQ.
*/
package io.github.bfstats.dbstats.jooq.tables.records;


import io.github.bfstats.dbstats.jooq.tables.Configuration;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
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
public class ConfigurationRecord extends UpdatableRecordImpl<ConfigurationRecord> implements Record2<Integer, String> {

    private static final long serialVersionUID = 1750799979;

    /**
     * Setter for <code>configuration.lock</code>.
     */
    public void setLock(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>configuration.lock</code>.
     */
    public Integer getLock() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>configuration.last_parsed_datetime</code>.
     */
    public void setLastParsedDatetime(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>configuration.last_parsed_datetime</code>.
     */
    public String getLastParsedDatetime() {
        return (String) get(1);
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
    // Record2 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row2<Integer, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return Configuration.CONFIGURATION.LOCK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Configuration.CONFIGURATION.LAST_PARSED_DATETIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component1() {
        return getLock();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getLastParsedDatetime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getLock();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getLastParsedDatetime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConfigurationRecord value1(Integer value) {
        setLock(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConfigurationRecord value2(String value) {
        setLastParsedDatetime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConfigurationRecord values(Integer value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ConfigurationRecord
     */
    public ConfigurationRecord() {
        super(Configuration.CONFIGURATION);
    }

    /**
     * Create a detached, initialised ConfigurationRecord
     */
    public ConfigurationRecord(Integer lock, String lastParsedDatetime) {
        super(Configuration.CONFIGURATION);

        set(0, lock);
        set(1, lastParsedDatetime);
    }
}