/*
 * This file is generated by jOOQ.
*/
package io.github.bfstats.game.jooq.tables;


import io.github.bfstats.game.jooq.DefaultSchema;
import io.github.bfstats.game.jooq.Keys;
import io.github.bfstats.game.jooq.tables.records.ConfigurationRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
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
public class Configuration extends TableImpl<ConfigurationRecord> {

    private static final long serialVersionUID = -371192880;

    /**
     * The reference instance of <code>configuration</code>
     */
    public static final Configuration CONFIGURATION = new Configuration();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ConfigurationRecord> getRecordType() {
        return ConfigurationRecord.class;
    }

    /**
     * The column <code>configuration.lock</code>.
     */
    public final TableField<ConfigurationRecord, Integer> LOCK = createField("lock", org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("1", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>configuration.last_parsed_datetime</code>.
     */
    public final TableField<ConfigurationRecord, String> LAST_PARSED_DATETIME = createField("last_parsed_datetime", org.jooq.impl.SQLDataType.VARCHAR(30), this, "");

    /**
     * Create a <code>configuration</code> table reference
     */
    public Configuration() {
        this(DSL.name("configuration"), null);
    }

    /**
     * Create an aliased <code>configuration</code> table reference
     */
    public Configuration(String alias) {
        this(DSL.name(alias), CONFIGURATION);
    }

    /**
     * Create an aliased <code>configuration</code> table reference
     */
    public Configuration(Name alias) {
        this(alias, CONFIGURATION);
    }

    private Configuration(Name alias, Table<ConfigurationRecord> aliased) {
        this(alias, aliased, null);
    }

    private Configuration(Name alias, Table<ConfigurationRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<ConfigurationRecord> getPrimaryKey() {
        return Keys.PK_CONFIGURATION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<ConfigurationRecord>> getKeys() {
        return Arrays.<UniqueKey<ConfigurationRecord>>asList(Keys.PK_CONFIGURATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration as(String alias) {
        return new Configuration(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration as(Name alias) {
        return new Configuration(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Configuration rename(String name) {
        return new Configuration(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Configuration rename(Name name) {
        return new Configuration(name, null);
    }
}
