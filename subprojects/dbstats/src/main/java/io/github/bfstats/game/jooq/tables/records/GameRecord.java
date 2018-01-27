/*
 * This file is generated by jOOQ.
*/
package io.github.bfstats.game.jooq.tables.records;


import io.github.bfstats.game.jooq.tables.Game;

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
public class GameRecord extends UpdatableRecordImpl<GameRecord> {

    private static final long serialVersionUID = 1890617606;

    /**
     * Setter for <code>game.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>game.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>game.server_id</code>.
     */
    public void setServerId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>game.server_id</code>.
     */
    public Integer getServerId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>game.start_time</code>.
     */
    public void setStartTime(Timestamp value) {
        set(2, value);
    }

    /**
     * Getter for <code>game.start_time</code>.
     */
    public Timestamp getStartTime() {
        return (Timestamp) get(2);
    }

    /**
     * Setter for <code>game.server_name</code>.
     */
    public void setServerName(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>game.server_name</code>.
     */
    public String getServerName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>game.server_port</code>.
     */
    public void setServerPort(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>game.server_port</code>.
     */
    public Integer getServerPort() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>game.mod_id</code>.
     */
    public void setModId(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>game.mod_id</code>.
     */
    public String getModId() {
        return (String) get(5);
    }

    /**
     * Setter for <code>game.map_code</code>.
     */
    public void setMapCode(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>game.map_code</code>.
     */
    public String getMapCode() {
        return (String) get(6);
    }

    /**
     * Setter for <code>game.game_mode</code>.
     */
    public void setGameMode(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>game.game_mode</code>.
     */
    public String getGameMode() {
        return (String) get(7);
    }

    /**
     * Setter for <code>game.max_game_time</code>.
     */
    public void setMaxGameTime(Integer value) {
        set(8, value);
    }

    /**
     * Getter for <code>game.max_game_time</code>.
     */
    public Integer getMaxGameTime() {
        return (Integer) get(8);
    }

    /**
     * Setter for <code>game.max_players</code>.
     */
    public void setMaxPlayers(Integer value) {
        set(9, value);
    }

    /**
     * Getter for <code>game.max_players</code>.
     */
    public Integer getMaxPlayers() {
        return (Integer) get(9);
    }

    /**
     * Setter for <code>game.score_limit</code>.
     */
    public void setScoreLimit(Integer value) {
        set(10, value);
    }

    /**
     * Getter for <code>game.score_limit</code>.
     */
    public Integer getScoreLimit() {
        return (Integer) get(10);
    }

    /**
     * Setter for <code>game.no_of_rounds</code>.
     */
    public void setNoOfRounds(Integer value) {
        set(11, value);
    }

    /**
     * Getter for <code>game.no_of_rounds</code>.
     */
    public Integer getNoOfRounds() {
        return (Integer) get(11);
    }

    /**
     * Setter for <code>game.spawn_time</code>.
     */
    public void setSpawnTime(Integer value) {
        set(12, value);
    }

    /**
     * Getter for <code>game.spawn_time</code>.
     */
    public Integer getSpawnTime() {
        return (Integer) get(12);
    }

    /**
     * Setter for <code>game.spawn_delay</code>.
     */
    public void setSpawnDelay(Integer value) {
        set(13, value);
    }

    /**
     * Getter for <code>game.spawn_delay</code>.
     */
    public Integer getSpawnDelay() {
        return (Integer) get(13);
    }

    /**
     * Setter for <code>game.game_start_delay</code>.
     */
    public void setGameStartDelay(Integer value) {
        set(14, value);
    }

    /**
     * Getter for <code>game.game_start_delay</code>.
     */
    public Integer getGameStartDelay() {
        return (Integer) get(14);
    }

    /**
     * Setter for <code>game.round_start_delay</code>.
     */
    public void setRoundStartDelay(Integer value) {
        set(15, value);
    }

    /**
     * Getter for <code>game.round_start_delay</code>.
     */
    public Integer getRoundStartDelay() {
        return (Integer) get(15);
    }

    /**
     * Setter for <code>game.soldier_ff</code>.
     */
    public void setSoldierFf(Integer value) {
        set(16, value);
    }

    /**
     * Getter for <code>game.soldier_ff</code>.
     */
    public Integer getSoldierFf() {
        return (Integer) get(16);
    }

    /**
     * Setter for <code>game.soldier_ff_on_splash</code>.
     */
    public void setSoldierFfOnSplash(Integer value) {
        set(17, value);
    }

    /**
     * Getter for <code>game.soldier_ff_on_splash</code>.
     */
    public Integer getSoldierFfOnSplash() {
        return (Integer) get(17);
    }

    /**
     * Setter for <code>game.vehicle_ff</code>.
     */
    public void setVehicleFf(Integer value) {
        set(18, value);
    }

    /**
     * Getter for <code>game.vehicle_ff</code>.
     */
    public Integer getVehicleFf() {
        return (Integer) get(18);
    }

    /**
     * Setter for <code>game.vehicle_ff_on_splash</code>.
     */
    public void setVehicleFfOnSplash(Integer value) {
        set(19, value);
    }

    /**
     * Getter for <code>game.vehicle_ff_on_splash</code>.
     */
    public Integer getVehicleFfOnSplash() {
        return (Integer) get(19);
    }

    /**
     * Setter for <code>game.ff_kickback</code>.
     */
    public void setFfKickback(Integer value) {
        set(20, value);
    }

    /**
     * Getter for <code>game.ff_kickback</code>.
     */
    public Integer getFfKickback() {
        return (Integer) get(20);
    }

    /**
     * Setter for <code>game.ff_kickback_on_splash</code>.
     */
    public void setFfKickbackOnSplash(Integer value) {
        set(21, value);
    }

    /**
     * Getter for <code>game.ff_kickback_on_splash</code>.
     */
    public Integer getFfKickbackOnSplash() {
        return (Integer) get(21);
    }

    /**
     * Setter for <code>game.ticket_ratio</code>.
     */
    public void setTicketRatio(Integer value) {
        set(22, value);
    }

    /**
     * Getter for <code>game.ticket_ratio</code>.
     */
    public Integer getTicketRatio() {
        return (Integer) get(22);
    }

    /**
     * Setter for <code>game.team_kill_punish</code>.
     */
    public void setTeamKillPunish(Integer value) {
        set(23, value);
    }

    /**
     * Getter for <code>game.team_kill_punish</code>.
     */
    public Integer getTeamKillPunish() {
        return (Integer) get(23);
    }

    /**
     * Setter for <code>game.punkbuster_enabled</code>.
     */
    public void setPunkbusterEnabled(Integer value) {
        set(24, value);
    }

    /**
     * Getter for <code>game.punkbuster_enabled</code>.
     */
    public Integer getPunkbusterEnabled() {
        return (Integer) get(24);
    }

    /**
     * Setter for <code>game.auto_balance_enabled</code>.
     */
    public void setAutoBalanceEnabled(Integer value) {
        set(25, value);
    }

    /**
     * Getter for <code>game.auto_balance_enabled</code>.
     */
    public Integer getAutoBalanceEnabled() {
        return (Integer) get(25);
    }

    /**
     * Setter for <code>game.tag_distance</code>.
     */
    public void setTagDistance(Integer value) {
        set(26, value);
    }

    /**
     * Getter for <code>game.tag_distance</code>.
     */
    public Integer getTagDistance() {
        return (Integer) get(26);
    }

    /**
     * Setter for <code>game.tag_distance_scope</code>.
     */
    public void setTagDistanceScope(Integer value) {
        set(27, value);
    }

    /**
     * Getter for <code>game.tag_distance_scope</code>.
     */
    public Integer getTagDistanceScope() {
        return (Integer) get(27);
    }

    /**
     * Setter for <code>game.nose_camera_allowed</code>.
     */
    public void setNoseCameraAllowed(Integer value) {
        set(28, value);
    }

    /**
     * Getter for <code>game.nose_camera_allowed</code>.
     */
    public Integer getNoseCameraAllowed() {
        return (Integer) get(28);
    }

    /**
     * Setter for <code>game.free_camera_allowed</code>.
     */
    public void setFreeCameraAllowed(Integer value) {
        set(29, value);
    }

    /**
     * Getter for <code>game.free_camera_allowed</code>.
     */
    public Integer getFreeCameraAllowed() {
        return (Integer) get(29);
    }

    /**
     * Setter for <code>game.external_views_allowed</code>.
     */
    public void setExternalViewsAllowed(Integer value) {
        set(30, value);
    }

    /**
     * Getter for <code>game.external_views_allowed</code>.
     */
    public Integer getExternalViewsAllowed() {
        return (Integer) get(30);
    }

    /**
     * Setter for <code>game.hit_indication_enabled</code>.
     */
    public void setHitIndicationEnabled(Integer value) {
        set(31, value);
    }

    /**
     * Getter for <code>game.hit_indication_enabled</code>.
     */
    public Integer getHitIndicationEnabled() {
        return (Integer) get(31);
    }

    /**
     * Setter for <code>game.internet</code>.
     */
    public void setInternet(Integer value) {
        set(32, value);
    }

    /**
     * Getter for <code>game.internet</code>.
     */
    public Integer getInternet() {
        return (Integer) get(32);
    }

    /**
     * Setter for <code>game.coop_cpu</code>.
     */
    public void setCoopCpu(Integer value) {
        set(33, value);
    }

    /**
     * Getter for <code>game.coop_cpu</code>.
     */
    public Integer getCoopCpu() {
        return (Integer) get(33);
    }

    /**
     * Setter for <code>game.coop_skill</code>.
     */
    public void setCoopSkill(Integer value) {
        set(34, value);
    }

    /**
     * Getter for <code>game.coop_skill</code>.
     */
    public Integer getCoopSkill() {
        return (Integer) get(34);
    }

    /**
     * Setter for <code>game.allied_player_count_ratio</code>.
     */
    public void setAlliedPlayerCountRatio(Integer value) {
        set(35, value);
    }

    /**
     * Getter for <code>game.allied_player_count_ratio</code>.
     */
    public Integer getAlliedPlayerCountRatio() {
        return (Integer) get(35);
    }

    /**
     * Setter for <code>game.axis_player_count_ratio</code>.
     */
    public void setAxisPlayerCountRatio(Integer value) {
        set(36, value);
    }

    /**
     * Getter for <code>game.axis_player_count_ratio</code>.
     */
    public Integer getAxisPlayerCountRatio() {
        return (Integer) get(36);
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
     * Create a detached GameRecord
     */
    public GameRecord() {
        super(Game.GAME);
    }

    /**
     * Create a detached, initialised GameRecord
     */
    public GameRecord(Integer id, Integer serverId, Timestamp startTime, String serverName, Integer serverPort, String modId, String mapCode, String gameMode, Integer maxGameTime, Integer maxPlayers, Integer scoreLimit, Integer noOfRounds, Integer spawnTime, Integer spawnDelay, Integer gameStartDelay, Integer roundStartDelay, Integer soldierFf, Integer soldierFfOnSplash, Integer vehicleFf, Integer vehicleFfOnSplash, Integer ffKickback, Integer ffKickbackOnSplash, Integer ticketRatio, Integer teamKillPunish, Integer punkbusterEnabled, Integer autoBalanceEnabled, Integer tagDistance, Integer tagDistanceScope, Integer noseCameraAllowed, Integer freeCameraAllowed, Integer externalViewsAllowed, Integer hitIndicationEnabled, Integer internet, Integer coopCpu, Integer coopSkill, Integer alliedPlayerCountRatio, Integer axisPlayerCountRatio) {
        super(Game.GAME);

        set(0, id);
        set(1, serverId);
        set(2, startTime);
        set(3, serverName);
        set(4, serverPort);
        set(5, modId);
        set(6, mapCode);
        set(7, gameMode);
        set(8, maxGameTime);
        set(9, maxPlayers);
        set(10, scoreLimit);
        set(11, noOfRounds);
        set(12, spawnTime);
        set(13, spawnDelay);
        set(14, gameStartDelay);
        set(15, roundStartDelay);
        set(16, soldierFf);
        set(17, soldierFfOnSplash);
        set(18, vehicleFf);
        set(19, vehicleFfOnSplash);
        set(20, ffKickback);
        set(21, ffKickbackOnSplash);
        set(22, ticketRatio);
        set(23, teamKillPunish);
        set(24, punkbusterEnabled);
        set(25, autoBalanceEnabled);
        set(26, tagDistance);
        set(27, tagDistanceScope);
        set(28, noseCameraAllowed);
        set(29, freeCameraAllowed);
        set(30, externalViewsAllowed);
        set(31, hitIndicationEnabled);
        set(32, internet);
        set(33, coopCpu);
        set(34, coopSkill);
        set(35, alliedPlayerCountRatio);
        set(36, axisPlayerCountRatio);
    }
}
