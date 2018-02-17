package io.github.bfstats.logparser.xml.enums.event;

public enum ScoreEventParams {
  player_id, player_location, score_type, victim_id, weapon // weapon "(none)" should be parsed to null
}

//scoreEvent (FlagCapture, Attack, Defence, Kill, Death, DeathNoMsg, TK, Spawned)
