SET
statement_timeout = 0;
SET
lock_timeout = 0;
SET
idle_in_transaction_session_timeout = 0;
SET
client_encoding = 'UTF8';
SET
standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET
check_function_bodies = false;
SET
xmloption = content;
SET
client_min_messages = warning;
SET
row_security = off;
SET
default_tablespace = '';

CREATE TABLE player_stats.player_common_stats
(
    player_id                          bigint  NOT NULL,
    battles_count                      integer NOT NULL,
    shots_count                        integer NOT NULL,
    successful_shots_count             integer NOT NULL,
    headshots_count                    integer NOT NULL,
    kills_count                        integer NOT NULL,
    deaths_count                       integer NOT NULL,
    wins_count                         integer NOT NULL,
    damage_sum                         integer NOT NULL,
    successful_shots_rate              float   NOT NULL,
    headshots_to_successful_shots_rate float   NOT NULL,
    avg_successful_shot_damage         float   NOT NULL,
    wins_rate                          float   NOT NULL,
    event_id                           text    NOT NULL,
    PRIMARY KEY (player_id)
);

CREATE TABLE player_stats.player_map_stats
(
    player_id                          bigint  NOT NULL,
    map_name                           text    NOT NULL,
    battles_count                      integer NOT NULL,
    shots_count                        integer NOT NULL,
    successful_shots_count             integer NOT NULL,
    headshots_count                    integer NOT NULL,
    kills_count                        integer NOT NULL,
    deaths_count                       integer NOT NULL,
    wins_count                         integer NOT NULL,
    damage_sum                         integer NOT NULL,
    successful_shots_rate              float   NOT NULL,
    headshots_to_successful_shots_rate float   NOT NULL,
    avg_successful_shot_damage         float   NOT NULL,
    wins_rate                          float   NOT NULL,
    event_id                           text    NOT NULL,
    PRIMARY KEY (player_id, map_name)
);

CREATE TABLE player_stats.player_weapon_stats
(
    player_id                          bigint  NOT NULL,
    weapon_id                          bigint  NOT NULL,
    battles_count                      integer NOT NULL,
    shots_count                        integer NOT NULL,
    successful_shots_count             integer NOT NULL,
    headshots_count                    integer NOT NULL,
    kills_count                        integer NOT NULL,
    damage_sum                         integer NOT NULL,
    successful_shots_rate              float   NOT NULL,
    headshots_to_successful_shots_rate float   NOT NULL,
    avg_successful_shot_damage         float   NOT NULL,
    event_id                           text    NOT NULL,
    PRIMARY KEY (player_id, weapon_id)
);

CREATE TABLE player_stats.player
(
    id      bigint                NOT NULL,
    login   character varying(20) NOT NULL,
    email   character varying(40) NOT NULL,
    active  bool                  NOT NULL,
    created timestamp with time zone,
    updated timestamp with time zone,
    PRIMARY KEY (id)
);

