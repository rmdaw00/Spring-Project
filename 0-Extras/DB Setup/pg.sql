-- Database: ticketBooking

-- DROP DATABASE "ticketBooking";

CREATE DATABASE "ticketBooking"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'English_Canada.1252'
    LC_CTYPE = 'English_Canada.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

GRANT ALL ON DATABASE "ticketBooking" TO postgres;

GRANT TEMPORARY, CONNECT ON DATABASE "ticketBooking" TO PUBLIC;






-- Table: public.events

-- DROP TABLE public.events;

CREATE TABLE IF NOT EXISTS public.events
(
    eventid bigint NOT NULL DEFAULT nextval('events_eventid_seq'::regclass),
    eventtitle character varying(50) COLLATE pg_catalog."default",
    eventdate date,
    CONSTRAINT events_pkey PRIMARY KEY (eventid)
)

TABLESPACE pg_default;

ALTER TABLE public.events
    OWNER to postgres;
-- Index: idx_res_date_

-- DROP INDEX public.idx_res_date_;

CREATE INDEX idx_res_date_
    ON public.events USING btree
    (eventdate ASC NULLS LAST)
    TABLESPACE pg_default;








    -- Table: public.tickets

    -- DROP TABLE public.tickets;

    CREATE TABLE IF NOT EXISTS public.tickets
    (
        ticketid bigint NOT NULL DEFAULT nextval('tickets_ticketid_seq'::regclass),
        eventid bigint NOT NULL DEFAULT nextval('tickets_eventid_seq'::regclass),
        userid bigint NOT NULL DEFAULT nextval('tickets_userid_seq'::regclass),
        ticketcategory integer,
        ticketplace integer,
        CONSTRAINT tickets_pkey PRIMARY KEY (ticketid),
        CONSTRAINT tickets_eventid_fkey FOREIGN KEY (eventid)
            REFERENCES public.events (eventid) MATCH SIMPLE
            ON UPDATE NO ACTION
            ON DELETE NO ACTION,
        CONSTRAINT tickets_userid_fkey FOREIGN KEY (userid)
            REFERENCES public.users (userid) MATCH SIMPLE
            ON UPDATE NO ACTION
            ON DELETE NO ACTION
    )

    TABLESPACE pg_default;

    ALTER TABLE public.tickets
        OWNER to postgres;







        -- Table: public.users

-- DROP TABLE public.users;

CREATE TABLE IF NOT EXISTS public.users
(
    userid bigint NOT NULL DEFAULT nextval('users_userid_seq'::regclass),
    username character varying(50) COLLATE pg_catalog."default",
    useremail character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (userid),
    CONSTRAINT users_useremail_key UNIQUE (useremail)
)

TABLESPACE pg_default;

ALTER TABLE public.users
    OWNER to postgres;
