/**
 * This class is generated by jOOQ
 */
package io.github.bfvstats.jpa.tables;


import io.github.bfvstats.jpa.DefaultSchema;
import io.github.bfvstats.jpa.Keys;
import io.github.bfvstats.jpa.tables.records.SelectbfGamesRecord;

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
public class SelectbfGames extends TableImpl<SelectbfGamesRecord> {

    private static final long serialVersionUID = 796437802;

    /**
     * The reference instance of <code>selectbf_games</code>
     */
    public static final SelectbfGames SELECTBF_GAMES = new SelectbfGames();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SelectbfGamesRecord> getRecordType() {
        return SelectbfGamesRecord.class;
    }

    /**
     * The column <code>selectbf_games.id</code>.
     */
    public final TableField<SelectbfGamesRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>selectbf_games.servername</code>.
     */
    public final TableField<SelectbfGamesRecord, Object> SERVERNAME = createField("servername", org.jooq.impl.DefaultDataType.getDefaultDataType("tinytext"), this, "");

    /**
     * The column <code>selectbf_games.modid</code>.
     */
    public final TableField<SelectbfGamesRecord, Object> MODID = createField("modid", org.jooq.impl.DefaultDataType.getDefaultDataType("tinytext"), this, "");

    /**
     * The column <code>selectbf_games.mapid</code>.
     */
    public final TableField<SelectbfGamesRecord, Object> MAPID = createField("mapid", org.jooq.impl.DefaultDataType.getDefaultDataType("tinytext"), this, "");

    /**
     * The column <code>selectbf_games.map</code>.
     */
    public final TableField<SelectbfGamesRecord, Object> MAP = createField("map", org.jooq.impl.DefaultDataType.getDefaultDataType("tinytext"), this, "");

    /**
     * The column <code>selectbf_games.game_mode</code>.
     */
    public final TableField<SelectbfGamesRecord, Object> GAME_MODE = createField("game_mode", org.jooq.impl.DefaultDataType.getDefaultDataType("tinytext"), this, "");

    /**
     * The column <code>selectbf_games.gametime</code>.
     */
    public final TableField<SelectbfGamesRecord, Integer> GAMETIME = createField("gametime", org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>selectbf_games.maxplayers</code>.
     */
    public final TableField<SelectbfGamesRecord, Integer> MAXPLAYERS = createField("maxplayers", org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>selectbf_games.scorelimit</code>.
     */
    public final TableField<SelectbfGamesRecord, Integer> SCORELIMIT = createField("scorelimit", org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>selectbf_games.spawntime</code>.
     */
    public final TableField<SelectbfGamesRecord, Integer> SPAWNTIME = createField("spawntime", org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>selectbf_games.soldierff</code>.
     */
    public final TableField<SelectbfGamesRecord, Integer> SOLDIERFF = createField("soldierff", org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>selectbf_games.vehicleff</code>.
     */
    public final TableField<SelectbfGamesRecord, Integer> VEHICLEFF = createField("vehicleff", org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>selectbf_games.tkpunish</code>.
     */
    public final TableField<SelectbfGamesRecord, Byte> TKPUNISH = createField("tkpunish", org.jooq.impl.SQLDataType.TINYINT.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.TINYINT)), this, "");

    /**
     * The column <code>selectbf_games.deathcamtype</code>.
     */
    public final TableField<SelectbfGamesRecord, Byte> DEATHCAMTYPE = createField("deathcamtype", org.jooq.impl.SQLDataType.TINYINT.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.TINYINT)), this, "");

    /**
     * The column <code>selectbf_games.starttime</code>.
     */
    public final TableField<SelectbfGamesRecord, Timestamp> STARTTIME = createField("starttime", org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * Create a <code>selectbf_games</code> table reference
     */
    public SelectbfGames() {
        this("selectbf_games", null);
    }

    /**
     * Create an aliased <code>selectbf_games</code> table reference
     */
    public SelectbfGames(String alias) {
        this(alias, SELECTBF_GAMES);
    }

    private SelectbfGames(String alias, Table<SelectbfGamesRecord> aliased) {
        this(alias, aliased, null);
    }

    private SelectbfGames(String alias, Table<SelectbfGamesRecord> aliased, Field<?>[] parameters) {
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
    public Identity<SelectbfGamesRecord, Integer> getIdentity() {
        return Keys.IDENTITY_SELECTBF_GAMES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<SelectbfGamesRecord> getPrimaryKey() {
        return Keys.PK_SELECTBF_GAMES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<SelectbfGamesRecord>> getKeys() {
        return Arrays.<UniqueKey<SelectbfGamesRecord>>asList(Keys.PK_SELECTBF_GAMES);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectbfGames as(String alias) {
        return new SelectbfGames(alias, this);
    }

    /**
     * Rename this table
     */
    public SelectbfGames rename(String name) {
        return new SelectbfGames(name, null);
    }
}
