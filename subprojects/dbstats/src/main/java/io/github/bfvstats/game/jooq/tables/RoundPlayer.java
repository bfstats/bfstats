/**
 * This class is generated by jOOQ
 */
package io.github.bfvstats.game.jooq.tables;


import io.github.bfvstats.game.jooq.DefaultSchema;
import io.github.bfvstats.game.jooq.Keys;
import io.github.bfvstats.game.jooq.tables.records.RoundPlayerRecord;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
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
public class RoundPlayer extends TableImpl<RoundPlayerRecord> {

    private static final long serialVersionUID = 913539197;

    /**
     * The reference instance of <code>round_player</code>
     */
    public static final RoundPlayer ROUND_PLAYER = new RoundPlayer();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RoundPlayerRecord> getRecordType() {
        return RoundPlayerRecord.class;
    }

    /**
     * The column <code>round_player.id</code>.
     */
    public final TableField<RoundPlayerRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>round_player.joined_round_id</code>.
     */
    public final TableField<RoundPlayerRecord, Integer> JOINED_ROUND_ID = createField("joined_round_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round_player.end_round_id</code>.
     */
    public final TableField<RoundPlayerRecord, Integer> END_ROUND_ID = createField("end_round_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round_player.player_id</code>.
     */
    public final TableField<RoundPlayerRecord, Integer> PLAYER_ID = createField("player_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round_player.start_time</code>.
     */
    public final TableField<RoundPlayerRecord, Timestamp> START_TIME = createField("start_time", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * The column <code>round_player.end_time</code>.
     */
    public final TableField<RoundPlayerRecord, Timestamp> END_TIME = createField("end_time", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * Create a <code>round_player</code> table reference
     */
    public RoundPlayer() {
        this("round_player", null);
    }

    /**
     * Create an aliased <code>round_player</code> table reference
     */
    public RoundPlayer(String alias) {
        this(alias, ROUND_PLAYER);
    }

    private RoundPlayer(String alias, Table<RoundPlayerRecord> aliased) {
        this(alias, aliased, null);
    }

    private RoundPlayer(String alias, Table<RoundPlayerRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<RoundPlayerRecord> getPrimaryKey() {
        return Keys.PK_ROUND_PLAYER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<RoundPlayerRecord>> getKeys() {
        return Arrays.<UniqueKey<RoundPlayerRecord>>asList(Keys.PK_ROUND_PLAYER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<RoundPlayerRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<RoundPlayerRecord, ?>>asList(Keys.FK_ROUND_PLAYER_ROUND_2, Keys.FK_ROUND_PLAYER_ROUND_1, Keys.FK_ROUND_PLAYER_PLAYER_1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayer as(String alias) {
        return new RoundPlayer(alias, this);
    }

    /**
     * Rename this table
     */
    public RoundPlayer rename(String name) {
        return new RoundPlayer(name, null);
    }
}
