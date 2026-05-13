----------------------------------------------------------------------------------
--- DS3_NEO4J_TRANSPORT_NETWORK_SparkSQL_Views.sql
----------------------------------------------------------------------------------

----------------------------------------------------------------------------------
-- 0. Test REST endpoints from SparkSQL
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8094/DSA-NoSQL-Neo4JService/rest/network/LineView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8094/DSA-NoSQL-Neo4JService/rest/network/RouteView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8094/DSA-NoSQL-Neo4JService/rest/network/StopView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8094/DSA-NoSQL-Neo4JService/rest/network/VehicleView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8094/DSA-NoSQL-Neo4JService/rest/network/DepotView');


----------------------------------------------------------------------------------
-- 1. LINES
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'NETWORK_LINES_JSON_VIEW',
               'http://localhost:8094/DSA-NoSQL-Neo4JService/rest/network/LineView');

SELECT * FROM NETWORK_LINES_JSON_VIEW;

CREATE OR REPLACE VIEW NETWORK_LINES_VIEW AS
SELECT v.*
FROM NETWORK_LINES_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM NETWORK_LINES_VIEW;


----------------------------------------------------------------------------------
-- 2. ROUTES
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'NETWORK_ROUTES_JSON_VIEW',
               'http://localhost:8094/DSA-NoSQL-Neo4JService/rest/network/RouteView');

SELECT * FROM NETWORK_ROUTES_JSON_VIEW;

CREATE OR REPLACE VIEW NETWORK_ROUTES_VIEW AS
SELECT v.*
FROM NETWORK_ROUTES_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM NETWORK_ROUTES_VIEW;


----------------------------------------------------------------------------------
-- 3. STOPS
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'NETWORK_STOPS_JSON_VIEW',
               'http://localhost:8094/DSA-NoSQL-Neo4JService/rest/network/StopView');

SELECT * FROM NETWORK_STOPS_JSON_VIEW;

CREATE OR REPLACE VIEW NETWORK_STOPS_VIEW AS
SELECT v.*
FROM NETWORK_STOPS_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM NETWORK_STOPS_VIEW;


----------------------------------------------------------------------------------
-- 4. VEHICLES
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'NETWORK_VEHICLES_JSON_VIEW',
               'http://localhost:8094/DSA-NoSQL-Neo4JService/rest/network/VehicleView');

SELECT * FROM NETWORK_VEHICLES_JSON_VIEW;

CREATE OR REPLACE VIEW NETWORK_VEHICLES_VIEW AS
SELECT v.*
FROM NETWORK_VEHICLES_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM NETWORK_VEHICLES_VIEW;


----------------------------------------------------------------------------------
-- 5. DEPOTS
----------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'NETWORK_DEPOTS_JSON_VIEW',
               'http://localhost:8094/DSA-NoSQL-Neo4JService/rest/network/DepotView');

SELECT * FROM NETWORK_DEPOTS_JSON_VIEW;

CREATE OR REPLACE VIEW NETWORK_DEPOTS_VIEW AS
SELECT v.*
FROM NETWORK_DEPOTS_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM NETWORK_DEPOTS_VIEW;