--------------------------------------------------------------------------------
-- PUBLIC_TRANSPORT_SPARKSQL_ANALYTICAL_QUERIES.sql
-- source views -> canonical views -> dimensions -> facts -> analytical views

--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- 0. Verificare minima a surselor incarcate in SparkSQL
--------------------------------------------------------------------------------
SELECT COUNT(*) AS cnt_ticket_sales FROM TICKETS_SALES_VIEW;
SELECT COUNT(*) AS cnt_vehicles     FROM VEHICLES_VIEW;
SELECT COUNT(*) AS cnt_lines        FROM NETWORK_LINES_VIEW;
SELECT COUNT(*) AS cnt_incidents    FROM INCIDENTS_VIEW;

--------------------------------------------------------------------------------
-- 1. Canonical/consolidation views
-- pastram nume stabile pentru stratul analitic, indiferent de sursa REST.
--------------------------------------------------------------------------------

DROP VIEW IF EXISTS V_TICKET_SALES;
CREATE OR REPLACE VIEW V_TICKET_SALES AS
SELECT * FROM TICKETS_SALES_VIEW;

DROP VIEW IF EXISTS V_TICKET_TYPES;
CREATE OR REPLACE VIEW V_TICKET_TYPES AS
SELECT * FROM TICKET_TYPES_VIEW;

DROP VIEW IF EXISTS V_TICKET_VALIDATIONS;
CREATE OR REPLACE VIEW V_TICKET_VALIDATIONS AS
SELECT * FROM TICKET_VALIDATIONS_VIEW;

DROP VIEW IF EXISTS DS2_VEHICLES_V;
CREATE OR REPLACE VIEW DS2_VEHICLES_V AS
SELECT
    CAST(vehicle_id AS STRING) AS vehicle_id,
    CAST(vehicle_id AS STRING) AS vehicle_code,
    fleet_number,
    vehicle_type,
    capacity,
    status,
    depot_id
FROM VEHICLES_VIEW;

DROP VIEW IF EXISTS DS2_DEPOTS_V;
CREATE OR REPLACE VIEW DS2_DEPOTS_V AS
SELECT * FROM DEPOTS_VIEW;

DROP VIEW IF EXISTS DS3_LINES_V;
CREATE OR REPLACE VIEW DS3_LINES_V AS
SELECT
    CAST(lineId AS STRING) AS line_id,
    CAST(displayName AS STRING) AS line_name,
    CAST(mode AS STRING) AS line_mode,
    CAST(realLineNo AS STRING) AS real_line_no,
    CAST(sourceOverlap AS BOOLEAN) AS source_overlap
FROM NETWORK_LINES_VIEW;

DROP VIEW IF EXISTS DS3_ROUTES_V;
CREATE OR REPLACE VIEW DS3_ROUTES_V AS
SELECT
    CAST(routeId AS STRING) AS route_id,
    CAST(lineId AS STRING) AS line_id,
    CAST(direction AS STRING) AS direction
FROM NETWORK_ROUTES_VIEW;

DROP VIEW IF EXISTS DS3_STOPS_V;
CREATE OR REPLACE VIEW DS3_STOPS_V AS
SELECT
    CAST(stopId AS STRING) AS stop_id,
    CAST(stopName AS STRING) AS stop_name,
    CAST(area AS STRING) AS area,
    CAST(sharedCore AS BOOLEAN) AS shared_core
FROM NETWORK_STOPS_VIEW;

-- Demo result pentru canonical views, doar cateva exemple.
SELECT * FROM V_TICKET_SALES LIMIT 5;
SELECT * FROM DS2_VEHICLES_V LIMIT 5;
SELECT * FROM DS3_LINES_V LIMIT 5;

--------------------------------------------------------------------------------
-- 2. Dimensions
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- 2.1 OLAP_DIM_TICKET
-- Clauze speciale: CASE, coloane calculate pentru unitatea si valoarea valabilitatii.
--------------------------------------------------------------------------------
DROP VIEW IF EXISTS OLAP_DIM_TICKET;

