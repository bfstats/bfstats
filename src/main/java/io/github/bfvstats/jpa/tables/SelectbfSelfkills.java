/**
 * This class is generated by jOOQ
 */
package io.github.bfvstats.jpa.tables;


import io.github.bfvstats.jpa.DefaultSchema;
import io.github.bfvstats.jpa.Keys;
import io.github.bfvstats.jpa.tables.records.SelectbfSelfkillsRecord;

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
public class SelectbfSelfkills extends TableImpl<SelectbfSelfkillsRecord> {

    private static final long serialVersionUID = 1565491164;

    /**
     * The reference instance of <code>selectbf_selfkills</code>
     */
    public static final SelectbfSelfkills SELECTBF_SELFKILLS = new SelectbfSelfkills();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SelectbfSelfkillsRecord> getRecordType() {
        return SelectbfSelfkillsRecord.class;
    }

    /**
     * The column <code>selectbf_selfkills.id</code>.
     */
    public final TableField<SelectbfSelfkillsRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>selectbf_selfkills.player_id</code>.
     */
    public final TableField<SelectbfSelfkillsRecord, Integer> PLAYER_ID = createField("player_id", org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>selectbf_selfkills.times_killed</code>.
     */
    public final TableField<SelectbfSelfkillsRecord, Integer> TIMES_KILLED = createField("times_killed", org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * Create a <code>selectbf_selfkills</code> table reference
     */
    public SelectbfSelfkills() {
        this("selectbf_selfkills", null);
    }

    /**
     * Create an aliased <code>selectbf_selfkills</code> table reference
     */
    public SelectbfSelfkills(String alias) {
        this(alias, SELECTBF_SELFKILLS);
    }

    private SelectbfSelfkills(String alias, Table<SelectbfSelfkillsRecord> aliased) {
        this(alias, aliased, null);
    }

    private SelectbfSelfkills(String alias, Table<SelectbfSelfkillsRecord> aliased, Field<?>[] parameters) {
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
    public Identity<SelectbfSelfkillsRecord, Integer> getIdentity() {
        return Keys.IDENTITY_SELECTBF_SELFKILLS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<SelectbfSelfkillsRecord> getPrimaryKey() {
        return Keys.PK_SELECTBF_SELFKILLS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<SelectbfSelfkillsRecord>> getKeys() {
        return Arrays.<UniqueKey<SelectbfSelfkillsRecord>>asList(Keys.PK_SELECTBF_SELFKILLS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectbfSelfkills as(String alias) {
        return new SelectbfSelfkills(alias, this);
    }

    /**
     * Rename this table
     */
    public SelectbfSelfkills rename(String name) {
        return new SelectbfSelfkills(name, null);
    }
}