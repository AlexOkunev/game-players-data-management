SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;
SET default_tablespace = '';

CREATE TABLE battle_results.player_battle_results
(
    player_id                 bigint                   NOT NULL,
    battle_id                 bigint                   NOT NULL,
    map_name                  text                     NOT NULL,
    event_id                  text                     NOT NULL,
    battle_finished_timestamp timestamp with time zone NOT NULL,
    damage_sum                integer                  NOT NULL,
    shots                     integer                  NOT NULL,
    successful_shots          integer                  NOT NULL,
    headshots                 integer                  NOT NULL,
    killed                    integer                  NOT NULL,
    deaths                    integer                  NOT NULL,
    winner                    boolean                  NOT NULL,
    PRIMARY KEY (player_id, battle_id)
);

CREATE TABLE battle_results.player
(
    id      bigint                NOT NULL,
    login   character varying(20) NOT NULL,
    email   character varying(40) NOT NULL,
    active  bool                  NOT NULL,
    created timestamp with time zone,
    updated timestamp with time zone,
    PRIMARY KEY (id)
);

CREATE TABLE battle_results.weapon
(
    id          bigint      NOT NULL,
    weapon_name varchar(30) NOT NULL,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS player_battle_results_pkey ON battle_results.player_battle_results USING btree (player_id, battle_id);

CREATE INDEX IF NOT EXISTS battle_id_index ON battle_results.player_battle_results USING btree (battle_id);

CREATE UNIQUE INDEX IF NOT EXISTS player_id_key ON battle_results.player USING btree (id);

CREATE UNIQUE INDEX IF NOT EXISTS weapon_pk ON battle_results.weapon USING btree (id);

ALTER TABLE battle_results.player_battle_results
    OWNER TO postgres;

ALTER TABLE battle_results.player
    OWNER TO postgres;

ALTER TABLE battle_results.weapon
    OWNER TO postgres;