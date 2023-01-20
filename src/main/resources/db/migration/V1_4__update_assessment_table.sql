ALTER TABLE assessment
    ADD subject  UUID;

ALTER TABLE assessment
    ADD CONSTRAINT fk_assessment_subject
    FOREIGN KEY (subject) REFERENCES aggregate(uuid);