CREATE OR REPLACE VIEW OLAP_DIM_TICKET AS
SELECT
    ticket_type_id,
    ticket_name,
    base_price,
    validity_minutes,
    validity_days,
    CASE
        WHEN validity_minutes IS NOT NULL THEN 'MINUTES'
        WHEN validity_days IS NOT NULL THEN 'DAYS'
        ELSE 'UNKNOWN'
    END AS validity_unit,
    CASE
        WHEN validity_minutes IS NOT NULL THEN validity_minutes
        WHEN validity_days IS NOT NULL THEN validity_days
        ELSE NULL
    END AS validity_value
FROM V_TICKET_TYPES;

-- Demo result
SELECT * FROM OLAP_DIM_TICKET ORDER BY ticket_type_id;

--------------------------------------------------------------------------------
-- 2.2 OLAP_DIM_SALES_AREA
-- Clauze speciale: DISTINCT pentru eliminarea zonelor duplicate.
--------------------------------------------------------------------------------
DROP VIEW IF EXISTS OLAP_DIM_SALES_AREA;

CREATE OR REPLACE VIEW OLAP_DIM_SALES_AREA AS
SELECT DISTINCT
    city,
    zone
FROM V_TICKET_SALES;

-- Demo result
SELECT * FROM OLAP_DIM_SALES_AREA ORDER BY city, zone;

--------------------------------------------------------------------------------
-- 2.3 OLAP_DIM_NETWORK
-- Clauze speciale: UNION, LEFT JOIN, COALESCE.
-- Integreaza validari Oracle, evenimente MongoDB si reteaua Neo4j.
--------------------------------------------------------------------------------
DROP VIEW IF EXISTS OLAP_DIM_NETWORK;

CREATE OR REPLACE VIEW OLAP_DIM_NETWORK AS
SELECT DISTINCT
    n.line_id,
    COALESCE(l.line_name, n.line_id) AS line_name,
    COALESCE(l.line_mode, 'UNKNOWN') AS line_mode,
    n.route_id,
    r.direction,
    n.stop_id,
    COALESCE(s.stop_name, n.stop_id) AS stop_name,
    s.area AS stop_area,
    s.shared_core
FROM (
    SELECT DISTINCT line_id, route_id, stop_id
    FROM V_TICKET_VALIDATIONS
    WHERE line_id IS NOT NULL AND route_id IS NOT NULL AND stop_id IS NOT NULL

    UNION

    SELECT DISTINCT line_id, route_id, stop_id
    FROM INCIDENTS_VIEW
    WHERE line_id IS NOT NULL AND route_id IS NOT NULL AND stop_id IS NOT NULL

    UNION

    SELECT DISTINCT line_id, route_id, stop_id
    FROM TELEMETRY_LOGS_VIEW
    WHERE line_id IS NOT NULL AND route_id IS NOT NULL AND stop_id IS NOT NULL
) n
LEFT JOIN DS3_LINES_V  l ON n.line_id = l.line_id
LEFT JOIN DS3_ROUTES_V r ON n.route_id = r.route_id AND n.line_id = r.line_id
LEFT JOIN DS3_STOPS_V  s ON n.stop_id = s.stop_id;

-- Demo result
SELECT * FROM OLAP_DIM_NETWORK ORDER BY line_id, route_id, stop_id;

--------------------------------------------------------------------------------
-- 2.4 OLAP_DIM_VEHICLE_DEPOT
-- Clauze speciale: LEFT JOIN intre vehicule si depouri.
-- Pastreaza in model si sursa PostgreSQL.
--------------------------------------------------------------------------------
DROP VIEW IF EXISTS OLAP_DIM_VEHICLE_DEPOT;

CREATE OR REPLACE VIEW OLAP_DIM_VEHICLE_DEPOT AS
SELECT DISTINCT
    v.vehicle_id,
    v.vehicle_code,
    v.fleet_number,
    v.vehicle_type,
    v.capacity,
    v.status AS vehicle_status,
    d.depot_id,
    d.depot_name,
    d.city AS depot_city,
    d.location AS depot_location
FROM DS2_VEHICLES_V v
LEFT JOIN DS2_DEPOTS_V d ON v.depot_id = d.depot_id;

