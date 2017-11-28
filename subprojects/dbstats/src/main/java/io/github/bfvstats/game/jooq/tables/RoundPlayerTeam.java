/*
 * This file is generated by jOOQ.
*/
package io.github.bfvstats.game.jooq.tables;


import io.github.bfvstats.game.jooq.DefaultSchema;
import io.github.bfvstats.game.jooq.Indexes;
import io.github.bfvstats.game.jooq.Keys;
import io.github.bfvstats.game.jooq.tables.records.RoundPlayerTeamRecord;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
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
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RoundPlayerTeam extends TableImpl<RoundPlayerTeamRecord> {

    private static final long serialVersionUID = 639500104;

    /**
     * The reference instance of <code>round_player_team</code>
     */
    public static final RoundPlayerTeam ROUND_PLAYER_TEAM = new RoundPlayerTeam();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RoundPlayerTeamRecord> getRecordType() {
        return RoundPlayerTeamRecord.class;
    }

    /**
     * The column <code>round_player_team.id</code>.
     */
    public final TableField<RoundPlayerTeamRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>round_player_team.round_id</code>.
     */
    public final TableField<RoundPlayerTeamRecord, Integer> ROUND_ID = createField("round_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round_player_team.player_id</code>.
     */
    public final TableField<RoundPlayerTeamRecord, Integer> PLAYER_ID = createField("player_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round_player_team.team</code>.
     */
    public final TableField<RoundPlayerTeamRecord, Integer> TEAM = createField("team", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>round_player_team.start_time</code>.
     */
    public final TableField<RoundPlayerTeamRecord, Timestamp> START_TIME = createField("start_time", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * The column <code>round_player_team.end_time</code>.
     */
    public final TableField<RoundPlayerTeamRecord, Timestamp> END_TIME = createField("end_time", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * Create a <code>round_player_team</code> table reference
     */
    public RoundPlayerTeam() {
        this(DSL.name("round_player_team"), null);
    }

    /**
     * Create an aliased <code>round_player_team</code> table reference
     */
    public RoundPlayerTeam(String alias) {
        this(DSL.name(alias), ROUND_PLAYER_TEAM);
    }

    /**
     * Create an aliased <code>round_player_team</code> table reference
     */
    public RoundPlayerTeam(Name alias) {
        this(alias, ROUND_PLAYER_TEAM);
    }

    private RoundPlayerTeam(Name alias, Table<RoundPlayerTeamRecord> aliased) {
        this(alias, aliased, null);
    }

    private RoundPlayerTeam(Name alias, Table<RoundPlayerTeamRecord> aliased, Field<?>[] parameters) {
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
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.ROUND_PLAYER_START_END_TIME_IDX, Indexes.ROUND_PLAYER_TEAM_ID_IDX);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<RoundPlayerTeamRecord, Integer> getIdentity() {
        return Keys.IDENTITY_ROUND_PLAYER_TEAM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<RoundPlayerTeamRecord> getPrimaryKey() {
        return Keys.PK_ROUND_PLAYER_TEAM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<RoundPlayerTeamRecord>> getKeys() {
        return Arrays.<UniqueKey<RoundPlayerTeamRecord>>asList(Keys.PK_ROUND_PLAYER_TEAM);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<RoundPlayerTeamRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<RoundPlayerTeamRecord, ?>>asList(Keys.FK_ROUND_PLAYER_TEAM_ROUND_1, Keys.FK_ROUND_PLAYER_TEAM_PLAYER_1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerTeam as(String alias) {
        return new RoundPlayerTeam(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundPlayerTeam as(Name alias) {
        return new RoundPlayerTeam(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public RoundPlayerTeam rename(String name) {
        return new RoundPlayerTeam(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public RoundPlayerTeam rename(Name name) {
        return new RoundPlayerTeam(name, null);
    }
}
