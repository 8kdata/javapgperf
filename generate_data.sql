CREATE EXTENSION IF NOT EXISTS pg_stat_statements;

DROP TABLE IF EXISTS number;

CREATE TABLE IF NOT EXISTS number AS
	SELECT i, 'Hello there ' || i AS t, '{"i": ' || i || ', "t": "' || 'Hello there ' || i || '" }' AS j
	FROM generate_series(1,10 * 1000 * 1000) AS i;

ANALYZE number;

SELECT pg_stat_statements_reset();
