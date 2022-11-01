CREATE TABLE IF NOT EXISTS risk(
    id                SERIAL          PRIMARY KEY,
    uuid              UUID            UNIQUE NOT NULL,
    aggregate_id      UUID            NOT NULL,
    riskOne           DECIMAL(4,2),
    riskTwo           DECIMAL(4,2),
    score             DECIMAL(4,2)     NOT NULL,
    risk_level        VARCHAR(10)     NOT NULL,
    version           VARCHAR(10)     NOT NULL,
    FOREIGN KEY (aggregate_id) REFERENCES aggregate(uuid)
);
