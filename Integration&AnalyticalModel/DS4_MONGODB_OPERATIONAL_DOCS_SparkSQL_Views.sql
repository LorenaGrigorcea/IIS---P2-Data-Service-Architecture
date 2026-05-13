----------------------------------------------------------------------------------
--- DS4_MONGODB_OPERATIONAL_DOCS_SparkSQL_Views.sql
----------------------------------------------------------------------------------

----------------------------------------------------------------------------------
-- 0. Test REST endpoints from SparkSQL
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/transport/IncidentView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/transport/MaintenanceWindowView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/transport/SpecialEventView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/transport/TelemetryLogView');


----------------------------------------------------------------------------------
-- 1. INCIDENTS
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'INCIDENTS_JSON_VIEW',
               'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/transport/IncidentView');

SELECT * FROM INCIDENTS_JSON_VIEW;

-- DROP VIEW INCIDENTS_VIEW;
CREATE OR REPLACE VIEW INCIDENTS_VIEW AS
SELECT
       v.incident_id AS incident_id,
       TO_TIMESTAMP(REPLACE(v.created_ts, 'T', ' ')) AS created_ts,
       v.category AS category,
       v.severity AS severity,
       v.line_id AS line_id,
       v.route_id AS route_id,
       v.stop_id AS stop_id,
       v.description AS description,
       v.status AS status
FROM INCIDENTS_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

-- Test Remote View
SELECT * FROM INCIDENTS_VIEW;


----------------------------------------------------------------------------------
-- 2. MAINTENANCE WINDOWS
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'MAINTENANCE_WINDOWS_JSON_VIEW',
               'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/transport/MaintenanceWindowView');

SELECT * FROM MAINTENANCE_WINDOWS_JSON_VIEW;

-- DROP VIEW MAINTENANCE_WINDOWS_VIEW;
CREATE OR REPLACE VIEW MAINTENANCE_WINDOWS_VIEW AS
SELECT
       v.work_id AS work_id,
       TO_TIMESTAMP(REPLACE(v.start_ts, 'T', ' ')) AS start_ts,
       TO_TIMESTAMP(REPLACE(v.end_ts, 'T', ' ')) AS end_ts,
       v.line_id AS line_id,
       v.route_id AS route_id,
       v.work_type AS work_type,
       v.impact_level AS impact_level
FROM MAINTENANCE_WINDOWS_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

-- Test Remote View
SELECT * FROM MAINTENANCE_WINDOWS_VIEW;


----------------------------------------------------------------------------------
-- 3. MAINTENANCE AFFECTED STOPS
----------------------------------------------------------------------------------

-- DROP VIEW MAINTENANCE_AFFECTED_STOPS_VIEW;
CREATE OR REPLACE VIEW MAINTENANCE_AFFECTED_STOPS_VIEW AS
SELECT
       v.work_id AS work_id,
       affected_stop AS stop_id
FROM MAINTENANCE_WINDOWS_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v
LATERAL VIEW explode(v.affected_stops) AS affected_stop;

-- Test Remote View
SELECT * FROM MAINTENANCE_AFFECTED_STOPS_VIEW;


----------------------------------------------------------------------------------
-- 4. SPECIAL EVENTS
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'SPECIAL_EVENTS_JSON_VIEW',
               'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/transport/SpecialEventView');

SELECT * FROM SPECIAL_EVENTS_JSON_VIEW;

-- DROP VIEW SPECIAL_EVENTS_VIEW;
CREATE OR REPLACE VIEW SPECIAL_EVENTS_VIEW AS
SELECT
       v.event_id AS event_id,
       TO_DATE(SUBSTR(v.event_date, 1, 10)) AS event_date,
       v.area AS area,
       v.event_type AS event_type,
       CAST(v.expected_traffic_multiplier AS DECIMAL(5,2)) AS expected_traffic_multiplier
FROM SPECIAL_EVENTS_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

-- Test Remote View
SELECT * FROM SPECIAL_EVENTS_VIEW;


----------------------------------------------------------------------------------
-- 5. TELEMETRY LOGS
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'TELEMETRY_LOGS_JSON_VIEW',
               'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/transport/TelemetryLogView');

SELECT * FROM TELEMETRY_LOGS_JSON_VIEW;

-- DROP VIEW TELEMETRY_LOGS_VIEW;
CREATE OR REPLACE VIEW TELEMETRY_LOGS_VIEW AS
SELECT
       v.telemetry_id AS telemetry_id,
       TO_TIMESTAMP(REPLACE(v.ts, 'T', ' ')) AS ts,
       v.vehicle_id AS vehicle_id,
       v.line_id AS line_id,
       v.route_id AS route_id,
       v.stop_id AS stop_id,
       CAST(v.speed AS DECIMAL(6,2)) AS speed,
       CAST(v.occupancy_estimate AS INT) AS occupancy_estimate
FROM TELEMETRY_LOGS_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

-- Test Remote View
SELECT * FROM TELEMETRY_LOGS_VIEW;