/*
 * This file is generated by jOOQ.
*/
package io.github.bfstats.dbstats.jooq.tables.records;


import io.github.bfstats.dbstats.jooq.tables.Round;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


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
public class RoundRecord extends UpdatableRecordImpl<RoundRecord> {

    private static final long serialVersionUID = -212971704;

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
     * Setter for <code>round.game_id</code>.
     */
    public void setGameId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>round.game_id</code>.
     */
    public Integer getGameId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>round.start_time</code>.
     */
    public void setStartTime(Timestamp value) {
        set(2, value);
    }

    /**
     * Getter for <code>round.start_time</code>.
     */
    public Timestamp getStartTime() {
        return (Timestamp) get(2);
    }

    /**
     * Setter for <code>round.start_tickets_team_1</code>.
     */
    public void setStartTicketsTeam_1(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>round.start_tickets_team_1</code>.
     */
    public Integer getStartTicketsTeam_1() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>round.start_tickets_team_2</code>.
     */
    public void setStartTicketsTeam_2(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>round.start_tickets_team_2</code>.
     */
    public Integer getStartTicketsTeam_2() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>round.server_name</code>.
     */
    public void setServerName(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>round.server_name</code>.
     */
    public String getServerName() {
        return (String) get(5);
    }

    /**
     * Setter for <code>round.server_port</code>.
     */
    public void setServerPort(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>round.server_port</code>.
     */
    public Integer getServerPort() {
        return (Integer) get(6);
    }

    /**
     * Setter for <code>round.game_code</code>.
     */
    public void setGameCode(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>round.game_code</code>.
     */
    public String getGameCode() {
        return (String) get(7);
    }

    /**
     * Setter for <code>round.mod_id</code>.
     */
    public void setModId(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>round.mod_id</code>.
     */
    public String getModId() {
        return (String) get(8);
    }

    /**
     * Setter for <code>round.map_code</code>.
     */
    public void setMapCode(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>round.map_code</code>.
     */
    public String getMapCode() {
        return (String) get(9);
    }

    /**
     * Setter for <code>round.game_mode</code>.
     */
    public void setGameMode(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>round.game_mode</code>.
     */
    public String getGameMode() {
        return (String) get(10);
    }

    /**
     * Setter for <code>round.max_game_time</code>.
     */
    public void setMaxGameTime(Integer value) {
        set(11, value);
    }

    /**
     * Getter for <code>round.max_game_time</code>.
     */
    public Integer getMaxGameTime() {
        return (Integer) get(11);
    }

    /**
     * Setter for <code>round.max_players</code>.
     */
    public void setMaxPlayers(Integer value) {
        set(12, value);
    }

    /**
     * Getter for <code>round.max_players</code>.
     */
    public Integer getMaxPlayers() {
        return (Integer) get(12);
    }

    /**
     * Setter for <code>round.score_limit</code>.
     */
    public void setScoreLimit(Integer value) {
        set(13, value);
    }

    /**
     * Getter for <code>round.score_limit</code>.
     */
    public Integer getScoreLimit() {
        return (Integer) get(13);
    }

    /**
     * Setter for <code>round.no_of_rounds</code>.
     */
    public void setNoOfRounds(Integer value) {
        set(14, value);
    }

    /**
     * Getter for <code>round.no_of_rounds</code>.
     */
    public Integer getNoOfRounds() {
        return (Integer) get(14);
    }

    /**
     * Setter for <code>round.spawn_time</code>.
     */
    public void setSpawnTime(Integer value) {
        set(15, value);
    }

    /**
     * Getter for <code>round.spawn_time</code>.
     */
    public Integer getSpawnTime() {
        return (Integer) get(15);
    }

    /**
     * Setter for <code>round.spawn_delay</code>.
     */
    public void setSpawnDelay(Integer value) {
        set(16, value);
    }

    /**
     * Getter for <code>round.spawn_delay</code>.
     */
    public Integer getSpawnDelay() {
        return (Integer) get(16);
    }

    /**
     * Setter for <code>round.game_start_delay</code>.
     */
    public void setGameStartDelay(Integer value) {
        set(17, value);
    }

    /**
     * Getter for <code>round.game_start_delay</code>.
     */
    public Integer getGameStartDelay() {
        return (Integer) get(17);
    }

    /**
     * Setter for <code>round.round_start_delay</code>.
     */
    public void setRoundStartDelay(Integer value) {
        set(18, value);
    }

    /**
     * Getter for <code>round.round_start_delay</code>.
     */
    public Integer getRoundStartDelay() {
        return (Integer) get(18);
    }

    /**
     * Setter for <code>round.soldier_ff</code>.
     */
    public void setSoldierFf(Integer value) {
        set(19, value);
    }

    /**
     * Getter for <code>round.soldier_ff</code>.
     */
    public Integer getSoldierFf() {
        return (Integer) get(19);
    }

    /**
     * Setter for <code>round.soldier_ff_on_splash</code>.
     */
    public void setSoldierFfOnSplash(Integer value) {
        set(20, value);
    }

    /**
     * Getter for <code>round.soldier_ff_on_splash</code>.
     */
    public Integer getSoldierFfOnSplash() {
        return (Integer) get(20);
    }

    /**
     * Setter for <code>round.vehicle_ff</code>.
     */
    public void setVehicleFf(Integer value) {
        set(21, value);
    }

    /**
     * Getter for <code>round.vehicle_ff</code>.
     */
    public Integer getVehicleFf() {
        return (Integer) get(21);
    }

    /**
     * Setter for <code>round.vehicle_ff_on_splash</code>.
     */
    public void setVehicleFfOnSplash(Integer value) {
        set(22, value);
    }

    /**
     * Getter for <code>round.vehicle_ff_on_splash</code>.
     */
    public Integer getVehicleFfOnSplash() {
        return (Integer) get(22);
    }

    /**
     * Setter for <code>round.ff_kickback</code>.
     */
    public void setFfKickback(Integer value) {
        set(23, value);
    }

    /**
     * Getter for <code>round.ff_kickback</code>.
     */
    public Integer getFfKickback() {
        return (Integer) get(23);
    }

    /**
     * Setter for <code>round.ff_kickback_on_splash</code>.
     */
    public void setFfKickbackOnSplash(Integer value) {
        set(24, value);
    }

    /**
     * Getter for <code>round.ff_kickback_on_splash</code>.
     */
    public Integer getFfKickbackOnSplash() {
        return (Integer) get(24);
    }

    /**
     * Setter for <code>round.ticket_ratio</code>.
     */
    public void setTicketRatio(Integer value) {
        set(25, value);
    }

    /**
     * Getter for <code>round.ticket_ratio</code>.
     */
    public Integer getTicketRatio() {
        return (Integer) get(25);
    }

    /**
     * Setter for <code>round.team_kill_punish</code>.
     */
    public void setTeamKillPunish(Integer value) {
        set(26, value);
    }

    /**
     * Getter for <code>round.team_kill_punish</code>.
     */
    public Integer getTeamKillPunish() {
        return (Integer) get(26);
    }

    /**
     * Setter for <code>round.punkbuster_enabled</code>.
     */
    public void setPunkbusterEnabled(Integer value) {
        set(27, value);
    }

    /**
     * Getter for <code>round.punkbuster_enabled</code>.
     */
    public Integer getPunkbusterEnabled() {
        return (Integer) get(27);
    }

    /**
     * Setter for <code>round.auto_balance_enabled</code>.
     */
    public void setAutoBalanceEnabled(Integer value) {
        set(28, value);
    }

    /**
     * Getter for <code>round.auto_balance_enabled</code>.
     */
    public Integer getAutoBalanceEnabled() {
        return (Integer) get(28);
    }

    /**
     * Setter for <code>round.tag_distance</code>.
     */
    public void setTagDistance(Integer value) {
        set(29, value);
    }

    /**
     * Getter for <code>round.tag_distance</code>.
     */
    public Integer getTagDistance() {
        return (Integer) get(29);
    }

    /**
     * Setter for <code>round.tag_distance_scope</code>.
     */
    public void setTagDistanceScope(Integer value) {
        set(30, value);
    }

    /**
     * Getter for <code>round.tag_distance_scope</code>.
     */
    public Integer getTagDistanceScope() {
        return (Integer) get(30);
    }

    /**
     * Setter for <code>round.nose_camera_allowed</code>.
     */
    public void setNoseCameraAllowed(Integer value) {
        set(31, value);
    }

    /**
     * Getter for <code>round.nose_camera_allowed</code>.
     */
    public Integer getNoseCameraAllowed() {
        return (Integer) get(31);
    }

    /**
     * Setter for <code>round.free_camera_allowed</code>.
     */
    public void setFreeCameraAllowed(Integer value) {
        set(32, value);
    }

    /**
     * Getter for <code>round.free_camera_allowed</code>.
     */
    public Integer getFreeCameraAllowed() {
        return (Integer) get(32);
    }

    /**
     * Setter for <code>round.external_views_allowed</code>.
     */
    public void setExternalViewsAllowed(Integer value) {
        set(33, value);
    }

    /**
     * Getter for <code>round.external_views_allowed</code>.
     */
    public Integer getExternalViewsAllowed() {
        return (Integer) get(33);
    }

    /**
     * Setter for <code>round.hit_indication_enabled</code>.
     */
    public void setHitIndicationEnabled(Integer value) {
        set(34, value);
    }

    /**
     * Getter for <code>round.hit_indication_enabled</code>.
     */
    public Integer getHitIndicationEnabled() {
        return (Integer) get(34);
    }

    /**
     * Setter for <code>round.internet</code>.
     */
    public void setInternet(Integer value) {
        set(35, value);
    }

    /**
     * Getter for <code>round.internet</code>.
     */
    public Integer getInternet() {
        return (Integer) get(35);
    }

    /**
     * Setter for <code>round.coop_cpu</code>.
     */
    public void setCoopCpu(Integer value) {
        set(36, value);
    }

    /**
     * Getter for <code>round.coop_cpu</code>.
     */
    public Integer getCoopCpu() {
        return (Integer) get(36);
    }

    /**
     * Setter for <code>round.coop_skill</code>.
     */
    public void setCoopSkill(Integer value) {
        set(37, value);
    }

    /**
     * Getter for <code>round.coop_skill</code>.
     */
    public Integer getCoopSkill() {
        return (Integer) get(37);
    }

    /**
     * Setter for <code>round.allied_player_count_ratio</code>.
     */
    public void setAlliedPlayerCountRatio(Integer value) {
        set(38, value);
    }

    /**
     * Getter for <code>round.allied_player_count_ratio</code>.
     */
    public Integer getAlliedPlayerCountRatio() {
        return (Integer) get(38);
    }

    /**
     * Setter for <code>round.axis_player_count_ratio</code>.
     */
    public void setAxisPlayerCountRatio(Integer value) {
        set(39, value);
    }

    /**
     * Getter for <code>round.axis_player_count_ratio</code>.
     */
    public Integer getAxisPlayerCountRatio() {
        return (Integer) get(39);
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
    public RoundRecord(Integer id, Integer gameId, Timestamp startTime, Integer startTicketsTeam_1, Integer startTicketsTeam_2, String serverName, Integer serverPort, String gameCode, String modId, String mapCode, String gameMode, Integer maxGameTime, Integer maxPlayers, Integer scoreLimit, Integer noOfRounds, Integer spawnTime, Integer spawnDelay, Integer gameStartDelay, Integer roundStartDelay, Integer soldierFf, Integer soldierFfOnSplash, Integer vehicleFf, Integer vehicleFfOnSplash, Integer ffKickback, Integer ffKickbackOnSplash, Integer ticketRatio, Integer teamKillPunish, Integer punkbusterEnabled, Integer autoBalanceEnabled, Integer tagDistance, Integer tagDistanceScope, Integer noseCameraAllowed, Integer freeCameraAllowed, Integer externalViewsAllowed, Integer hitIndicationEnabled, Integer internet, Integer coopCpu, Integer coopSkill, Integer alliedPlayerCountRatio, Integer axisPlayerCountRatio) {
        super(Round.ROUND);

        set(0, id);
        set(1, gameId);
        set(2, startTime);
        set(3, startTicketsTeam_1);
        set(4, startTicketsTeam_2);
        set(5, serverName);
        set(6, serverPort);
        set(7, gameCode);
        set(8, modId);
        set(9, mapCode);
        set(10, gameMode);
        set(11, maxGameTime);
        set(12, maxPlayers);
        set(13, scoreLimit);
        set(14, noOfRounds);
        set(15, spawnTime);
        set(16, spawnDelay);
        set(17, gameStartDelay);
        set(18, roundStartDelay);
        set(19, soldierFf);
        set(20, soldierFfOnSplash);
        set(21, vehicleFf);
        set(22, vehicleFfOnSplash);
        set(23, ffKickback);
        set(24, ffKickbackOnSplash);
        set(25, ticketRatio);
        set(26, teamKillPunish);
        set(27, punkbusterEnabled);
        set(28, autoBalanceEnabled);
        set(29, tagDistance);
        set(30, tagDistanceScope);
        set(31, noseCameraAllowed);
        set(32, freeCameraAllowed);
        set(33, externalViewsAllowed);
        set(34, hitIndicationEnabled);
        set(35, internet);
        set(36, coopCpu);
        set(37, coopSkill);
        set(38, alliedPlayerCountRatio);
        set(39, axisPlayerCountRatio);
    }
}
