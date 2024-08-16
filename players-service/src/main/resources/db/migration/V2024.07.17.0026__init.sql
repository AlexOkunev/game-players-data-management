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

CREATE TABLE players.player
(
    id      bigint                                                                   NOT NULL,
    login   character varying(20)                                                    NOT NULL UNIQUE,
    email   character varying(40)                                                    NOT NULL UNIQUE,
    active  bool                                                                     NOT NULL,
    created timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    updated timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    PRIMARY KEY (id)
);


CREATE SEQUENCE players.seq_player
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE UNIQUE INDEX IF NOT EXISTS player_id_key ON players.player USING btree (id);

CREATE UNIQUE INDEX IF NOT EXISTS index_player_login ON players.player USING btree (login);

CREATE UNIQUE INDEX IF NOT EXISTS index_player_email ON players.player USING btree (email);


