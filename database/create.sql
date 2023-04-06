--
-- PostgreSQL database dump
--

-- Dumped from database version 12.9 (Ubuntu 12.9-0ubuntu0.20.04.1)
-- Dumped by pg_dump version 12.9 (Ubuntu 12.9-0ubuntu0.20.04.1)

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

--
-- Name: tdlschema; Type: SCHEMA; Schema: -; Owner: tdluser
--

CREATE SCHEMA tdlschema;


ALTER SCHEMA tdlschema OWNER TO tdluser;

--
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;


--
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: list_items; Type: TABLE; Schema: tdlschema; Owner: tdluser
--

CREATE TABLE tdlschema.list_items (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    owner_id uuid NOT NULL,
    message character varying(100) NOT NULL,
    is_done boolean NOT NULL,
    priority integer NOT NULL,
    priority_change_number integer NOT NULL
);


ALTER TABLE tdlschema.list_items OWNER TO tdluser;

--
-- Name: users; Type: TABLE; Schema: tdlschema; Owner: tdluser
--

CREATE TABLE tdlschema.users (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(50) NOT NULL
);


ALTER TABLE tdlschema.users OWNER TO tdluser;

--
-- Data for Name: list_items; Type: TABLE DATA; Schema: tdlschema; Owner: tdluser
--

COPY tdlschema.list_items (id, owner_id, message, is_done, priority, priority_change_number) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: tdlschema; Owner: tdluser
--

COPY tdlschema.users (id, username, password) FROM stdin;
\.


--
-- Name: list_items list_items_pkey; Type: CONSTRAINT; Schema: tdlschema; Owner: tdluser
--

ALTER TABLE ONLY tdlschema.list_items
    ADD CONSTRAINT list_items_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: tdlschema; Owner: tdluser
--

ALTER TABLE ONLY tdlschema.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users users_username_key; Type: CONSTRAINT; Schema: tdlschema; Owner: tdluser
--

ALTER TABLE ONLY tdlschema.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- Name: list_items li_u; Type: FK CONSTRAINT; Schema: tdlschema; Owner: tdluser
--

ALTER TABLE ONLY tdlschema.list_items
    ADD CONSTRAINT li_u FOREIGN KEY (owner_id) REFERENCES tdlschema.users(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