-- Demo result
SELECT * FROM OLAP_DIM_VEHICLE_DEPOT ORDER BY depot_id, vehicle_id;

--------------------------------------------------------------------------------
-- 2.5 OLAP_DIM_EVENT_TYPE
-- Clauze speciale: UNION, CASE, CONCAT.
-- Normalizeaza incidentele, mentenanta, evenimentele speciale si telemetria.
--------------------------------------------------------------------------------
DROP VIEW IF EXISTS OLAP_DIM_EVENT_TYPE;

CREATE OR REPLACE VIEW OLAP_DIM_EVENT_TYPE AS
SELECT DISTINCT
    'INCIDENT' AS source_type,
    category AS event_subtype,
    severity AS impact_label,
    status AS status_label
FROM INCIDENTS_VIEW

UNION

SELECT DISTINCT
    'MAINTENANCE' AS source_type,
    work_type AS event_subtype,
    impact_level AS impact_label,
    'scheduled' AS status_label
FROM MAINTENANCE_WINDOWS_VIEW

UNION

SELECT DISTINCT
    'SPECIAL_EVENT' AS source_type,
    event_type AS event_subtype,
    CONCAT('traffic x', CAST(expected_traffic_multiplier AS STRING)) AS impact_label,
    'planned' AS status_label
FROM SPECIAL_EVENTS_VIEW

UNION

SELECT DISTINCT
    'TELEMETRY' AS source_type,
    'telemetry_log' AS event_subtype,
    CASE
        WHEN occupancy_estimate >= 70 THEN 'HIGH_OCCUPANCY'
        WHEN occupancy_estimate >= 40 THEN 'MEDIUM_OCCUPANCY'
        ELSE 'LOW_OCCUPANCY'
    END AS impact_label,
    'observed' AS status_label
FROM TELEMETRY_LOGS_VIEW;

-- Demo result
SELECT * FROM OLAP_DIM_EVENT_TYPE ORDER BY source_type, event_subtype, impact_label;

--------------------------------------------------------------------------------
-- 3. Facts
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- 3.1 OLAP_FACTS_TICKET_SALES
-- Clauze speciale: subquery cu DISTINCT, INNER JOIN, GROUP BY, SUM.
--------------------------------------------------------------------------------
DROP VIEW IF EXISTS OLAP_FACTS_TICKET_SALES;

CREATE OR REPLACE VIEW OLAP_FACTS_TICKET_SALES AS
SELECT
    TO_DATE(s.purchase_date) AS full_date,
    v.ticket_type_id,
    s.city,
    s.zone,
    s.customer_category,
    COUNT(*) AS sales_count,
    SUM(COALESCE(s.amount_paid, 0)) AS total_amount_paid
FROM V_TICKET_SALES s
INNER JOIN (
    SELECT DISTINCT order_id, ticket_type_id
    FROM V_TICKET_VALIDATIONS
) v ON s.order_id = v.order_id
GROUP BY
    TO_DATE(s.purchase_date),
    v.ticket_type_id,
    s.city,
    s.zone,
    s.customer_category;

-- Demo result
SELECT *
FROM OLAP_FACTS_TICKET_SALES
ORDER BY full_date, ticket_type_id, city, zone, customer_category
LIMIT 20;

--------------------------------------------------------------------------------
-- 3.2 OLAP_FACTS_VALIDATIONS_NETWORK
-- Clauze speciale: GROUP BY multidimensional pe timp, bilet, retea si vehicul.
--------------------------------------------------------------------------------
DROP VIEW IF EXISTS OLAP_FACTS_VALIDATIONS_NETWORK;

CREATE OR REPLACE VIEW OLAP_FACTS_VALIDATIONS_NETWORK AS
SELECT
    TO_DATE(validation_ts) AS full_date,
    ticket_type_id,
    line_id,
    route_id,
    stop_id,
    vehicle_id,
    COUNT(*) AS validation_count
FROM V_TICKET_VALIDATIONS
GROUP BY
    TO_DATE(validation_ts),
    ticket_type_id,
    line_id,
    route_id,
    stop_id,
    vehicle_id;

