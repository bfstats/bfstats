/**
 * This class is generated by jOOQ
 */
package io.github.bfvstats.jpa.tables;


import io.github.bfvstats.jpa.DefaultSchema;
import io.github.bfvstats.jpa.Keys;
import io.github.bfvstats.jpa.tables.records.SelectbfParamsRecord;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
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
public class SelectbfParams extends TableImpl<SelectbfParamsRecord> {

    private static final long serialVersionUID = -1517207237;

    /**
     * The reference instance of <code>selectbf_params</code>
     */
    public static final SelectbfParams SELECTBF_PARAMS = new SelectbfParams();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SelectbfParamsRecord> getRecordType() {
        return SelectbfParamsRecord.class;
    }

    /**
     * The column <code>selectbf_params.id</code>.
     */
    public final TableField<SelectbfParamsRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>selectbf_params.name</code>.
     */
    public final TableField<SelectbfParamsRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR.length(50).defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>selectbf_params.value</code>.
     */
    public final TableField<SelectbfParamsRecord, String> VALUE = createField("value", org.jooq.impl.SQLDataType.VARCHAR.length(255).defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>selectbf_params.inserttime</code>.
     */
    public final TableField<SelectbfParamsRecord, Timestamp> INSERTTIME = createField("inserttime", org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * Create a <code>selectbf_params</code> table reference
     */
    public SelectbfParams() {
        this("selectbf_params", null);
    }

    /**
     * Create an aliased <code>selectbf_params</code> table reference
     */
    public SelectbfParams(String alias) {
        this(alias, SELECTBF_PARAMS);
    }

    private SelectbfParams(String alias, Table<SelectbfParamsRecord> aliased) {
        this(alias, aliased, null);
    }

    private SelectbfParams(String alias, Table<SelectbfParamsRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<SelectbfParamsRecord> getPrimaryKey() {
        return Keys.PK_SELECTBF_PARAMS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<SelectbfParamsRecord>> getKeys() {
        return Arrays.<UniqueKey<SelectbfParamsRecord>>asList(Keys.PK_SELECTBF_PARAMS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectbfParams as(String alias) {
        return new SelectbfParams(alias, this);
    }

    /**
     * Rename this table
     */
    public SelectbfParams rename(String name) {
        return new SelectbfParams(name, null);
    }
}