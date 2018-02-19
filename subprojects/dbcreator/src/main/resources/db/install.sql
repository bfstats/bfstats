CREATE TABLE IF NOT EXISTS player (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(150) NOT NULL,
  keyhash VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS player_nickname (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  player_id INTEGER NOT NULL,
  nickname VARCHAR(150) NOT NULL,
  times_used INTEGER NOT NULL,
  FOREIGN KEY (player_id) REFERENCES player(id)
);
CREATE INDEX IF NOT EXISTS player_nickname_player_id_idx ON player_nickname(player_id);

CREATE TABLE IF NOT EXISTS server (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  ip INTEGER, -- ip as integer
  port INTEGER NOT NULL,
  name VARCHAR(150) NOT NULL, -- last known name
  timezone_name VARCHAR(64) NOT NULL DEFAULT 'GMT' -- timezone name used in java
);

-- single log file, consists of 1 or more rounds; same map
CREATE TABLE IF NOT EXISTS game (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  gamename VARCHAR(100) NOT NULL, -- bf1942 or bfvietnam
  server_id INTEGER NOT NULL,
  start_time DATETIME NOT NULL, -- log file timestamp
  -- following attributes are basically from first round (if it exists), and are common to all rounds
  server_name VARCHAR(150) NOT NULL,
  server_port INTEGER NOT NULL,
  mod_id VARCHAR(100) NOT NULL,
  map_code VARCHAR(100) NOT NULL,
  game_mode VARCHAR(100) NOT NULL,
  max_game_time INTEGER NOT NULL,
  max_players INTEGER NOT NULL,
  score_limit INTEGER NOT NULL,
  no_of_rounds INTEGER NOT NULL, -- number of planned rounds, not necessarily actual rounds
  spawn_time INTEGER NOT NULL,
  spawn_delay INTEGER NOT NULL,
  game_start_delay INTEGER NOT NULL,
  round_start_delay INTEGER NOT NULL,
  soldier_ff INTEGER NOT NULL,
  soldier_ff_on_splash INTEGER NOT NULL,
  vehicle_ff INTEGER NOT NULL,
  vehicle_ff_on_splash INTEGER NOT NULL,
  ff_kickback INTEGER NOT NULL,
  ff_kickback_on_splash INTEGER NOT NULL,
  ticket_ratio INTEGER NOT NULL,
  team_kill_punish INTEGER NOT NULL,
  punkbuster_enabled INTEGER NOT NULL,
  auto_balance_enabled INTEGER NOT NULL,
  tag_distance INTEGER NOT NULL,
  tag_distance_scope INTEGER NOT NULL,
  nose_camera_allowed INTEGER NOT NULL,
  free_camera_allowed INTEGER NOT NULL,
  external_views_allowed INTEGER NOT NULL,
  hit_indication_enabled INTEGER NOT NULL,
  internet INTEGER NOT NULL,
  coop_cpu INTEGER NOT NULL,
  coop_skill INTEGER NOT NULL,
  allied_player_count_ratio INTEGER NOT NULL,
  axis_player_count_ratio INTEGER NOT NULL,
  FOREIGN KEY (server_id) REFERENCES server(id)
);
CREATE INDEX IF NOT EXISTS game_server_id_idx ON game(server_id);

CREATE TABLE IF NOT EXISTS round (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  game_id INTEGER NOT NULL,
  start_time DATETIME NOT NULL, -- from roundInit event
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
  round_start_delay INTEGER NOT NULL,
  soldier_ff INTEGER NOT NULL,
  soldier_ff_on_splash INTEGER NOT NULL,
  vehicle_ff INTEGER NOT NULL,
  vehicle_ff_on_splash INTEGER NOT NULL,
  ff_kickback INTEGER NOT NULL,
  ff_kickback_on_splash INTEGER NOT NULL,
  ticket_ratio INTEGER NOT NULL,
  team_kill_punish INTEGER NOT NULL,
  punkbuster_enabled INTEGER NOT NULL,
  auto_balance_enabled INTEGER NOT NULL,
  tag_distance INTEGER NOT NULL,
  tag_distance_scope INTEGER NOT NULL,
  nose_camera_allowed INTEGER NOT NULL,
  free_camera_allowed INTEGER NOT NULL,
  external_views_allowed INTEGER NOT NULL,
  hit_indication_enabled INTEGER NOT NULL,
  internet INTEGER NOT NULL,
  coop_cpu INTEGER NOT NULL,
  coop_skill INTEGER NOT NULL,
  allied_player_count_ratio INTEGER NOT NULL,
  axis_player_count_ratio INTEGER NOT NULL,
  FOREIGN KEY (game_id) REFERENCES game(id)
);
CREATE INDEX IF NOT EXISTS round_game_id_idx ON round(game_id);

CREATE TABLE IF NOT EXISTS round_end_stats (
  round_id INTEGER PRIMARY KEY,
  end_time DATETIME NOT NULL,
  winning_team INTEGER NOT NULL,
  victory_type INTEGER NOT NULL,
  end_tickets_team_1 INTEGER NOT NULL,
  end_tickets_team_2 INTEGER NOT NULL,
  FOREIGN KEY (round_id) REFERENCES round(id)
);
CREATE INDEX IF NOT EXISTS round_end_stats_round_id_idx ON round_end_stats(round_id);

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
CREATE INDEX IF NOT EXISTS round_end_stats_player_round_id_idx ON round_end_stats_player(round_id);
CREATE INDEX IF NOT EXISTS round_end_stats_player_player_id_idx ON round_end_stats_player(player_id);

CREATE TABLE IF NOT EXISTS round_chat_log (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  round_id INTEGER NOT NULL,
  player_id INTEGER NOT NULL,
  -- hesitated to put location NOT NULL, because player can chat while dead or spectator
  -- DECIMAL(4,4) because maps are usually 2000x2000, and logged with precision up to 4 digits after decimal point
  player_location_x DECIMAL(4,4),
  player_location_y DECIMAL(4,4),
  player_location_z DECIMAL(4,4),
  to_team INTEGER NOT NULL, -- 0 is all, maybe: 1 (NVA) or 2 (USA)
  message VARCHAR(50) NOT NULL,
  event_time DATETIME NOT NULL,
  FOREIGN KEY (round_id) REFERENCES round(id),
  FOREIGN KEY (player_id) REFERENCES player(id)
);
CREATE INDEX IF NOT EXISTS round_chat_log_round_id_idx ON round_chat_log(round_id);
CREATE INDEX IF NOT EXISTS round_chat_log_player_id_idx ON round_chat_log(player_id);

CREATE TABLE IF NOT EXISTS round_player_score_event (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  round_id INTEGER NOT NULL,
  player_id INTEGER NOT NULL,
  player_location_x DECIMAL(4,4) NOT NULL,
  player_location_y DECIMAL(4,4) NOT NULL,
  player_location_z DECIMAL(4,4) NOT NULL,
  event_time DATETIME NOT NULL,
  score_type VARCHAR(30) NOT NULL, -- FlagCapture, Defence
  FOREIGN KEY (round_id) REFERENCES round(id),
  FOREIGN KEY (player_id) REFERENCES player(id)
);
CREATE INDEX IF NOT EXISTS round_player_score_round_id_idx ON round_player_score_event(round_id);
CREATE INDEX IF NOT EXISTS round_player_score_player_id_idx ON round_player_score_event(player_id);
CREATE INDEX IF NOT EXISTS round_player_score_type_idx ON round_player_score_event(score_type);

CREATE TABLE IF NOT EXISTS round_player (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  joined_round_id INTEGER NOT NULL,
  end_round_id INTEGER NOT NULL,
  player_id INTEGER NOT NULL,
  start_time DATETIME NOT NULL,
  end_time DATETIME NOT NULL,
  FOREIGN KEY (joined_round_id) REFERENCES round(id),
  FOREIGN KEY (end_round_id) REFERENCES round(id),
  FOREIGN KEY (player_id) REFERENCES player(id)
);
CREATE INDEX IF NOT EXISTS round_player_joined_round_id_idx ON round_player(joined_round_id);
CREATE INDEX IF NOT EXISTS round_player_end_round_id_idx ON round_player(end_round_id);
CREATE INDEX IF NOT EXISTS round_player_player_id_idx ON round_player(player_id);

CREATE TABLE IF NOT EXISTS round_player_team (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  round_id INTEGER NOT NULL,
  player_id INTEGER NOT NULL,
  team INTEGER NOT NULL, -- 1 (NVA); 2 (USA); 3 (Spectator)
  start_time DATETIME NOT NULL,
  end_time DATETIME NOT NULL,
  FOREIGN KEY (round_id) REFERENCES round(id),
  FOREIGN KEY (player_id) REFERENCES player(id)
);
CREATE INDEX IF NOT EXISTS round_player_team_id_idx ON round_player_team(round_id);
CREATE INDEX IF NOT EXISTS round_player_team_id_idx ON round_player_team(player_id);
CREATE INDEX IF NOT EXISTS round_player_start_end_time_idx ON round_player_team(start_time, end_time);

CREATE TABLE IF NOT EXISTS round_player_death (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  round_id INTEGER NOT NULL,
  player_id INTEGER NOT NULL,
  player_location_x DECIMAL(4,4) NOT NULL,
  player_location_y DECIMAL(4,4) NOT NULL,
  player_location_z DECIMAL(4,4) NOT NULL,
  event_time DATETIME NOT NULL,
  killer_player_id INTEGER,
  killer_location_x DECIMAL(4,4),
  killer_location_y DECIMAL(4,4),
  killer_location_z DECIMAL(4,4),
  kill_type VARCHAR(30), -- Kill, TK
  kill_weapon VARCHAR(50),
  FOREIGN KEY (round_id) REFERENCES round(id),
  FOREIGN KEY (player_id) REFERENCES player(id),
  FOREIGN KEY (killer_player_id) REFERENCES player(id)
);
CREATE INDEX IF NOT EXISTS round_player_death_round_id_idx ON round_player_death(round_id);
CREATE INDEX IF NOT EXISTS round_player_death_player_id_idx ON round_player_death(player_id);
CREATE INDEX IF NOT EXISTS round_player_killer_player_id_idx ON round_player_death(killer_player_id);

CREATE TABLE IF NOT EXISTS round_player_vehicle (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  round_id INTEGER NOT NULL,
  player_id INTEGER NOT NULL,
  player_location_x DECIMAL(4,4) NOT NULL, -- entering location
  player_location_y DECIMAL(4,4) NOT NULL, -- entering location
  player_location_z DECIMAL(4,4) NOT NULL, -- entering location
  start_time DATETIME NOT NULL,
  end_time DATETIME NOT NULL,
  duration_seconds INTEGER NOT NULL,
  vehicle VARCHAR(50),
  FOREIGN KEY (round_id) REFERENCES round(id),
  FOREIGN KEY (player_id) REFERENCES player(id)
);
CREATE INDEX IF NOT EXISTS round_player_vehicle_round_id_idx ON round_player_vehicle(round_id);
CREATE INDEX IF NOT EXISTS round_player_vehicle_player_id_idx ON round_player_vehicle(player_id);

CREATE TABLE IF NOT EXISTS round_player_repair (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  round_id INTEGER NOT NULL,
  player_id INTEGER NOT NULL,
  player_location_x DECIMAL(4,4) NOT NULL, -- start location
  player_location_y DECIMAL(4,4) NOT NULL, -- start location
  player_location_z DECIMAL(4,4) NOT NULL, -- start location
  start_time DATETIME NOT NULL,
  end_time DATETIME NOT NULL,
  duration_seconds INTEGER NOT NULL,
  start_repair_status INTEGER NOT NULL,
  vehicle_type VARCHAR(50) NOT NULL,
  vehicle_player_id INTEGER,
  end_player_location_x DECIMAL(4,4),
  end_player_location_y DECIMAL(4,4),
  end_player_location_z DECIMAL(4,4),
  end_repair_status INTEGER, -- null because it might've ended with death event
  FOREIGN KEY (round_id) REFERENCES round(id),
  FOREIGN KEY (player_id) REFERENCES player(id),
  FOREIGN KEY (vehicle_player_id) REFERENCES player(id)
);
CREATE INDEX IF NOT EXISTS round_player_repair_round_id_idx ON round_player_repair(round_id);
CREATE INDEX IF NOT EXISTS round_player_repair_player_id_idx ON round_player_repair(player_id);
CREATE INDEX IF NOT EXISTS round_player_repair_vehicle_player_id_idx ON round_player_repair(vehicle_player_id);

CREATE TABLE IF NOT EXISTS round_player_medpack (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  round_id INTEGER NOT NULL,
  player_id INTEGER NOT NULL,
  player_location_x DECIMAL(4,4) NOT NULL, -- start location
  player_location_y DECIMAL(4,4) NOT NULL, -- start location
  player_location_z DECIMAL(4,4) NOT NULL, -- start location
  start_time DATETIME NOT NULL,
  end_time DATETIME NOT NULL,
  duration_seconds INTEGER NOT NULL,
  start_medpack_status INTEGER NOT NULL,
  healed_player_id INTEGER NOT NULL,
  end_player_location_x DECIMAL(4,4),
  end_player_location_y DECIMAL(4,4),
  end_player_location_z DECIMAL(4,4),
  end_medpack_status INTEGER, -- null because it might've ended with death event
  FOREIGN KEY (round_id) REFERENCES round(id),
  FOREIGN KEY (player_id) REFERENCES player(id),
  FOREIGN KEY (healed_player_id) REFERENCES player(id)
);
CREATE INDEX IF NOT EXISTS round_player_medpack_round_id_idx ON round_player_medpack(round_id);
CREATE INDEX IF NOT EXISTS round_player_medpack_player_id_idx ON round_player_medpack(player_id);
CREATE INDEX IF NOT EXISTS round_player_medpack_vehicle_healed_player_id_idx ON round_player_medpack(healed_player_id);

CREATE TABLE IF NOT EXISTS round_player_pickup_kit (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  round_id INTEGER NOT NULL,
  player_id INTEGER NOT NULL,
  player_location_x DECIMAL(4,4) NOT NULL,
  player_location_y DECIMAL(4,4) NOT NULL,
  player_location_z DECIMAL(4,4) NOT NULL,
  kit VARCHAR(50) NOT NULL,
  event_time DATETIME NOT NULL,
  FOREIGN KEY (round_id) REFERENCES round(id),
  FOREIGN KEY (player_id) REFERENCES player(id)
);
CREATE INDEX IF NOT EXISTS round_player_pickup_kit_round_id_idx ON round_player_pickup_kit(round_id);
CREATE INDEX IF NOT EXISTS round_player_pickup_kit_player_id_idx ON round_player_pickup_kit(player_id);

CREATE TABLE IF NOT EXISTS round_player_deploy_object (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  round_id INTEGER NOT NULL,
  player_id INTEGER NOT NULL,
  player_location_x DECIMAL(4,4) NOT NULL,
  player_location_y DECIMAL(4,4) NOT NULL,
  player_location_z DECIMAL(4,4) NOT NULL,
  object VARCHAR(50) NOT NULL,
  event_time DATETIME NOT NULL,
  FOREIGN KEY (round_id) REFERENCES round(id),
  FOREIGN KEY (player_id) REFERENCES player(id)
);
CREATE INDEX IF NOT EXISTS round_player_deploy_object_round_id_idx ON round_player_deploy_object(round_id);
CREATE INDEX IF NOT EXISTS round_player_deploy_object_player_id_idx ON round_player_deploy_object(player_id);

CREATE TABLE IF NOT EXISTS configuration (
  -- guarantee that configuration table is a single record table. It has to be unique and can have only one value.
  lock INTEGER PRIMARY KEY DEFAULT 1,
  last_parsed_datetime VARCHAR(30),
  CHECK (lock = 1)
);
INSERT INTO configuration (last_parsed_datetime) VALUES (null);

CREATE TABLE IF NOT EXISTS player_rank (
  rank INTEGER PRIMARY KEY,
  player_id INTEGER NOT NULL,
  FOREIGN KEY (player_id) REFERENCES player(id)
);
CREATE INDEX IF NOT EXISTS player_rank_player_id_idx ON player_rank(player_id);
