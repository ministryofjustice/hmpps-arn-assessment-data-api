CREATE TABLE IF NOT EXISTS support_need
(
    id                    SERIAL          PRIMARY KEY,
    type                  VARCHAR(255)    NOT NULL,
    details               TEXT
);


CREATE TABLE IF NOT EXISTS assessment
(
    id                    SERIAL          PRIMARY KEY,
    assessment_type       VARCHAR(255)    NOT NULL,
    support_needs_id      INT                     ,
    version               VARCHAR(255)    NOT NULL,
    created_date          TIMESTAMP       NOT NULL,
    completed_date        TIMESTAMP               ,
    FOREIGN KEY (support_needs_id) REFERENCES support_need(id)
);


-- INSERT INTO offender(first_name, last_name, crn, gender, created_date)
-- VALUES
--     ('Fred', 'Smith', 'V12345', 'MALE', '2020-11-30 14:50:00'),
--     ('Ted', 'Bundy', 'X54321', 'MALE', '2020-11-30 14:50:00');

