/*
 * This file is generated by jOOQ.
*/
package io.github.bfvstats.game.jooq.tables.records;


import io.github.bfvstats.game.jooq.tables.Round;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record22;
import org.jooq.Row22;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RoundRecord extends UpdatableRecordImpl<RoundRecord> implements Record22<Integer, Timestamp, Integer, Integer, String, Integer, String, String, String, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> {

    private static final long serialVersionUID = -1903963968;

    /**
     * Setter for <code>round.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>round.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>round.start_time</code>.
     */
    public void setStartTime(Timestamp value) {
        set(1, value);
    }

    /**
     * Getter for <code>round.start_time</code>.
     */
    public Timestamp getStartTime() {
        return (Timestamp) get(1);
    }

    /**
     * Setter for <code>round.start_tickets_team_1</code>.
     */
    public void setStartTicketsTeam_1(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>round.start_tickets_team_1</code>.
     */
    public Integer getStartTicketsTeam_1() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>round.start_tickets_team_2</code>.
     */
    public void setStartTicketsTeam_2(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>round.start_tickets_team_2</code>.
     */
    public Integer getStartTicketsTeam_2() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>round.server_name</code>.
     */
    public void setServerName(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>round.server_name</code>.
     */
    public String getServerName() {
        return (String) get(4);
    }

    /**
     * Setter for <code>round.server_port</code>.
     */
    public void setServerPort(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>round.server_port</code>.
     */
    public Integer getServerPort() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>round.mod_id</code>.
     */
    public void setModId(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>round.mod_id</code>.
     */
    public String getModId() {
        return (String) get(6);
    }

    /**
     * Setter for <code>round.map_code</code>.
     */
    public void setMapCode(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>round.map_code</code>.
     */
    public String getMapCode() {
        return (String) get(7);
    }

    /**
     * Setter for <code>round.game_mode</code>.
     */
    public void setGameMode(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>round.game_mode</code>.
     */
    public String getGameMode() {
        return (String) get(8);
    }

    /**
     * Setter for <code>round.max_game_time</code>.
     */
    public void setMaxGameTime(Integer value) {
        set(9, value);
    }

    /**
     * Getter for <code>round.max_game_time</code>.
     */
    public Integer getMaxGameTime() {
        return (Integer) get(9);
    }

    /**
     * Setter for <code>round.max_players</code>.
     */
    public void setMaxPlayers(Integer value) {
        set(10, value);
    }

    /**
     * Getter for <code>round.max_players</code>.
     */
    public Integer getMaxPlayers() {
        return (Integer) get(10);
    }

    /**
     * Setter for <code>round.score_limit</code>.
     */
    public void setScoreLimit(Integer value) {
        set(11, value);
    }

    /**
     * Getter for <code>round.score_limit</code>.
     */
    public Integer getScoreLimit() {
        return (Integer) get(11);
    }

    /**
     * Setter for <code>round.no_of_rounds</code>.
     */
    public void setNoOfRounds(Integer value) {
        set(12, value);
    }

    /**
     * Getter for <code>round.no_of_rounds</code>.
     */
    public Integer getNoOfRounds() {
        return (Integer) get(12);
    }

    /**
     * Setter for <code>round.spawn_time</code>.
     */
    public void setSpawnTime(Integer value) {
        set(13, value);
    }

    /**
     * Getter for <code>round.spawn_time</code>.
     */
    public Integer getSpawnTime() {
        return (Integer) get(13);
    }

    /**
     * Setter for <code>round.spawn_delay</code>.
     */
    public void setSpawnDelay(Integer value) {
        set(14, value);
    }

    /**
     * Getter for <code>round.spawn_delay</code>.
     */
    public Integer getSpawnDelay() {
        return (Integer) get(14);
    }

    /**
     * Setter for <code>round.game_start_delay</code>.
     */
    public void setGameStartDelay(Integer value) {
        set(15, value);
    }

    /**
     * Getter for <code>round.game_start_delay</code>.
     */
    public Integer getGameStartDelay() {
        return (Integer) get(15);
    }

    /**
     * Setter for <code>round.round_start_delay</code>.
     */
    public void setRoundStartDelay(Integer value) {
        set(16, value);
    }

    /**
     * Getter for <code>round.round_start_delay</code>.
     */
    public Integer getRoundStartDelay() {
        return (Integer) get(16);
    }

    /**
     * Setter for <code>round.soldier_ff</code>.
     */
    public void setSoldierFf(Integer value) {
        set(17, value);
    }

    /**
     * Getter for <code>round.soldier_ff</code>.
     */
    public Integer getSoldierFf() {
        return (Integer) get(17);
    }

    /**
     * Setter for <code>round.vehicle_ff</code>.
     */
    public void setVehicleFf(Integer value) {
        set(18, value);
    }

    /**
     * Getter for <code>round.vehicle_ff</code>.
     */
    public Integer getVehicleFf() {
        return (Integer) get(18);
    }

    /**
     * Setter for <code>round.ticket_ratio</code>.
     */
    public void setTicketRatio(Integer value) {
        set(19, value);
    }

    /**
     * Getter for <code>round.ticket_ratio</code>.
     */
    public Integer getTicketRatio() {
        return (Integer) get(19);
    }

    /**
     * Setter for <code>round.team_kill_punish</code>.
     */
    public void setTeamKillPunish(Integer value) {
        set(20, value);
    }

    /**
     * Getter for <code>round.team_kill_punish</code>.
     */
    public Integer getTeamKillPunish() {
        return (Integer) get(20);
    }

    /**
     * Setter for <code>round.punkbuster_enabled</code>.
     */
    public void setPunkbusterEnabled(Integer value) {
        set(21, value);
    }

    /**
     * Getter for <code>round.punkbuster_enabled</code>.
     */
    public Integer getPunkbusterEnabled() {
        return (Integer) get(21);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record22 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row22<Integer, Timestamp, Integer, Integer, String, Integer, String, String, String, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> fieldsRow() {
        return (Row22) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row22<Integer, Timestamp, Integer, Integer, String, Integer, String, String, String, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> valuesRow() {
        return (Row22) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return Round.ROUND.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field2() {
        return Round.ROUND.START_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field3() {
        return Round.ROUND.START_TICKETS_TEAM_1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field4() {
        return Round.ROUND.START_TICKETS_TEAM_2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return Round.ROUND.SERVER_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field6() {
        return Round.ROUND.SERVER_PORT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return Round.ROUND.MOD_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return Round.ROUND.MAP_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field9() {
        return Round.ROUND.GAME_MODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field10() {
        return Round.ROUND.MAX_GAME_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field11() {
        return Round.ROUND.MAX_PLAYERS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field12() {
        return Round.ROUND.SCORE_LIMIT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field13() {
        return Round.ROUND.NO_OF_ROUNDS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field14() {
        return Round.ROUND.SPAWN_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field15() {
        return Round.ROUND.SPAWN_DELAY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field16() {
        return Round.ROUND.GAME_START_DELAY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field17() {
        return Round.ROUND.ROUND_START_DELAY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field18() {
        return Round.ROUND.SOLDIER_FF;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field19() {
        return Round.ROUND.VEHICLE_FF;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field20() {
        return Round.ROUND.TICKET_RATIO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field21() {
        return Round.ROUND.TEAM_KILL_PUNISH;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field22() {
        return Round.ROUND.PUNKBUSTER_ENABLED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value2() {
        return getStartTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value3() {
        return getStartTicketsTeam_1();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value4() {
        return getStartTicketsTeam_2();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getServerName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value6() {
        return getServerPort();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getModId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getMapCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value9() {
        return getGameMode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value10() {
        return getMaxGameTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value11() {
        return getMaxPlayers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value12() {
        return getScoreLimit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value13() {
        return getNoOfRounds();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value14() {
        return getSpawnTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value15() {
        return getSpawnDelay();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value16() {
        return getGameStartDelay();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value17() {
        return getRoundStartDelay();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value18() {
        return getSoldierFf();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value19() {
        return getVehicleFf();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value20() {
        return getTicketRatio();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value21() {
        return getTeamKillPunish();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value22() {
        return getPunkbusterEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value2(Timestamp value) {
        setStartTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value3(Integer value) {
        setStartTicketsTeam_1(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value4(Integer value) {
        setStartTicketsTeam_2(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value5(String value) {
        setServerName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value6(Integer value) {
        setServerPort(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value7(String value) {
        setModId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value8(String value) {
        setMapCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value9(String value) {
        setGameMode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value10(Integer value) {
        setMaxGameTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value11(Integer value) {
        setMaxPlayers(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value12(Integer value) {
        setScoreLimit(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value13(Integer value) {
        setNoOfRounds(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value14(Integer value) {
        setSpawnTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value15(Integer value) {
        setSpawnDelay(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value16(Integer value) {
        setGameStartDelay(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value17(Integer value) {
        setRoundStartDelay(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value18(Integer value) {
        setSoldierFf(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value19(Integer value) {
        setVehicleFf(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value20(Integer value) {
        setTicketRatio(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value21(Integer value) {
        setTeamKillPunish(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord value22(Integer value) {
        setPunkbusterEnabled(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundRecord values(Integer value1, Timestamp value2, Integer value3, Integer value4, String value5, Integer value6, String value7, String value8, String value9, Integer value10, Integer value11, Integer value12, Integer value13, Integer value14, Integer value15, Integer value16, Integer value17, Integer value18, Integer value19, Integer value20, Integer value21, Integer value22) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        value14(value14);
        value15(value15);
        value16(value16);
        value17(value17);
        value18(value18);
        value19(value19);
        value20(value20);
        value21(value21);
        value22(value22);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RoundRecord
     */
    public RoundRecord() {
        super(Round.ROUND);
    }

    /**
     * Create a detached, initialised RoundRecord
     */
    public RoundRecord(Integer id, Timestamp startTime, Integer startTicketsTeam_1, Integer startTicketsTeam_2, String serverName, Integer serverPort, String modId, String mapCode, String gameMode, Integer maxGameTime, Integer maxPlayers, Integer scoreLimit, Integer noOfRounds, Integer spawnTime, Integer spawnDelay, Integer gameStartDelay, Integer roundStartDelay, Integer soldierFf, Integer vehicleFf, Integer ticketRatio, Integer teamKillPunish, Integer punkbusterEnabled) {
        super(Round.ROUND);

        set(0, id);
        set(1, startTime);
        set(2, startTicketsTeam_1);
        set(3, startTicketsTeam_2);
        set(4, serverName);
        set(5, serverPort);
        set(6, modId);
        set(7, mapCode);
        set(8, gameMode);
        set(9, maxGameTime);
        set(10, maxPlayers);
        set(11, scoreLimit);
        set(12, noOfRounds);
        set(13, spawnTime);
        set(14, spawnDelay);
        set(15, gameStartDelay);
        set(16, roundStartDelay);
        set(17, soldierFf);
        set(18, vehicleFf);
        set(19, ticketRatio);
        set(20, teamKillPunish);
        set(21, punkbusterEnabled);
    }
}
