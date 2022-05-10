CREATE TABLE IF NOT EXISTS offender
(
    id                  SERIAL          PRIMARY KEY,
    first_name          VARCHAR(255)    NOT NULL,
    last_name           VARCHAR(255)    NOT NULL,
    crn                 VARCHAR(255)    NOT NULL UNIQUE,
    gender              VARCHAR(255)    NOT NULL,
    created_date        TIMESTAMP       NOT NULL
);

INSERT INTO offender(first_name, last_name, crn, gender, created_date)
VALUES
    ('Fred', 'Smith', 'V12345', 'MALE', '2020-11-30 14:50:00'),
    ('Ted', 'Bundy', 'X54321', 'MALE', '2020-11-30 14:50:00');