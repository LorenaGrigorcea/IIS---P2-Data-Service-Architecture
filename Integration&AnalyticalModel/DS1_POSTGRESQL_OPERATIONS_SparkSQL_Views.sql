----------------------------------------------------------------------------------
--- DS1_POSTGRESQL_OPERATIONS_SparkSQL_Views.sql
----------------------------------------------------------------------------------

----------------------------------------------------------------------------------
-- 0. Test REST endpoints from SparkSQL
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8092/DSA_SQL_JPAService_PostgreSQL/rest/operations/EmployeesView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8092/DSA_SQL_JPAService_PostgreSQL/rest/operations/DepotsView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8092/DSA_SQL_JPAService_PostgreSQL/rest/operations/VehiclesView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8092/DSA_SQL_JPAService_PostgreSQL/rest/operations/ShiftsView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8092/DSA_SQL_JPAService_PostgreSQL/rest/operations/AttendanceView');


----------------------------------------------------------------------------------
-- 1. EMPLOYEES
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'EMPLOYEES_JSON_VIEW',
               'http://localhost:8092/DSA_SQL_JPAService_PostgreSQL/rest/operations/EmployeesView');

SELECT * FROM EMPLOYEES_JSON_VIEW;

-- DROP VIEW EMPLOYEES_VIEW;
CREATE OR REPLACE VIEW EMPLOYEES_VIEW AS
SELECT
       CAST(v.employee_id AS BIGINT) AS employee_id,
       v.first_name AS first_name,
       v.last_name AS last_name,
       v.role AS role,
       TO_DATE(SUBSTR(v.hire_date, 1, 10)) AS hire_date,
       v.status AS status,
       CAST(v.depot_id AS BIGINT) AS depot_id
FROM EMPLOYEES_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

-- Test Remote View
SELECT * FROM EMPLOYEES_VIEW;


----------------------------------------------------------------------------------
-- 2. DEPOTS
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'DEPOTS_JSON_VIEW',
               'http://localhost:8092/DSA_SQL_JPAService_PostgreSQL/rest/operations/DepotsView');

SELECT * FROM DEPOTS_JSON_VIEW;

-- DROP VIEW DEPOTS_VIEW;
CREATE OR REPLACE VIEW DEPOTS_VIEW AS
SELECT
       CAST(v.depot_id AS BIGINT) AS depot_id,
       v.depot_name AS depot_name,
       v.city AS city,
       v.location AS location
FROM DEPOTS_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

-- Test Remote View
SELECT * FROM DEPOTS_VIEW;


----------------------------------------------------------------------------------
-- 3. VEHICLES
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'VEHICLES_JSON_VIEW',
               'http://localhost:8092/DSA_SQL_JPAService_PostgreSQL/rest/operations/VehiclesView');

SELECT * FROM VEHICLES_JSON_VIEW;

-- DROP VIEW VEHICLES_VIEW;
CREATE OR REPLACE VIEW VEHICLES_VIEW AS
SELECT
       CAST(v.vehicle_id AS BIGINT) AS vehicle_id,
       v.fleet_number AS fleet_number,
       v.vehicle_type AS vehicle_type,
       CAST(v.capacity AS INT) AS capacity,
       v.status AS status,
       CAST(v.depot_id AS BIGINT) AS depot_id
FROM VEHICLES_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

-- Test Remote View
SELECT * FROM VEHICLES_VIEW;


----------------------------------------------------------------------------------
-- 4. SHIFTS
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'SHIFTS_JSON_VIEW',
               'http://localhost:8092/DSA_SQL_JPAService_PostgreSQL/rest/operations/ShiftsView');

SELECT * FROM SHIFTS_JSON_VIEW;

-- DROP VIEW SHIFTS_VIEW;
CREATE OR REPLACE VIEW SHIFTS_VIEW AS
SELECT
       CAST(v.shift_id AS BIGINT) AS shift_id,
       CAST(v.employee_id AS BIGINT) AS employee_id,
       CAST(v.vehicle_id AS BIGINT) AS vehicle_id,
       v.line_id AS line_id,
       v.route_id AS route_id,
       TO_TIMESTAMP(REPLACE(v.shift_start_ts, 'T', ' ')) AS shift_start_ts,
       TO_TIMESTAMP(REPLACE(v.shift_end_ts, 'T', ' ')) AS shift_end_ts,
       v.shift_type AS shift_type
FROM SHIFTS_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

-- Test Remote View
SELECT * FROM SHIFTS_VIEW;


----------------------------------------------------------------------------------
-- 5. ATTENDANCE
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'ATTENDANCE_JSON_VIEW',
               'http://localhost:8092/DSA_SQL_JPAService_PostgreSQL/rest/operations/AttendanceView');

SELECT * FROM ATTENDANCE_JSON_VIEW;

-- DROP VIEW ATTENDANCE_VIEW;
CREATE OR REPLACE VIEW ATTENDANCE_VIEW AS
SELECT
       CAST(v.attendance_id AS BIGINT) AS attendance_id,
       CAST(v.employee_id AS BIGINT) AS employee_id,
       TO_DATE(SUBSTR(v.work_date, 1, 10)) AS work_date,
       v.status AS status,
       CAST(v.hours_worked AS DECIMAL(4,2)) AS hours_worked
FROM ATTENDANCE_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

-- Test Remote View
SELECT * FROM ATTENDANCE_VIEW;


