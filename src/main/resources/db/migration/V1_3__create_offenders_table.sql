CREATE TABLE IF NOT EXISTS person(
    id                SERIAL          PRIMARY KEY,
    uuid              UUID            UNIQUE NOT NULL,
    aggregate_id      UUID            NOT NULL,
    aggregate_state   VARCHAR         NOT NULL,
    given_name        VARCHAR,
    family_name       VARCHAR,
    date_of_birth     DATE,
    FOREIGN KEY (aggregate_id) REFERENCES aggregate(uuid)
);

CREATE TABLE IF NOT EXISTS person_address(
    id                  SERIAL          PRIMARY KEY,
    uuid                UUID            UNIQUE NOT NULL,
    address_id          UUID            NOT NULL,
    person_id           UUID            NOT NULL,
    address_type        VARCHAR         NOT NULL,
    FOREIGN KEY (address_id)    REFERENCES address(uuid),
    FOREIGN KEY (person_id)     REFERENCES person(uuid)
);
