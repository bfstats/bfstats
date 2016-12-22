/**
 * This class is generated by jOOQ
 */
package io.github.bfvstats.jpa.tables;


import io.github.bfvstats.jpa.DefaultSchema;
import io.github.bfvstats.jpa.Keys;
import io.github.bfvstats.jpa.tables.records.SelectbfCategoryRecord;

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
public class SelectbfCategory extends TableImpl<SelectbfCategoryRecord> {

    private static final long serialVersionUID = 1647423630;

    /**
     * The reference instance of <code>selectbf_category</code>
     */
    public static final SelectbfCategory SELECTBF_CATEGORY = new SelectbfCategory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SelectbfCategoryRecord> getRecordType() {
        return SelectbfCategoryRecord.class;
    }

    /**
     * The column <code>selectbf_category.id</code>.
     */
    public final TableField<SelectbfCategoryRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>selectbf_category.name</code>.
     */
    public final TableField<SelectbfCategoryRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR.length(50).defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>selectbf_category.collect_data</code>.
     */
    public final TableField<SelectbfCategoryRecord, Integer> COLLECT_DATA = createField("collect_data", org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>selectbf_category.datasource_name</code>.
     */
    public final TableField<SelectbfCategoryRecord, String> DATASOURCE_NAME = createField("datasource_name", org.jooq.impl.SQLDataType.VARCHAR.length(50).defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>selectbf_category.type</code>.
     */
    public final TableField<SelectbfCategoryRecord, String> TYPE = createField("type", org.jooq.impl.SQLDataType.VARCHAR.length(50).defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>selectbf_category.inserttime</code>.
     */
    public final TableField<SelectbfCategoryRecord, Timestamp> INSERTTIME = createField("inserttime", org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * Create a <code>selectbf_category</code> table reference
     */
    public SelectbfCategory() {
        this("selectbf_category", null);
    }

    /**
     * Create an aliased <code>selectbf_category</code> table reference
     */
    public SelectbfCategory(String alias) {
        this(alias, SELECTBF_CATEGORY);
    }

    private SelectbfCategory(String alias, Table<SelectbfCategoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private SelectbfCategory(String alias, Table<SelectbfCategoryRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<SelectbfCategoryRecord> getPrimaryKey() {
        return Keys.PK_SELECTBF_CATEGORY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<SelectbfCategoryRecord>> getKeys() {
        return Arrays.<UniqueKey<SelectbfCategoryRecord>>asList(Keys.PK_SELECTBF_CATEGORY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectbfCategory as(String alias) {
        return new SelectbfCategory(alias, this);
    }

    /**
     * Rename this table
     */
    public SelectbfCategory rename(String name) {
        return new SelectbfCategory(name, null);
    }
}