-- Demo result
SELECT *
FROM OLAP_FACTS_VALIDATIONS_NETWORK
ORDER BY full_date, line_id, route_id, stop_id
LIMIT 20;

--------------------------------------------------------------------------------
-- 3.3 OLAP_FACTS_OPERATIONAL_EVENTS
-- Clauze speciale: UNION ALL, CAST, CASE.
-- Integreaza sursele operationale MongoDB intr-un fact comun.
--------------------------------------------------------------------------------
DROP VIEW IF EXISTS OLAP_FACTS_OPERATIONAL_EVENTS;

CREATE OR REPLACE VIEW OLAP_FACTS_OPERATIONAL_EVENTS AS
SELECT
    TO_DATE(created_ts) AS full_date,
    'INCIDENT' AS source_type,
    category AS event_subtype,
    severity AS impact_label,
    status AS status_label,
    line_id,
    route_id,
    stop_id,
    CAST(NULL AS STRING) AS vehicle_id,
    1 AS event_count,
    CAST(NULL AS DOUBLE) AS speed,
    CAST(NULL AS INT) AS occupancy_estimate,
    CAST(NULL AS DOUBLE) AS traffic_multiplier
FROM INCIDENTS_VIEW

UNION ALL

SELECT
    TO_DATE(start_ts) AS full_date,
    'MAINTENANCE' AS source_type,
    work_type AS event_subtype,
    impact_level AS impact_label,
    'scheduled' AS status_label,
    line_id,
    route_id,
    CAST(NULL AS STRING) AS stop_id,
    CAST(NULL AS STRING) AS vehicle_id,
    1 AS event_count,
    CAST(NULL AS DOUBLE) AS speed,
    CAST(NULL AS INT) AS occupancy_estimate,
    CAST(NULL AS DOUBLE) AS traffic_multiplier
FROM MAINTENANCE_WINDOWS_VIEW

UNION ALL

SELECT
    TO_DATE(event_date) AS full_date,
    'SPECIAL_EVENT' AS source_type,
    event_type AS event_subtype,
    CONCAT('traffic x', CAST(expected_traffic_multiplier AS STRING)) AS impact_label,
    'planned' AS status_label,
    CAST(NULL AS STRING) AS line_id,
    CAST(NULL AS STRING) AS route_id,
    CAST(NULL AS STRING) AS stop_id,
    CAST(NULL AS STRING) AS vehicle_id,
    1 AS event_count,
    CAST(NULL AS DOUBLE) AS speed,
    CAST(NULL AS INT) AS occupancy_estimate,
    CAST(expected_traffic_multiplier AS DOUBLE) AS traffic_multiplier
FROM SPECIAL_EVENTS_VIEW

UNION ALL

SELECT
    TO_DATE(ts) AS full_date,
    'TELEMETRY' AS source_type,
    'telemetry_log' AS event_subtype,
    CASE
        WHEN occupancy_estimate >= 70 THEN 'HIGH_OCCUPANCY'
        WHEN occupancy_estimate >= 40 THEN 'MEDIUM_OCCUPANCY'
        ELSE 'LOW_OCCUPANCY'
    END AS impact_label,
    'observed' AS status_label,
    line_id,
    route_id,
    stop_id,
    vehicle_id,
    1 AS event_count,
    CAST(speed AS DOUBLE) AS speed,
    occupancy_estimate,
    CAST(NULL AS DOUBLE) AS traffic_multiplier
FROM TELEMETRY_LOGS_VIEW;

-- Demo result
SELECT *
FROM OLAP_FACTS_OPERATIONAL_EVENTS
ORDER BY full_date, source_type, line_id, route_id, stop_id
LIMIT 30;

--------------------------------------------------------------------------------
-- 3.4 OLAP_DIM_TIME
-- Clauze speciale: UNION, DISTINCT, functii calendar YEAR, MONTH, DAYOFMONTH.
-- Varianta este construita din facts, ca sa evitam citirea repetata a tuturor surselor REST.
--------------------------------------------------------------------------------
DROP VIEW IF EXISTS OLAP_DIM_TIME;

