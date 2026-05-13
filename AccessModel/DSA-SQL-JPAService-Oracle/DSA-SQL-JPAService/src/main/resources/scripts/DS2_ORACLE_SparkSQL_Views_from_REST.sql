----------------------------------------------------------------------------------
-- DS1_ORACLE_TICKETING_SparkSQL_Views.sql
-- Source: DSA-SQL-JPAService, Oracle Ticketing Access Model
----------------------------------------------------------------------------------

----------------------------------------------------------------------------------
-- 0. Test REST endpoints from SparkSQL
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8091/DSA_SQL_JPAService/rest/ticketing/TicketSalesView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8091/DSA_SQL_JPAService/rest/ticketing/TicketTypesView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8091/DSA_SQL_JPAService/rest/ticketing/PaymentsView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8091/DSA_SQL_JPAService/rest/ticketing/TicketValidationsView');


----------------------------------------------------------------------------------
-- 1. TICKET SALES
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'TICKETS_SALES_JSON_VIEW',
               'http://localhost:8091/DSA_SQL_JPAService/rest/ticketing/TicketSalesView');

SELECT * FROM TICKETS_SALES_JSON_VIEW;

-- DROP VIEW TICKETS_SALES_VIEW;
CREATE OR REPLACE VIEW TICKETS_SALES_VIEW AS
SELECT
       CAST(v.order_id AS BIGINT) AS order_id,
       v.city AS city,
       v.zone AS zone,
       v.customer_category AS customer_category,
       CAST(v.amount_paid AS DECIMAL(10,2)) AS amount_paid,
       TO_DATE(SUBSTR(v.purchase_date, 1, 10)) AS purchase_date
FROM TICKETS_SALES_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

-- Test Remote View
SELECT * FROM TICKETS_SALES_VIEW;


----------------------------------------------------------------------------------
-- 2. TICKET TYPES
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'TICKET_TYPES_JSON_VIEW',
               'http://localhost:8091/DSA_SQL_JPAService/rest/ticketing/TicketTypesView');

SELECT * FROM TICKET_TYPES_JSON_VIEW;

-- DROP VIEW TICKET_TYPES_VIEW;
CREATE OR REPLACE VIEW TICKET_TYPES_VIEW AS
SELECT
       CAST(v.ticket_type_id AS BIGINT) AS ticket_type_id,
       v.ticket_name AS ticket_name,
       CAST(v.base_price AS DECIMAL(10,2)) AS base_price,
       CAST(v.validity_minutes AS INT) AS validity_minutes,
       CAST(v.validity_days AS INT) AS validity_days
FROM TICKET_TYPES_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

-- Test Remote View
SELECT * FROM TICKET_TYPES_VIEW;


----------------------------------------------------------------------------------
-- 3. PAYMENTS
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'PAYMENTS_JSON_VIEW',
               'http://localhost:8091/DSA_SQL_JPAService/rest/ticketing/PaymentsView');

SELECT * FROM PAYMENTS_JSON_VIEW;

-- DROP VIEW PAYMENTS_VIEW;
CREATE OR REPLACE VIEW PAYMENTS_VIEW AS
SELECT
       CAST(v.payment_id AS BIGINT) AS payment_id,
       CAST(v.order_id AS BIGINT) AS order_id,
       v.method AS method,
       v.status AS status,
       TO_TIMESTAMP(REPLACE(v.pay_ts, 'T', ' ')) AS pay_ts
FROM PAYMENTS_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

-- Test Remote View
SELECT * FROM PAYMENTS_VIEW;


----------------------------------------------------------------------------------
-- 4. TICKET VALIDATIONS
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'TICKET_VALIDATIONS_JSON_VIEW',
               'http://localhost:8091/DSA_SQL_JPAService/rest/ticketing/TicketValidationsView');

SELECT * FROM TICKET_VALIDATIONS_JSON_VIEW;

-- DROP VIEW TICKET_VALIDATIONS_VIEW;
CREATE OR REPLACE VIEW TICKET_VALIDATIONS_VIEW AS
SELECT
       CAST(v.validation_id AS BIGINT) AS validation_id,
       CAST(v.order_id AS BIGINT) AS order_id,
       TO_TIMESTAMP(REPLACE(v.validation_ts, 'T', ' ')) AS validation_ts,
       CAST(v.ticket_type_id AS BIGINT) AS ticket_type_id,
       v.line_id AS line_id,
       v.route_id AS route_id,
       v.stop_id AS stop_id,
       v.vehicle_id AS vehicle_id
FROM TICKET_VALIDATIONS_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

-- Test Remote View
SELECT * FROM TICKET_VALIDATIONS_VIEW;


