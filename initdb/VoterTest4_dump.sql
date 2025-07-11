--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1
-- Dumped by pg_dump version 16.1

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

SET default_table_access_method = heap;

--
-- Name: Files; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Files" (
    "FileId" integer NOT NULL,
    "FileName" text NOT NULL,
    "FilePath" text NOT NULL,
    "UploadedDate" timestamp with time zone NOT NULL,
    "filerefId" integer DEFAULT 0 NOT NULL
);


ALTER TABLE public."Files" OWNER TO postgres;

--
-- Name: Files_FileId_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."Files" ALTER COLUMN "FileId" ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public."Files_FileId_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: Voters4; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Voters4" (
    "ID" integer NOT NULL,
    "Name" character varying(100) NOT NULL,
    "Age" integer NOT NULL,
    "IsAdult" boolean NOT NULL
);


ALTER TABLE public."Voters4" OWNER TO postgres;

--
-- Name: Voters4_ID_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."Voters4" ALTER COLUMN "ID" ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public."Voters4_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: __EFMigrationsHistory; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."__EFMigrationsHistory" (
    "MigrationId" character varying(150) NOT NULL,
    "ProductVersion" character varying(32) NOT NULL
);


ALTER TABLE public."__EFMigrationsHistory" OWNER TO postgres;

--
-- Name: voters4; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.voters4 (
    id integer NOT NULL,
    age integer NOT NULL,
    is_adult boolean NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.voters4 OWNER TO postgres;

--
-- Name: voters4_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.voters4 ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.voters4_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Data for Name: Files; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Files" ("FileId", "FileName", "FilePath", "UploadedDate", "filerefId") FROM stdin;
1	231a4971-0f3b-4b2d-b183-67476454372a.pdf	D:\\dotnet NIC\\VoterID\\static\\231a4971-0f3b-4b2d-b183-67476454372a.pdf	2024-09-09 23:39:14.640086+05:30	0
2	db8700f6-b587-4d93-abf0-bc702787d2a9.pdf	D:\\dotnet NIC\\VotersManagement\\static\\db8700f6-b587-4d93-abf0-bc702787d2a9.pdf	2024-09-09 23:57:07.349522+05:30	0
3	ed3952f8-520d-4647-a09e-d7a221138d1b.pdf	D:\\dotnet NIC\\VoterManagemnetCRUD\\static\\ed3952f8-520d-4647-a09e-d7a221138d1b.pdf	2024-09-10 00:07:57.950882+05:30	0
4	283742a1-e202-4be9-b0d4-72798bef0796.pdf	D:\\dotnet NIC\\VoterID\\static\\283742a1-e202-4be9-b0d4-72798bef0796.pdf	2024-09-10 00:30:15.493636+05:30	1
5	28e8a2b3-a13f-479e-8a80-dfcd1dea60e7.pdf	D:\\dotnet NIC\\VoterID\\static\\28e8a2b3-a13f-479e-8a80-dfcd1dea60e7.pdf	2024-09-10 00:30:17.470816+05:30	1
\.


--
-- Data for Name: Voters4; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Voters4" ("ID", "Name", "Age", "IsAdult") FROM stdin;
3	stri443ng33	43	t
2	string2222	11	f
4	shtttt	17	f
5	hastinapur	26	t
6	satringa	90	t
7	angel	50	t
8	qqqq	7	f
9	Alpha	25	t
11	delta	25	t
12	John Doe	30	t
\.


--
-- Data for Name: __EFMigrationsHistory; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."__EFMigrationsHistory" ("MigrationId", "ProductVersion") FROM stdin;
20240419103505_init24	8.0.3
20240909180641_adding file contents	8.0.3
20240909184700_second mig	8.0.3
\.


--
-- Data for Name: voters4; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.voters4 (id, age, is_adult, name) FROM stdin;
\.


--
-- Name: Files_FileId_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Files_FileId_seq"', 5, true);


--
-- Name: Voters4_ID_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Voters4_ID_seq"', 13, true);


--
-- Name: voters4_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.voters4_id_seq', 1, false);


--
-- Name: Files PK_Files; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Files"
    ADD CONSTRAINT "PK_Files" PRIMARY KEY ("FileId");


--
-- Name: Voters4 PK_Voters4; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Voters4"
    ADD CONSTRAINT "PK_Voters4" PRIMARY KEY ("ID");


--
-- Name: __EFMigrationsHistory PK___EFMigrationsHistory; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."__EFMigrationsHistory"
    ADD CONSTRAINT "PK___EFMigrationsHistory" PRIMARY KEY ("MigrationId");


--
-- Name: voters4 voters4_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voters4
    ADD CONSTRAINT voters4_pkey PRIMARY KEY (id);


--
-- PostgreSQL database dump complete
--