CREATE OR REPLACE VIEW OLAP_DIM_TIME AS
SELECT DISTINCT
    full_date,
    YEAR(full_date)       AS year_no,
    MONTH(full_date)      AS month_no,
    DAYOFMONTH(full_date) AS day_no
FROM (
    SELECT full_date FROM OLAP_FACTS_TICKET_SALES
    WHERE full_date IS NOT NULL

    UNION

    SELECT full_date FROM OLAP_FACTS_VALIDATIONS_NETWORK
    WHERE full_date IS NOT NULL

    UNION

    SELECT full_date FROM OLAP_FACTS_OPERATIONAL_EVENTS
    WHERE full_date IS NOT NULL
) d;

-- Demo result
SELECT * FROM OLAP_DIM_TIME ORDER BY full_date;

--------------------------------------------------------------------------------
-- 4. Analytical views
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- 4.1 OLAP_VIEW_SALES_CALENDAR
-- Clauze speciale: ROLLUP, GROUPING, CASE, ROUND.
-- analiza vânzările în timp: pe zi, pe lună, pe an și total general, leagă fact-ul de vânzări cu dimensiunea de timp.
--------------------------------------------------------------------------------
DROP VIEW IF EXISTS OLAP_VIEW_SALES_CALENDAR;

CREATE OR REPLACE VIEW OLAP_VIEW_SALES_CALENDAR AS
SELECT
    CASE
        WHEN GROUPING(d.year_no) = 1 THEN '{Total General}'
        ELSE CAST(d.year_no AS STRING)
    END AS year_no,
    CASE
        WHEN GROUPING(d.year_no) = 1 THEN ' '
        WHEN GROUPING(d.month_no) = 1 THEN CONCAT('subtotal an ', CAST(d.year_no AS STRING))
        ELSE CAST(d.month_no AS STRING)
    END AS month_no,
    CASE
        WHEN GROUPING(d.year_no) = 1 THEN ' '
        WHEN GROUPING(d.month_no) = 1 THEN ' '
        WHEN GROUPING(d.day_no) = 1 THEN CONCAT('subtotal luna ', CAST(d.month_no AS STRING))
        ELSE CAST(d.day_no AS STRING)
    END AS day_no,
    SUM(COALESCE(f.sales_count, 0)) AS sales_count,
    SUM(COALESCE(f.total_amount_paid, 0)) AS total_amount_paid,
    ROUND(
        SUM(COALESCE(f.total_amount_paid, 0)) /
        CASE WHEN SUM(COALESCE(f.sales_count, 0)) = 0 THEN NULL ELSE SUM(COALESCE(f.sales_count, 0)) END,
        2
    ) AS avg_amount_per_sale
FROM OLAP_DIM_TIME d
INNER JOIN OLAP_FACTS_TICKET_SALES f ON d.full_date = f.full_date
GROUP BY ROLLUP(d.year_no, d.month_no, d.day_no);

-- Demo result
SELECT * FROM OLAP_VIEW_SALES_CALENDAR ORDER BY year_no, month_no, day_no;

--------------------------------------------------------------------------------
-- 4.2 OLAP_VIEW_SALES_TICKET_AREA_CUBE
-- Clauze speciale: CUBE, GROUPING, CASE.
-- Analiza: vanzari pe tip de bilet si zona, cu subtotaluri pe ambele axe, factul pentru vanzari tichete si dimensiunile tichete si sales area
--------------------------------------------------------------------------------
DROP VIEW IF EXISTS OLAP_VIEW_SALES_TICKET_AREA_CUBE;