CREATE TABLE player_stats.weapon
(
    id          bigint      NOT NULL,
    weapon_name varchar(30) NOT NULL,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS player_common_stats_pkey ON player_stats.player_common_stats USING btree (player_id);

CREATE INDEX player_common_stats_battles_count ON player_stats.player_common_stats(battles_count);

CREATE INDEX player_common_stats_shots_count ON player_stats.player_common_stats(shots_count);

CREATE INDEX player_common_stats_successful_shots_count ON player_stats.player_common_stats(successful_shots_count);

CREATE INDEX player_common_stats_headshots_count ON player_stats.player_common_stats(headshots_count);

CREATE INDEX player_common_stats_kills_count ON player_stats.player_common_stats(kills_count);

CREATE INDEX player_common_stats_deaths_count ON player_stats.player_common_stats(deaths_count);

CREATE INDEX player_common_stats_wins_count ON player_stats.player_common_stats(wins_count);

CREATE INDEX player_common_stats_damage_sum ON player_stats.player_common_stats(damage_sum);

CREATE INDEX player_common_stats_successful_shots_rate ON player_stats.player_common_stats(successful_shots_rate);

CREATE INDEX player_common_stats_headshots_to_successful_shots_rate ON player_stats.player_common_stats(headshots_to_successful_shots_rate);

CREATE INDEX player_common_stats_avg_successful_shot_damage ON player_stats.player_common_stats(avg_successful_shot_damage);

CREATE INDEX player_common_stats_wins_rate ON player_stats.player_common_stats(wins_rate);

CREATE UNIQUE INDEX IF NOT EXISTS player_map_stats_pkey ON player_stats.player_map_stats USING btree (player_id, map_name);

CREATE INDEX player_map_stats_battles_count ON player_stats.player_map_stats(battles_count);

CREATE INDEX player_map_stats_shots_count ON player_stats.player_map_stats(shots_count);

CREATE INDEX player_map_stats_successful_shots_count ON player_stats.player_map_stats(successful_shots_count);

CREATE INDEX player_map_stats_headshots_count ON player_stats.player_map_stats(headshots_count);

CREATE INDEX player_map_stats_kills_count ON player_stats.player_map_stats(kills_count);

CREATE INDEX player_map_stats_deaths_count ON player_stats.player_map_stats(deaths_count);

CREATE INDEX player_map_stats_wins_count ON player_stats.player_map_stats(wins_count);

CREATE INDEX player_map_stats_damage_sum ON player_stats.player_map_stats(damage_sum);

CREATE INDEX player_map_stats_successful_shots_rate ON player_stats.player_map_stats(successful_shots_rate);

CREATE INDEX player_map_stats_headshots_to_successful_shots_rate ON player_stats.player_map_stats(headshots_to_successful_shots_rate);

CREATE INDEX player_map_stats_avg_successful_shot_damage ON player_stats.player_map_stats(avg_successful_shot_damage);

CREATE INDEX player_map_stats_wins_rate ON player_stats.player_map_stats(wins_rate);

CREATE UNIQUE INDEX IF NOT EXISTS player_weapon_stats_pkey ON player_stats.player_weapon_stats USING btree (player_id, weapon_id);

CREATE INDEX player_weapon_stats_battles_count ON player_stats.player_weapon_stats(battles_count);

CREATE INDEX player_weapon_stats_shots_count ON player_stats.player_weapon_stats(shots_count);

CREATE INDEX player_weapon_stats_successful_shots_count ON player_stats.player_weapon_stats(successful_shots_count);

CREATE INDEX player_weapon_stats_headshots_count ON player_stats.player_weapon_stats(headshots_count);

CREATE INDEX player_weapon_stats_kills_count ON player_stats.player_weapon_stats(kills_count);

CREATE INDEX player_weapon_stats_damage_sum ON player_stats.player_weapon_stats(damage_sum);

CREATE INDEX player_weapon_stats_successful_shots_rate ON player_stats.player_weapon_stats(successful_shots_rate);

CREATE INDEX player_weapon_stats_headshots_to_successful_shots_rate ON player_stats.player_weapon_stats(headshots_to_successful_shots_rate);

CREATE INDEX player_weapon_stats_avg_successful_shot_damage ON player_stats.player_weapon_stats(avg_successful_shot_damage);

CREATE UNIQUE INDEX IF NOT EXISTS player_id_key ON player_stats.player USING btree (id);

CREATE UNIQUE INDEX IF NOT EXISTS weapon_pk ON player_stats.weapon USING btree (id);

ALTER TABLE player_stats.player_common_stats
    OWNER TO postgres;

ALTER TABLE player_stats.player_map_stats
    OWNER TO postgres;

ALTER TABLE player_stats.player_weapon_stats
    OWNER TO postgres;

ALTER TABLE player_stats.player
    OWNER TO postgres;

ALTER TABLE player_stats.weapon
    OWNER TO postgres;