CREATE TABLE IF NOT EXISTS aggregate(
    id                SERIAL          PRIMARY KEY,
    uuid              UUID            UNIQUE NOT NULL,
    aggregate_type    VARCHAR(255)    NOT NULL
);

CREATE TABLE IF NOT EXISTS address(
    id                SERIAL          PRIMARY KEY,
    uuid              UUID            UNIQUE NOT NULL,
    aggregate_id      UUID            NOT NULL,
    state             VARCHAR         NOT NULL,
    building          VARCHAR,
    postcode          VARCHAR,
    FOREIGN KEY (aggregate_id) REFERENCES aggregate(uuid)
);

CREATE TABLE IF NOT EXISTS event(
    id                SERIAL          PRIMARY KEY,
    uuid              UUID            UNIQUE NOT NULL,
    created_on        TIMESTAMP       NOT NULL,
    created_by        VARCHAR(255)    NOT NULL,
    aggregate_id      UUID            NOT NULL,
    event_type        VARCHAR(255)    NOT NULL,
    values            JSON,
    FOREIGN KEY (aggregate_id) REFERENCES aggregate(uuid)
);
