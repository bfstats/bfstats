/**
 * This class is generated by jOOQ
 */
package io.github.bfvstats.game.jooq.tables.records;


import io.github.bfvstats.game.jooq.tables.RoundEndStatsPlayer;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record13;
import org.jooq.Row13;
import org.jooq.impl.UpdatableRecordImpl;


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
public class RoundEndStatsPlayerRecord extends UpdatableRecordImpl<RoundEndStatsPlayerRecord> implements Record13<Integer, Integer, Integer, String, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> {

    private static final long serialVersionUID = -561943602;

    /**
     * Setter for <code>round_end_stats_player.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>round_end_stats_player.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>round_end_stats_player.round_id</code>.
     */
    public void setRoundId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>round_end_stats_player.round_id</code>.
     */
    public Integer getRoundId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>round_end_stats_player.player_id</code>.
     */
    public void setPlayerId(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>round_end_stats_player.player_id</code>.
     */
    public Integer getPlayerId() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>round_end_stats_player.player_name</code>.
     */
    public void setPlayerName(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>round_end_stats_player.player_name</code>.
     */
    public String getPlayerName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>round_end_stats_player.is_ai</code>.
     */
    public void setIsAi(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>round_end_stats_player.is_ai</code>.
     */
    public Integer getIsAi() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>round_end_stats_player.team</code>.
     */
    public void setTeam(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>round_end_stats_player.team</code>.
     */
    public Integer getTeam() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>round_end_stats_player.score</code>.
     */
    public void setScore(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>round_end_stats_player.score</code>.
     */
    public Integer getScore() {
        return (Integer) get(6);
    }

    /**
     * Setter for <code>round_end_stats_player.kills</code>.
     */
    public void setKills(Integer value) {
        set(7, value);
    }

    /**
     * Getter for <code>round_end_stats_player.kills</code>.
     */
    public Integer getKills() {
        return (Integer) get(7);
    }

    /**
     * Setter for <code>round_end_stats_player.deaths</code>.
     */
    public void setDeaths(Integer value) {
        set(8, value);
    }

    /**
     * Getter for <code>round_end_stats_player.deaths</code>.
     */
    public Integer getDeaths() {
        return (Integer) get(8);
    }

    /**
     * Setter for <code>round_end_stats_player.tks</code>.
     */
    public void setTks(Integer value) {
        set(9, value);
    }

    /**
     * Getter for <code>round_end_stats_player.tks</code>.
     */
    public Integer getTks() {
        return (Integer) get(9);
    }

    /**
     * Setter for <code>round_end_stats_player.captures</code>.
     */
    public void setCaptures(Integer value) {
        set(10, value);
    }

    /**
     * Getter for <code>round_end_stats_player.captures</code>.
     */
    public Integer getCaptures() {
        return (Integer) get(10);
    }

    /**
     * Setter for <code>round_end_stats_player.attacks</code>.
     */
    public void setAttacks(Integer value) {
        set(11, value);
    }

    /**
     * Getter for <code>round_end_stats_player.attacks</code>.
     */
    public Integer getAttacks() {
        return (Integer) get(11);
    }

    /**
     * Setter for <code>round_end_stats_player.defences</code>.
     */
    public void setDefences(Integer value) {
        set(12, value);
    }

    /**
     * Getter for <code>round_end_stats_player.defences</code>.
     */
    public Integer getDefences() {
        return (Integer) get(12);
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
    // Record13 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row13<Integer, Integer, Integer, String, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> fieldsRow() {
        return (Row13) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row13<Integer, Integer, Integer, String, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> valuesRow() {
        return (Row13) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field2() {
        return RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.ROUND_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field3() {
        return RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.PLAYER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.PLAYER_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field5() {
        return RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.IS_AI;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field6() {
        return RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.TEAM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field7() {
        return RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.SCORE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field8() {
        return RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.KILLS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field9() {
        return RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.DEATHS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field10() {
        return RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.TKS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field11() {
        return RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.CAPTURES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field12() {
        return RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.ATTACKS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field13() {
        return RoundEndStatsPlayer.ROUND_END_STATS_PLAYER.DEFENCES;
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
    public Integer value2() {
        return getRoundId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value3() {
        return getPlayerId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getPlayerName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value5() {
        return getIsAi();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value6() {
        return getTeam();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value7() {
        return getScore();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value8() {
        return getKills();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value9() {
        return getDeaths();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value10() {
        return getTks();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value11() {
        return getCaptures();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value12() {
        return getAttacks();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value13() {
        return getDefences();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundEndStatsPlayerRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundEndStatsPlayerRecord value2(Integer value) {
        setRoundId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundEndStatsPlayerRecord value3(Integer value) {
        setPlayerId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundEndStatsPlayerRecord value4(String value) {
        setPlayerName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundEndStatsPlayerRecord value5(Integer value) {
        setIsAi(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundEndStatsPlayerRecord value6(Integer value) {
        setTeam(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundEndStatsPlayerRecord value7(Integer value) {
        setScore(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundEndStatsPlayerRecord value8(Integer value) {
        setKills(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundEndStatsPlayerRecord value9(Integer value) {
        setDeaths(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundEndStatsPlayerRecord value10(Integer value) {
        setTks(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundEndStatsPlayerRecord value11(Integer value) {
        setCaptures(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundEndStatsPlayerRecord value12(Integer value) {
        setAttacks(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundEndStatsPlayerRecord value13(Integer value) {
        setDefences(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundEndStatsPlayerRecord values(Integer value1, Integer value2, Integer value3, String value4, Integer value5, Integer value6, Integer value7, Integer value8, Integer value9, Integer value10, Integer value11, Integer value12, Integer value13) {
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
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RoundEndStatsPlayerRecord
     */
    public RoundEndStatsPlayerRecord() {
        super(RoundEndStatsPlayer.ROUND_END_STATS_PLAYER);
    }

    /**
     * Create a detached, initialised RoundEndStatsPlayerRecord
     */
    public RoundEndStatsPlayerRecord(Integer id, Integer roundId, Integer playerId, String playerName, Integer isAi, Integer team, Integer score, Integer kills, Integer deaths, Integer tks, Integer captures, Integer attacks, Integer defences) {
        super(RoundEndStatsPlayer.ROUND_END_STATS_PLAYER);

        set(0, id);
        set(1, roundId);
        set(2, playerId);
        set(3, playerName);
        set(4, isAi);
        set(5, team);
        set(6, score);
        set(7, kills);
        set(8, deaths);
        set(9, tks);
        set(10, captures);
        set(11, attacks);
        set(12, defences);
    }
}