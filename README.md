# P2: Data Service Architecture

## Case study
Public Transport System, urban public transport analysis for Iași.

## Architecture
The project follows the Java4DI architecture, using Spring Boot microservices for the Access Model, Spark SQL for integration and analytical processing, and a Web REST service for exposing analytical views.

## 1. Access Model

### DSA-SQL-JPAService
Source: Oracle ticketing database  
Main endpoint examples:
- /rest/ticketing/TicketSalesView
- /rest/ticketing/TicketTypesView
- /rest/ticketing/TicketValidationsView

### DSA-NoSQL-MongoDBService
Source: MongoDB operational documents  
Main endpoint examples:
- /rest/events/IncidentsView
- /rest/events/MaintenanceWindowsView
- /rest/events/TelemetryLogsView

### DSA-NoSQL-Neo4JService
Source: Neo4j transport network graph  
Main endpoint examples:
- /rest/network/StopsView
- /rest/network/LinesView
- /rest/network/RoutesView

## 2. Integration and Analytical Model

The DSA-SparkSQL-Service is started as a Spring Boot application and accessed from DBeaver using the Hive/Spark JDBC connection.

The SQL scripts from the `sql/` folder create Spark SQL views over the REST endpoints exposed by the Access Model services.

## 3. Analytical Views

The script `SparkSQL_OLAP.sql` creates the analytical/multidimensional views used for reporting:
- daily ticket revenue
- revenue by zone and customer category
- validations by line and route
- incidents by severity
- vehicle speed and occupancy indicators
- operational coverage indicators

## 4. Web Model

The DSA-WEB-RESTService exposes selected Spark SQL analytical views as REST endpoints.

## 5. Optional Web UI

The `web-ui/` folder contains a JavaScript-based interface with interactive charts for consuming the analytical REST endpoints.
