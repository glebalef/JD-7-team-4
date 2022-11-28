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

