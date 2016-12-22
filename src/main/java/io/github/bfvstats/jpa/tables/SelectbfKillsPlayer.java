/**
 * This class is generated by jOOQ
 */
package io.github.bfvstats.jpa.tables;


import io.github.bfvstats.jpa.DefaultSchema;
import io.github.bfvstats.jpa.Keys;
import io.github.bfvstats.jpa.tables.records.SelectbfKillsPlayerRecord;

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
public class SelectbfKillsPlayer extends TableImpl<SelectbfKillsPlayerRecord> {

    private static final long serialVersionUID = 989432601;

    /**
     * The reference instance of <code>selectbf_kills_player</code>
     */
    public static final SelectbfKillsPlayer SELECTBF_KILLS_PLAYER = new SelectbfKillsPlayer();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SelectbfKillsPlayerRecord> getRecordType() {
        return SelectbfKillsPlayerRecord.class;
    }

    /**
     * The column <code>selectbf_kills_player.id</code>.
     */
    public final TableField<SelectbfKillsPlayerRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>selectbf_kills_player.player_id</code>.
     */
    public final TableField<SelectbfKillsPlayerRecord, Integer> PLAYER_ID = createField("player_id", org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>selectbf_kills_player.victim_id</code>.
     */
    public final TableField<SelectbfKillsPlayerRecord, Integer> VICTIM_ID = createField("victim_id", org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>selectbf_kills_player.times_killed</code>.
     */
    public final TableField<SelectbfKillsPlayerRecord, Integer> TIMES_KILLED = createField("times_killed", org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("'0'", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * Create a <code>selectbf_kills_player</code> table reference
     */
    public SelectbfKillsPlayer() {
        this("selectbf_kills_player", null);
    }

    /**
     * Create an aliased <code>selectbf_kills_player</code> table reference
     */
    public SelectbfKillsPlayer(String alias) {
        this(alias, SELECTBF_KILLS_PLAYER);
    }

    private SelectbfKillsPlayer(String alias, Table<SelectbfKillsPlayerRecord> aliased) {
        this(alias, aliased, null);
    }

    private SelectbfKillsPlayer(String alias, Table<SelectbfKillsPlayerRecord> aliased, Field<?>[] parameters) {
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
    public Identity<SelectbfKillsPlayerRecord, Integer> getIdentity() {
        return Keys.IDENTITY_SELECTBF_KILLS_PLAYER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<SelectbfKillsPlayerRecord> getPrimaryKey() {
        return Keys.PK_SELECTBF_KILLS_PLAYER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<SelectbfKillsPlayerRecord>> getKeys() {
        return Arrays.<UniqueKey<SelectbfKillsPlayerRecord>>asList(Keys.PK_SELECTBF_KILLS_PLAYER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectbfKillsPlayer as(String alias) {
        return new SelectbfKillsPlayer(alias, this);
    }

    /**
     * Rename this table
     */
    public SelectbfKillsPlayer rename(String name) {
        return new SelectbfKillsPlayer(name, null);
    }
}