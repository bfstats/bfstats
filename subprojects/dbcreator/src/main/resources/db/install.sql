CREATE TABLE IF NOT EXISTS player (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(150) NOT NULL,
  keyhash VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS round (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  start_time DATETIME NOT NULL,
  start_tickets_team_1 INTEGER NOT NULL, -- from roundInit event
  start_tickets_team_2 INTEGER NOT NULL, -- from roundInit event
  server_name VARCHAR(150) NOT NULL,
  server_port INTEGER NOT NULL,
  mod_id VARCHAR(100) NOT NULL,
  map_code VARCHAR(100) NOT NULL,
  game_mode VARCHAR(100) NOT NULL,
  max_game_time INTEGER NOT NULL,
  max_players INTEGER NOT NULL,
  score_limit INTEGER NOT NULL,
  no_of_rounds INTEGER NOT NULL,
  spawn_time INTEGER NOT NULL,
  spawn_delay INTEGER NOT NULL,
  game_start_delay INTEGER NOT NULL,
  soldier_ff INTEGER NOT NULL,
  vehicle_ff INTEGER NOT NULL,
  ticket_ratio INTEGER NOT NULL,
  team_kill_punish INTEGER NOT NULL,
  punkbuster_enabled INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS round_end_stats (
  round_id INTEGER PRIMARY KEY,
  end_time DATETIME NOT NULL,
  winning_team INTEGER NOT NULL,
  victory_type INTEGER NOT NULL,
  end_tickets_team_1 INTEGER NOT NULL,
  end_tickets_team_2 INTEGER NOT NULL,
  FOREIGN KEY (round_id) REFERENCES round(id)
);

CREATE TABLE IF NOT EXISTS round_end_stats_player (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  round_id INTEGER NOT NULL,
  player_id INTEGER NOT NULL,
  player_name VARCHAR(150) NOT NULL,
  rank INTEGER NOT NULL,
  is_ai INTEGER NOT NULL,
  team INTEGER NOT NULL, -- 1 (NVA) or 2 (USA)
  score INTEGER NOT NULL,
  kills INTEGER NOT NULL,
  deaths INTEGER NOT NULL,
  tks INTEGER NOT NULL,
  captures INTEGER NOT NULL,
  attacks INTEGER NOT NULL,
  defences INTEGER NOT NULL,
  FOREIGN KEY (round_id) REFERENCES round_end_stats(round_id),
  FOREIGN KEY (player_id) REFERENCES player(id)
);

CREATE TABLE IF NOT EXISTS round_chat_log (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  round_id INTEGER NOT NULL,
  player_id INTEGER NOT NULL,
  -- hesitated to put location NOT NULL, because player can chat while dead or spectator
  -- DECIMAL(4,4) because maps are usually 2000x2000, and logged with precision up to 4 digits after decimal point
  player_location_x DECIMAL(4,4),
  player_location_y DECIMAL(4,4),
  player_location_z DECIMAL(4,4),
  team INTEGER NOT NULL, -- 0 is all, maybe: 1 (NVA) or 2 (USA)
  message VARCHAR(50) NOT NULL,
  event_time DATETIME NOT NULL,
  FOREIGN KEY (round_id) REFERENCES round(round_id),
  FOREIGN KEY (player_id) REFERENCES player(id)
);

CREATE TABLE IF NOT EXISTS round_player_score_event (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  round_id INTEGER NOT NULL,
  player_id INTEGER NOT NULL,
  player_location_x DECIMAL(4,4) NOT NULL,
  player_location_y DECIMAL(4,4) NOT NULL,
  player_location_z DECIMAL(4,4) NOT NULL,
  event_time DATETIME NOT NULL,
  score_type VARCHAR(30) NOT NULL, -- Kill, TK,  Death, DeathNoMsg,  FlagCapture, Defence
  victim_id INTEGER, -- if type is TK or Kill
  weapon VARCHAR(50),
  FOREIGN KEY (round_id) REFERENCES round(round_id),
  FOREIGN KEY (player_id) REFERENCES player(id),
  FOREIGN KEY (victim_id) REFERENCES player(id)
);
