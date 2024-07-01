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

CREATE TABLE players.player (
    id bigint NOT NULL,
    login character varying(20) NOT NULL UNIQUE ,
    email character varying(40) NOT NULL UNIQUE ,
    active bool NOT NULL,
    created timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    updated timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);


CREATE SEQUENCE players.seq_player
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE INDEX index_player_id ON players.player USING btree (id);
