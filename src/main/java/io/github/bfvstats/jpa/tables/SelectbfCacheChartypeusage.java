/**
 * This class is generated by jOOQ
 */
package io.github.bfvstats.jpa.tables;


import io.github.bfvstats.jpa.DefaultSchema;
import io.github.bfvstats.jpa.Keys;
import io.github.bfvstats.jpa.tables.records.SelectbfCacheChartypeusageRecord;

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
public class SelectbfCacheChartypeusage extends TableImpl<SelectbfCacheChartypeusageRecord> {

    private static final long serialVersionUID = 128857851;

    /**
     * The reference instance of <code>selectbf_cache_chartypeusage</code>
     */
    public static final SelectbfCacheChartypeusage SELECTBF_CACHE_CHARTYPEUSAGE = new SelectbfCacheChartypeusage();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SelectbfCacheChartypeusageRecord> getRecordType() {
        return SelectbfCacheChartypeusageRecord.class;
    }

    /**
     * The column <code>selectbf_cache_chartypeusage.kit</code>.
     */
    public final TableField<SelectbfCacheChartypeusageRecord, String> KIT = createField("kit", org.jooq.impl.SQLDataType.VARCHAR.length(35).nullable(false).defaultValue(org.jooq.impl.DSL.field("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>selectbf_cache_chartypeusage.percentage</code>.
     */
    public final TableField<SelectbfCacheChartypeusageRecord, Float> PERCENTAGE = createField("percentage", org.jooq.impl.SQLDataType.REAL.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.REAL)), this, "");

    /**
     * The column <code>selectbf_cache_chartypeusage.times_used</code>.
     */
    public final TableField<SelectbfCacheChartypeusageRecord, Integer> TIMES_USED = createField("times_used", org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * Create a <code>selectbf_cache_chartypeusage</code> table reference
     */
    public SelectbfCacheChartypeusage() {
        this("selectbf_cache_chartypeusage", null);
    }

    /**
     * Create an aliased <code>selectbf_cache_chartypeusage</code> table reference
     */
    public SelectbfCacheChartypeusage(String alias) {
        this(alias, SELECTBF_CACHE_CHARTYPEUSAGE);
    }

    private SelectbfCacheChartypeusage(String alias, Table<SelectbfCacheChartypeusageRecord> aliased) {
        this(alias, aliased, null);
    }

    private SelectbfCacheChartypeusage(String alias, Table<SelectbfCacheChartypeusageRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<SelectbfCacheChartypeusageRecord> getPrimaryKey() {
        return Keys.PK_SELECTBF_CACHE_CHARTYPEUSAGE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<SelectbfCacheChartypeusageRecord>> getKeys() {
        return Arrays.<UniqueKey<SelectbfCacheChartypeusageRecord>>asList(Keys.PK_SELECTBF_CACHE_CHARTYPEUSAGE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectbfCacheChartypeusage as(String alias) {
        return new SelectbfCacheChartypeusage(alias, this);
    }

    /**
     * Rename this table
     */
    public SelectbfCacheChartypeusage rename(String name) {
        return new SelectbfCacheChartypeusage(name, null);
    }
}
