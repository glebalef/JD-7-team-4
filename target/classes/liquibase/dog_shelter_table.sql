--liquibase formatted sql

-changeset gleb:1

CREATE TABLE dog_reports (
    id        BIGINT,
    ownerid   BIGINT,
    chatid   BIGSERIAL,
    diet      VARCHAR,
    condition VARCHAR,
    newhabits BOOLEAN,
    oldhabits BOOLEAN,
    datetime  TIMESTAMP,
    PRIMARY KEY (id)
);