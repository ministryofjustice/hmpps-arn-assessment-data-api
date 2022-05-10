-- noinspection SqlResolveForFile

CREATE TABLE IF NOT EXISTS offender
(
    id                  SERIAL          PRIMARY KEY,
    first_name          VARCHAR(255)    NOT NULL,
    last_name           VARCHAR(255)    NOT NULL,
    crn                 VARCHAR(255)    NOT NULL UNIQUE,
    gender              VARCHAR(255)    NOT NULL,
    created_date        TIMESTAMP       NOT NULL
);