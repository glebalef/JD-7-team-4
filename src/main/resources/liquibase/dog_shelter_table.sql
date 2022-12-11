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
