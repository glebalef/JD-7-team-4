-- liquibase formatted sql

-- changeset EvgenyL:1
CREATE TABLE dog
(
    id            BIGSERIAL primary key,
    name          VARCHAR,
    age           INTEGER,
    breed         VARCHAR
);

CREATE TABLE cat
(
    id            BIGSERIAL primary key,
    name          VARCHAR,
    age           INTEGER,
    breed         VARCHAR
);

CREATE TABLE person_dog
(
    id            BIGSERIAL primary key,
    chat_id       BIGSERIAL,
    first_name    VARCHAR,
    last_name     VARCHAR,
    phone         VARCHAR,
    dog_id        BIGSERIAL REFERENCES dog (id)
);

CREATE TABLE person_cat
(
    id            BIGSERIAL primary key,
    chat_id       BIGSERIAL,
    first_name    VARCHAR,
    last_name     VARCHAR,
    phone         VARCHAR,
    cat_id        BIGSERIAL REFERENCES cat (id)
);

CREATE TABLE dog_report
(
    id              BIGSERIAL PRIMARY KEY,
    chat_id         BIGSERIAL,
    person_dog_id   BIGSERIAL REFERENCES person_dog (id),
    diet            TEXT,
    condition       TEXT,
    new_habits      BOOLEAN,
    old_habits      BOOLEAN,
    report_date     TIMESTAMP,
    file_id         TEXT
);

CREATE TABLE cat_report
(
    id              BIGSERIAL PRIMARY KEY,
    chat_id         BIGSERIAL,
    person_cat_id   BIGSERIAL REFERENCES person_cat (id),
    diet            TEXT,
    condition       TEXT,
    new_habits      BOOLEAN,
    old_habits      BOOLEAN,
    report_date     TIMESTAMP,
    file_id         TEXT
);

CREATE TABLE context
(
    id              BIGSERIAL PRIMARY KEY,
    chat_id         BIGSERIAL,
    type            VARCHAR,
    add_days        VARCHAR,
    test_off        BOOLEAN,
    person_dog_id   BIGSERIAL REFERENCES person_dog (id),
    person_cat_id   BIGSERIAL REFERENCES person_cat (id)
);

-- changeset EvgenyL:2
ALTER TABLE dog_report DROP COLUMN report_date,
    ADD COLUMN dog_report DATE;

-- changeset EvgenyL:3
ALTER TABLE cat_report DROP COLUMN report_date,
                       ADD COLUMN cat_report DATE;

-- changeset EvgenyL:4
ALTER TABLE dog_report DROP COLUMN dog_report,
                       ADD COLUMN report_date DATE;

-- changeset EvgenyL:5
ALTER TABLE cat_report DROP COLUMN cat_report,
                       ADD COLUMN report_date DATE;


