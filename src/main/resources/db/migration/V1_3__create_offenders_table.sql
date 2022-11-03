CREATE OR REPLACE VIEW v_approved_address AS
SELECT DISTINCT address_event.aggregate_id,
                address_event.created_on,
                address_event.event_values->>'building' AS building,
                address_event.event_values->>'postcode' AS postcode
FROM event address_event
RIGHT JOIN (
    SELECT e.aggregate_id, max(created_on) AS max_created_on FROM event e
    WHERE
        e.event_type = 'CHANGED_ADDRESS' AND
        e.created_on <
        (SELECT max(created_on) FROM event WHERE event_type = 'CHANGES_APPROVED' AND aggregate_id = e.aggregate_id)
    GROUP BY aggregate_id
) AS j_most_recent
ON j_most_recent.max_created_on = address_event.created_on;

CREATE OR REPLACE VIEW v_approved_address_moves AS
SELECT
    person_event.uuid AS event_id,
    person_event.aggregate_id AS person_id,
    person_event.created_on,
    person_event.event_values->>'addressUuid' AS address_id,
    person_event.event_values->>'addressType' AS address_type
FROM event person_event
RIGHT JOIN (
    SELECT address_event.aggregate_id,max(created_on) AS latest_created_on,address_event.event_values->>'addressType' AS address_type
    FROM event AS address_event
    WHERE address_event.event_type = 'PERSON_MOVED_ADDRESS'
    AND address_event.created_on <
    (SELECT max(created_on) FROM event WHERE event_type = 'CHANGES_APPROVED' AND aggregate_id = address_event.aggregate_id)
    GROUP BY aggregate_id, address_type
) AS j_most_recent
ON j_most_recent.latest_created_on = person_event.created_on;

CREATE OR REPLACE VIEW v_person_address AS
SELECT
    person_address.event_id,
    person_address.person_id,
    person_address.created_on,
    person_address.address_type,
    person_address.address_id,
    j_address.building,
    j_address.postcode
FROM v_approved_address_moves as person_address
RIGHT JOIN (
    SELECT approved_address.aggregate_id, approved_address.building, approved_address.postcode FROM v_approved_address AS approved_address
) AS j_address
ON j_address.aggregate_id::varchar = person_address.address_id;
