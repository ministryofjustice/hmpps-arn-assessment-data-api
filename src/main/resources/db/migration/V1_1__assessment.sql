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
    offender_id           INT             NOT NULL,
    support_needs_id      INT                     ,
    version               VARCHAR(255)    NOT NULL,
    created_date          TIMESTAMP       NOT NULL,
    completed_date        TIMESTAMP               ,
    FOREIGN KEY (support_needs_id) REFERENCES support_need(id),
    FOREIGN KEY (offender_id) REFERENCES offender(id)
);


-- INSERT INTO assessment(assessment_type, support_needs_id, version, created_date, completed_date)
-- VALUES
--     ('UPW', '1', '1.1', '2020-11-30 14:50:00', '2020-11-30 14:50:00'),
--     ('UPW', '2', '1.1', '2020-11-30 14:50:00', null);