CREATE OR REPLACE VIEW OLAP_VIEW_SALES_TICKET_AREA_CUBE AS
SELECT
    CASE
        WHEN GROUPING(t.ticket_name) = 1 THEN '{Total General}'
        ELSE t.ticket_name
    END AS ticket_name,
    CASE
        WHEN GROUPING(t.ticket_name) = 1 AND GROUPING(a.zone) = 1 THEN ' '
        WHEN GROUPING(t.ticket_name) = 1 THEN CONCAT('subtotal zona ', a.zone)
        WHEN GROUPING(a.zone) = 1 THEN CONCAT('subtotal bilet ', t.ticket_name)
        ELSE a.zone
    END AS zone,
    SUM(COALESCE(f.sales_count, 0)) AS sales_count,
    SUM(COALESCE(f.total_amount_paid, 0)) AS total_amount_paid,
    ROUND(
        SUM(COALESCE(f.total_amount_paid, 0)) /
        CASE WHEN SUM(COALESCE(f.sales_count, 0)) = 0 THEN NULL ELSE SUM(COALESCE(f.sales_count, 0)) END,
        2
    ) AS avg_amount_per_sale
FROM OLAP_FACTS_TICKET_SALES f
INNER JOIN OLAP_DIM_TICKET t ON f.ticket_type_id = t.ticket_type_id
INNER JOIN OLAP_DIM_SALES_AREA a ON f.city = a.city AND f.zone = a.zone
GROUP BY CUBE(t.ticket_name, a.zone);

-- Demo result
SELECT * FROM OLAP_VIEW_SALES_TICKET_AREA_CUBE ORDER BY ticket_name, zone;

--------------------------------------------------------------------------------
-- 4.3 OLAP_VIEW_VALIDATIONS_NETWORK
-- Clauze speciale: CTE, LEFT JOIN, COALESCE, ROLLUP.
-- Analiza: validari pe linie, ruta, statie si totaluri ierarhice. Factul vlidations network si dimesniunea network
--------------------------------------------------------------------------------
DROP VIEW IF EXISTS OLAP_VIEW_VALIDATIONS_NETWORK;

CREATE OR REPLACE VIEW OLAP_VIEW_VALIDATIONS_NETWORK AS
WITH network_base AS (
    SELECT
        COALESCE(n.line_name, f.line_id) AS line_name,
        COALESCE(n.route_id, f.route_id) AS route_id,
        COALESCE(n.stop_name, f.stop_id) AS stop_name,
        SUM(COALESCE(f.validation_count, 0)) AS validation_count
    FROM OLAP_FACTS_VALIDATIONS_NETWORK f
    LEFT JOIN OLAP_DIM_NETWORK n
           ON f.line_id = n.line_id
          AND f.route_id = n.route_id
          AND f.stop_id = n.stop_id
    GROUP BY
        COALESCE(n.line_name, f.line_id),
        COALESCE(n.route_id, f.route_id),
        COALESCE(n.stop_name, f.stop_id)
)
SELECT
    CASE
        WHEN GROUPING(line_name) = 1 THEN '{Total General}'
        ELSE line_name
    END AS line_name,
    CASE
        WHEN GROUPING(line_name) = 1 THEN ' '
        WHEN GROUPING(route_id) = 1 THEN CONCAT('subtotal linie ', line_name)
        ELSE route_id
    END AS route_id,
    CASE
        WHEN GROUPING(line_name) = 1 THEN ' '
        WHEN GROUPING(route_id) = 1 THEN ' '
        WHEN GROUPING(stop_name) = 1 THEN CONCAT('subtotal ruta ', route_id)
        ELSE stop_name
    END AS stop_name,
    SUM(validation_count) AS validation_count
FROM network_base
GROUP BY ROLLUP(line_name, route_id, stop_name);

-- Demo result
SELECT * FROM OLAP_VIEW_VALIDATIONS_NETWORK ORDER BY line_name, route_id, stop_name;

--------------------------------------------------------------------------------
-- 4.4 OLAP_VIEW_OPERATIONAL_EVENTS_TYPE
-- Clauze speciale: ROLLUP, GROUPING, AVG, SUM.
-- Analiza: evenimente operationale dupa sursa, subtip si impact. Fact operational events si dimensiunea event type
--------------------------------------------------------------------------------
DROP VIEW IF EXISTS OLAP_VIEW_OPERATIONAL_EVENTS_TYPE;

