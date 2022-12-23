-- liquibase formatted sql

-- changeset EvgenyF:1
CREATE TABLE person
(
    id                     BIGINT primary key,
    chat_id                BIGINT,
    name                   TEXT,
    phone                  TEXT
);

-- changeset EvgenyF:2
Alter TABLE person rename column name to first_name;
ALTER table person add column last_name text;

-- changeset EvgenyF:3
Alter table person drop column name;


-- changeset Gleb:4
CREATE TABLE dog_report
(
     id                     BIGSERIAL primary key,
     diet                   TEXT,
     condition              TEXT,
     newhabits              BOOLEAN,
     oldhabits              BOOLEAN,
     report_date            timestamp,
     dog_id                 BIGSERIAL,
     file_id                TEXT
);

-- changeset Gleb:5
CREATE TABLE dog (
    id BIGSERIAL primary key,
    age INTEGER,
    breed VARCHAR,
    name VARCHAR,
    person_id INTEGER
);

-- changeset Gleb:6
DROP TABLE dog CASCADE;

-- changeset EvgeniyL:7
CREATE TABLE cat (
    id          BIGSERIAL primary key,
    age         INTEGER,
    breed       VARCHAR,
    name        VARCHAR,
    person_id   INTEGER
);

-- changeset EvgenyF:8
ALTER  TABLE person RENAME TO person_dog;

-- changeset EvgenyF:9
CREATE TABLE person_cat
(
    id                     BIGINT primary key,
    chat_id                BIGINT,
    name                   TEXT,
    phone                  TEXT,
    shelter_menu           TEXT
);

-- changeset EvgenyF:10
DROP  TABLE person_cat CASCADE;

-- changeset EvgenyF:11
CREATE TABLE person_cat
(
    id                     BIGINT primary key,
    chat_id                BIGINT,
    first_name             TEXT,
    last_name              TEXT,
    phone                  TEXT,
    shelter_menu           TEXT
);
-- changeset EvgenyF:13
DROP  TABLE person_dog CASCADE;
-- changeset EvgenyF:14
CREATE TABLE person_dog
(
    id                     BIGINT primary key,
    chat_id                BIGINT,
    first_name             TEXT,
    last_name              TEXT,
    phone                  TEXT,
    shelter_menu           TEXT
);

-- changeset EvgenyF:15
DROP  TABLE person_dog CASCADE;

-- changeset EvgenyF:16
DROP  TABLE person_cat CASCADE;

-- changeset EvgenyF:17
CREATE TABLE person_dog
(
    id                     BIGINT primary key,
    chat_id                BIGINT,
    first_name             TEXT,
    last_name              TEXT,
    phone                  TEXT,
    shelter_menu           TEXT
);
-- changeset EvgenyF:18

CREATE TABLE person_cat
(
    id                     BIGINT primary key,
    chat_id                BIGINT,
    first_name             TEXT,
    last_name              TEXT,
    phone                  TEXT,
    shelter_menu           TEXT
);

-- changeset EvgenyF:19

ALTER  TABLE person_cat DROP COLUMN shelter_menu;
ALTER  TABLE person_dog DROP COLUMN shelter_menu;

-- changeset EvgenyF:20
ALTER  TABLE cat DROP COLUMN person_id;
ALTER  TABLE dog DROP COLUMN person_id;

-- changeset EvgenyF:21
ALTER table dog_report alter column report_date type date;

-- changeset EvgenyF:22
ALTER table cat_report alter column report_date type date;


-- changeset EvgenyF:23
CREATE TABLE context (
id                     BIGINT primary key,
chat_id                BIGINT,
type                   TEXT,
add_days               TEXT,
test_off               BOOLEAN
);
-- changeset EvgenyF:23
Alter table context drop column id,
    add column id bigserial primary key;

