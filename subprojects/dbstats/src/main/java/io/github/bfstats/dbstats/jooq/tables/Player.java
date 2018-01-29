/*
 * This file is generated by jOOQ.
*/
package io.github.bfstats.dbstats.jooq.tables;


import io.github.bfstats.dbstats.jooq.DefaultSchema;
import io.github.bfstats.dbstats.jooq.Keys;
import io.github.bfstats.dbstats.jooq.tables.records.PlayerRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
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
public class Player extends TableImpl<PlayerRecord> {

    private static final long serialVersionUID = 298635990;

    /**
     * The reference instance of <code>player</code>
     */
    public static final Player PLAYER = new Player();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PlayerRecord> getRecordType() {
        return PlayerRecord.class;
    }

    /**
     * The column <code>player.id</code>.
     */
    public final TableField<PlayerRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>player.name</code>.
     */
    public final TableField<PlayerRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR(150).nullable(false), this, "");

    /**
     * The column <code>player.keyhash</code>.
     */
    public final TableField<PlayerRecord, String> KEYHASH = createField("keyhash", org.jooq.impl.SQLDataType.VARCHAR(32), this, "");

    /**
     * Create a <code>player</code> table reference
     */
    public Player() {
        this(DSL.name("player"), null);
    }

    /**
     * Create an aliased <code>player</code> table reference
     */
    public Player(String alias) {
        this(DSL.name(alias), PLAYER);
    }

    /**
     * Create an aliased <code>player</code> table reference
     */
    public Player(Name alias) {
        this(alias, PLAYER);
    }

    private Player(Name alias, Table<PlayerRecord> aliased) {
        this(alias, aliased, null);
    }

    private Player(Name alias, Table<PlayerRecord> aliased, Field<?>[] parameters) {
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
    public Identity<PlayerRecord, Integer> getIdentity() {
        return Keys.IDENTITY_PLAYER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<PlayerRecord> getPrimaryKey() {
        return Keys.PK_PLAYER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<PlayerRecord>> getKeys() {
        return Arrays.<UniqueKey<PlayerRecord>>asList(Keys.PK_PLAYER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player as(String alias) {
        return new Player(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player as(Name alias) {
        return new Player(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Player rename(String name) {
        return new Player(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Player rename(Name name) {
        return new Player(name, null);
    }
}