CREATE OR REPLACE VIEW OLAP_VIEW_OPERATIONAL_EVENTS_TYPE AS
SELECT
    CASE
        WHEN GROUPING(e.source_type) = 1 THEN '{Total General}'
        ELSE e.source_type
    END AS source_type,
    CASE
        WHEN GROUPING(e.source_type) = 1 THEN ' '
        WHEN GROUPING(e.event_subtype) = 1 THEN CONCAT('subtotal sursa ', e.source_type)
        ELSE e.event_subtype
    END AS event_subtype,
    CASE
        WHEN GROUPING(e.source_type) = 1 THEN ' '
        WHEN GROUPING(e.event_subtype) = 1 THEN ' '
        WHEN GROUPING(e.impact_label) = 1 THEN CONCAT('subtotal tip ', e.event_subtype)
        ELSE e.impact_label
    END AS impact_label,
    SUM(COALESCE(f.event_count, 0)) AS total_events,
    ROUND(AVG(f.speed), 2) AS avg_speed,
    ROUND(AVG(f.occupancy_estimate), 2) AS avg_occupancy_estimate,
    ROUND(AVG(f.traffic_multiplier), 2) AS avg_traffic_multiplier
FROM OLAP_DIM_EVENT_TYPE e
INNER JOIN OLAP_FACTS_OPERATIONAL_EVENTS f
        ON e.source_type = f.source_type
       AND e.event_subtype = f.event_subtype
       AND e.impact_label = f.impact_label
       AND e.status_label = f.status_label
GROUP BY ROLLUP(e.source_type, e.event_subtype, e.impact_label);

-- Demo result
SELECT * FROM OLAP_VIEW_OPERATIONAL_EVENTS_TYPE ORDER BY source_type, event_subtype, impact_label;

--------------------------------------------------------------------------------
-- 5. Window functions
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- 5.1 OLAP_VIEW_DAILY_SALES_WINDOW
-- Clauze speciale: SUM() OVER, AVG() OVER, LAG(), ROWS BETWEEN.
-- Analiza: evoluția veniturilor zilnice
--------------------------------------------------------------------------------
DROP VIEW IF EXISTS OLAP_VIEW_DAILY_SALES_WINDOW;

CREATE OR REPLACE VIEW OLAP_VIEW_DAILY_SALES_WINDOW AS
WITH daily_sales AS (
    SELECT
        full_date,
        SUM(total_amount_paid) AS daily_revenue,
        SUM(sales_count) AS daily_sales_count
    FROM OLAP_FACTS_TICKET_SALES
    GROUP BY full_date
)
SELECT
    full_date,
    daily_revenue,
    daily_sales_count,
    SUM(daily_revenue) OVER (
        ORDER BY full_date
        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
    ) AS running_revenue,
    ROUND(AVG(daily_revenue) OVER (
        ORDER BY full_date
        ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
    ), 2) AS moving_avg_7_days,
    LAG(daily_revenue) OVER (ORDER BY full_date) AS previous_day_revenue,
    daily_revenue - LAG(daily_revenue) OVER (ORDER BY full_date) AS revenue_change_vs_previous_day
FROM daily_sales;

-- Demo result
SELECT * FROM OLAP_VIEW_DAILY_SALES_WINDOW ORDER BY full_date;

--------------------------------------------------------------------------------
-- 5.2 OLAP_VIEW_LINE_VALIDATION_RANKING
-- Clauze speciale: RANK(), DENSE_RANK(), PERCENT_RANK(), PARTITION BY.
-- Analiza: clasamentul liniilor dupa numarul de validari pe zi.
--------------------------------------------------------------------------------
DROP VIEW IF EXISTS OLAP_VIEW_LINE_VALIDATION_RANKING;

