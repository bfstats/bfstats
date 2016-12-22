/**
 * This class is generated by jOOQ
 */
package io.github.bfvstats.jpa.tables;


import io.github.bfvstats.jpa.DefaultSchema;
import io.github.bfvstats.jpa.Keys;
import io.github.bfvstats.jpa.tables.records.SelectbfPlaytimesRecord;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


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
public class SelectbfPlaytimes extends TableImpl<SelectbfPlaytimesRecord> {

    private static final long serialVersionUID = -1937120340;

    /**
     * The reference instance of <code>selectbf_playtimes</code>
     */
    public static final SelectbfPlaytimes SELECTBF_PLAYTIMES = new SelectbfPlaytimes();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SelectbfPlaytimesRecord> getRecordType() {
        return SelectbfPlaytimesRecord.class;
    }

    /**
     * The column <code>selectbf_playtimes.id</code>.
     */
    public final TableField<SelectbfPlaytimesRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>selectbf_playtimes.player_id</code>.
     */
    public final TableField<SelectbfPlaytimesRecord, Integer> PLAYER_ID = createField("player_id", org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>selectbf_playtimes.last_seen</code>.
     */
    public final TableField<SelectbfPlaytimesRecord, Timestamp> LAST_SEEN = createField("last_seen", org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>selectbf_playtimes.playtime</code>.
     */
    public final TableField<SelectbfPlaytimesRecord, Float> PLAYTIME = createField("playtime", org.jooq.impl.SQLDataType.REAL.defaultValue(org.jooq.impl.DSL.field("'0'", org.jooq.impl.SQLDataType.REAL)), this, "");

    /**
     * The column <code>selectbf_playtimes.slots_used</code>.
     */
    public final TableField<SelectbfPlaytimesRecord, Integer> SLOTS_USED = createField("slots_used", org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("'0'", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * Create a <code>selectbf_playtimes</code> table reference
     */
    public SelectbfPlaytimes() {
        this("selectbf_playtimes", null);
    }

    /**
     * Create an aliased <code>selectbf_playtimes</code> table reference
     */
    public SelectbfPlaytimes(String alias) {
        this(alias, SELECTBF_PLAYTIMES);
    }

    private SelectbfPlaytimes(String alias, Table<SelectbfPlaytimesRecord> aliased) {
        this(alias, aliased, null);
    }

    private SelectbfPlaytimes(String alias, Table<SelectbfPlaytimesRecord> aliased, Field<?>[] parameters) {
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
    public Identity<SelectbfPlaytimesRecord, Integer> getIdentity() {
        return Keys.IDENTITY_SELECTBF_PLAYTIMES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<SelectbfPlaytimesRecord> getPrimaryKey() {
        return Keys.PK_SELECTBF_PLAYTIMES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<SelectbfPlaytimesRecord>> getKeys() {
        return Arrays.<UniqueKey<SelectbfPlaytimesRecord>>asList(Keys.PK_SELECTBF_PLAYTIMES);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectbfPlaytimes as(String alias) {
        return new SelectbfPlaytimes(alias, this);
    }

    /**
     * Rename this table
     */
    public SelectbfPlaytimes rename(String name) {
        return new SelectbfPlaytimes(name, null);
    }
}