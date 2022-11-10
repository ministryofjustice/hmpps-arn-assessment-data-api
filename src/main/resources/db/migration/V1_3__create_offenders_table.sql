CREATE OR REPLACE VIEW v_all_approved AS
SELECT * FROM event AS e
WHERE
	e.event_type != 'CHANGES_APPROVED' AND
	e.created_on < (
	    SELECT max(created_on)
	    FROM event
	    WHERE
	        event_type = 'CHANGES_APPROVED'
	        AND aggregate_id = e.aggregate_id
	);

CREATE OR REPLACE VIEW v_all_unapproved AS
SELECT * FROM event AS e
WHERE
	e.event_type != 'CHANGES_APPROVED' AND
	e.created_on > (
	    SELECT max(created_on)
	    FROM event
	    WHERE
	        event_type = 'CHANGES_APPROVED'
	        AND aggregate_id = e.aggregate_id
	);
