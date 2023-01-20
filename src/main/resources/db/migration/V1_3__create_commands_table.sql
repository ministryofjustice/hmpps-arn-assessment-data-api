CREATE TABLE IF NOT EXISTS command(
    id                SERIAL          PRIMARY KEY,
    uuid              UUID            UNIQUE NOT NULL,
    aggregate_id      UUID            NOT NULL,
    command_type        VARCHAR(255)    NOT NULL,
    command_values            JSON,
    FOREIGN KEY (aggregate_id) REFERENCES aggregate(uuid)
);