CREATE OR REPLACE VIEW OLAP_VIEW_LINE_VALIDATION_RANKING AS
WITH line_validations AS (
    SELECT
        f.full_date,
        f.line_id,
        COALESCE(l.line_name, f.line_id) AS line_name,
        SUM(f.validation_count) AS validation_count
    FROM OLAP_FACTS_VALIDATIONS_NETWORK f
    LEFT JOIN DS3_LINES_V l ON f.line_id = l.line_id
    GROUP BY f.full_date, f.line_id, COALESCE(l.line_name, f.line_id)
)
SELECT
    full_date,
    line_id,
    line_name,
    validation_count,
    RANK() OVER (
        PARTITION BY full_date
        ORDER BY validation_count DESC
    ) AS rank_by_day,
    DENSE_RANK() OVER (
        PARTITION BY full_date
        ORDER BY validation_count DESC
    ) AS dense_rank_by_day,
    PERCENT_RANK() OVER (
        PARTITION BY full_date
        ORDER BY validation_count DESC
    ) AS percent_rank_by_day
FROM line_validations;

-- Demo result
SELECT * FROM OLAP_VIEW_LINE_VALIDATION_RANKING ORDER BY full_date, rank_by_day, line_id;

--------------------------------------------------------------------------------
-- 5.3 OLAP_VIEW_TELEMETRY_WINDOW
-- Clauze speciale: LAG(), AVG() OVER, PARTITION BY.
-- Analiza: evolutia ocuparii si vitezei pe vehicul, cu date din MongoDB si PostgreSQL.
--------------------------------------------------------------------------------
DROP VIEW IF EXISTS OLAP_VIEW_TELEMETRY_WINDOW;

CREATE OR REPLACE VIEW OLAP_VIEW_TELEMETRY_WINDOW AS
SELECT
    ts,
    vehicle_id,
    line_id,
    route_id,
    stop_id,
    speed,
    occupancy_estimate,
    LAG(occupancy_estimate) OVER (
        PARTITION BY vehicle_id
        ORDER BY ts
    ) AS previous_occupancy,
    occupancy_estimate - LAG(occupancy_estimate) OVER (
        PARTITION BY vehicle_id
        ORDER BY ts
    ) AS occupancy_change,
    ROUND(AVG(speed) OVER (
        PARTITION BY vehicle_id
        ORDER BY ts
        ROWS BETWEEN 2 PRECEDING AND CURRENT ROW
    ), 2) AS moving_avg_speed_3_logs
FROM TELEMETRY_LOGS_VIEW;

SELECT *
FROM OLAP_VIEW_TELEMETRY_WINDOW
ORDER BY vehicle_id, ts;

--------------------------------------------------------------------------------
-- 6. PIVOT
-- Clauze speciale: PIVOT, SUM, IN cu aliasuri.
-- Analiza: venit lunar pe tipuri de bilete afisate ca coloane.
--------------------------------------------------------------------------------
SELECT *
FROM (
    SELECT
        MONTH(f.full_date) AS sale_month,
        t.ticket_name,
        f.total_amount_paid
    FROM OLAP_FACTS_TICKET_SALES f
    INNER JOIN OLAP_DIM_TICKET t ON f.ticket_type_id = t.ticket_type_id
) src
PIVOT (
    SUM(total_amount_paid)
    FOR ticket_name IN (
        'Bilet 1 Calatorie' AS bilet_1_calatorie,
        'Abonament 30 zile' AS abonament_30_zile,
        'Bilet 1 zi' AS bilet_1_zi
    )
)
ORDER BY sale_month;

--------------------------------------------------------------------------------
-- 7. Interogari finale de verificare
--------------------------------------------------------------------------------
-- SELECT * FROM OLAP_VIEW_SALES_CALENDAR ORDER BY year_no, month_no, day_no;
-- SELECT * FROM OLAP_VIEW_SALES_TICKET_AREA_CUBE ORDER BY ticket_name, zone;
-- SELECT * FROM OLAP_VIEW_VALIDATIONS_NETWORK ORDER BY line_name, route_id, stop_name;
-- SELECT * FROM OLAP_VIEW_OPERATIONAL_EVENTS_TYPE ORDER BY source_type, event_subtype, impact_label;
-- SELECT * FROM OLAP_VIEW_DAILY_SALES_WINDOW ORDER BY full_date;
-- SELECT * FROM OLAP_VIEW_LINE_VALIDATION_RANKING ORDER BY full_date, rank_by_day, line_id;
-- SELECT * FROM OLAP_VIEW_TELEMETRY_WINDOW ORDER BY vehicle_id, ts;
