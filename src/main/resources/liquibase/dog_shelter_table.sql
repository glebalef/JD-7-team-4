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


-- --changeset Gleb:4
-- CREATE TABLE dog_report
-- (
--     id                     BIGSERIAL primary key,
--     dog                    dog,
--     phone                  TEXT,
--     diet                   TEXT,
--     condition              TEXT,
--     oldhabits              BOOLEAN,
--     newhabits              BOOLEAN
--
-- );
--
-- --changeset Gleb:5
-- ALTER TABLE dog_report ADD COLUMN report_date timestamp;

-- changeset Gleb:6
ALTER TABLE dog_report ADD COLUMN file_Id TEXT;

