REST Endpoints Workflow, Web Model
1. Rolul părții Web Model

În această etapă am creat un serviciu Spring Boot separat, DSA-WEB-RESTService, care expune prin endpointuri REST view-urile analitice deja construite în SparkSQL. Serviciul nu mai accesează direct sursele inițiale, Oracle, PostgreSQL, MongoDB sau Neo4j, ci citește rezultatele analitice din SparkSQL prin Hive JDBC.

Fluxul general este:

SparkSQL analytical view
→ Java @Entity
→ Spring Data JpaRepository
→ RESTViewService endpoint
→ JSON response in browser/Postman

Această abordare urmează modelul din curs, unde Web Model-ul este descris ca un Spring Boot REST Service conectat la SparkSQL-Hive Server, folosind clase @Entity, repository-uri JPA și metode în RESTViewService pentru expunerea view-urilor analitice.

2. Configurarea proiectului

Proiectul folosit:

DSA-WEB-RESTService

Fișier principal:

src/main/java/org/j4di/SpringBootWEBService.java

Fișier de configurare:

src/main/resources/application.properties

Configurarea importantă:

server.port=8096
server.servlet.context-path=/DSA-WEB-RESTService/rest

spring.datasource.url=jdbc:hive2://localhost:10001
spring.datasource.driver-class-name=org.apache.hive.jdbc.HiveDriver

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl

Observație: portul 10001 trebuie să fie același cu portul folosit de conexiunea SparkSQL din DBeaver. Inițial serviciul încerca să se conecteze la 10000, dar SparkSQL rula pe 10001, de aceea endpointurile dădeau eroare Connection refused.

3. Ordinea corectă de pornire

Pentru ca endpointurile Web să funcționeze, serviciile trebuie pornite în ordine:

1. Serviciile Access Model:
    - DSA_SQL_JPAService, Oracle ticketing
    - DSA_SQL_JPAService_PostgreSQL, operations
    - DSA-NoSQL-MongoDBService
    - DSA-NoSQL-Neo4JService

2. DSA-SparkSQL-Service

3. Scripturile SparkSQL pentru source views, canonical views, dimensions, facts și analytical views

4. DSA-WEB-RESTService

Dacă un endpoint Web depinde de un view SparkSQL care citește live dintr-un REST service, acel REST service trebuie să fie pornit. De exemplu, OLAP_VIEW_LINE_VALIDATION_RANKING depinde de date din Neo4j, deci DSA-NoSQL-Neo4JService trebuie să funcționeze înainte de testarea endpointului.

4. Verificarea view-urilor înainte de implementarea Java

Pentru fiecare endpoint, am verificat întâi în DBeaver că view-ul SparkSQL funcționează.

Exemplu:

SELECT *
FROM OLAP_VIEW_DAILY_SALES_WINDOW
LIMIT 10;

Pentru structura coloanelor:

DESCRIBE OLAP_VIEW_DAILY_SALES_WINDOW;

Această verificare este importantă pentru că Java trebuie să respecte exact numele și tipurile coloanelor din SparkSQL.

5. Modelul de implementare pentru fiecare endpoint

Pentru fiecare view analitic s-au creat trei componente:

1. Clasă @Entity
2. Repository JPA
3. Metodă endpoint în RESTViewService

Exemplu pentru OLAP_VIEW_DAILY_SALES_WINDOW:

@Getter
@Entity
@Immutable
@Table(name = "OLAP_VIEW_DAILY_SALES_WINDOW")
public class OLAP_VIEW_DAILY_SALES_WINDOW {

    @Id
    private Date full_date;

    private Double daily_revenue;
    private Long daily_sales_count;
    private Double running_revenue;
    private Double moving_avg_7_days;
    private Double previous_day_revenue;
    private Double revenue_change_vs_previous_day;
}

Repository:

public interface OLAP_VIEW_DAILY_SALES_WINDOW_Repository
extends JpaRepository<OLAP_VIEW_DAILY_SALES_WINDOW, Date> {

    @Query("SELECT v FROM OLAP_VIEW_DAILY_SALES_WINDOW v")
    List<OLAP_VIEW_DAILY_SALES_WINDOW> getDailySalesWindow();
}

Endpoint:

@RequestMapping(value = "/daily-sales-window", method = RequestMethod.GET,
produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
public List<OLAP_VIEW_DAILY_SALES_WINDOW> getDailySalesWindow() {
logger.info(">>>> Get OLAP_VIEW_DAILY_SALES_WINDOW");
return dailySalesWindowRepository.getDailySalesWindow();
}
6. Alegerea cheilor @Id

JPA cere ca fiecare @Entity să aibă un identificator. Pentru view-urile analitice am ales identificatori naturali sau compuși, fără să creăm wrapper views.

Exemple:

OLAP_VIEW_DAILY_SALES_WINDOW
→ @Id full_date

OLAP_VIEW_LINE_VALIDATION_RANKING
→ @IdClass(full_date + line_id)

OLAP_VIEW_OPERATIONAL_EVENTS_TYPE
→ @IdClass(source_type + event_subtype + impact_label)

OLAP_VIEW_SALES_TICKET_AREA_CUBE
→ @IdClass(ticket_name + zone)

OLAP_VIEW_VALIDATIONS_NETWORK
→ @IdClass(line_name + route_id + stop_name)

OLAP_VIEW_TELEMETRY_WINDOW
→ @IdClass(ts + vehicle_id + line_id + route_id + stop_id)

Am discutat și varianta cu wrapper view și ROW_NUMBER(), dar nu am folosit-o în implementarea finală pentru aceste endpointuri, deoarece view-urile aveau suficiente coloane pentru identificatori naturali sau compuși.

7. Endpointurile implementate

Au fost implementate și testate 6 endpointuri REST:

http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/daily-sales-window
http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/line-validation-ranking
http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/operational-events-type
http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/sales-ticket-area-cube
http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/validations-network
http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/telemetry-window

Endpoint de test:

http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/ping
8. Rolul fiecărui endpoint
   daily-sales-window
   → expune analiza vânzărilor zilnice, total cumulativ, medie mobilă și variație față de ziua anterioară.

line-validation-ranking
→ expune clasamentul liniilor de transport după numărul de validări pe zi.

operational-events-type
→ expune evenimente operaționale grupate pe sursă, tip și nivel de impact.

sales-ticket-area-cube
→ expune analiza multidimensională a vânzărilor pe tip de bilet și zonă.

validations-network
→ expune validările agregate pe linie, rută și stație.

telemetry-window
→ expune date de telemetrie cu valori anterioare, variații și medie mobilă a vitezei.
9. Probleme întâlnite și rezolvări
   Command line is too long

La pornirea SpringBootWEBService, IntelliJ a afișat eroarea:

Command line is too long

Rezolvare:

Edit Configurations
→ SpringBootWEBService
→ Modify options
→ Shorten command line
→ JAR manifest sau classpath file
Connection refused pe SparkSQL

Eroarea:

Could not open client transport with JDBC Uri: jdbc:hive2://localhost:10000
Connection refused

Cauză: DSA-WEB-RESTService încerca să se conecteze la portul 10000, dar SparkSQL rula pe 10001.

Rezolvare:

spring.datasource.url=jdbc:hive2://localhost:10001
View SparkSQL găsit, dar nu se executa

Pentru OLAP_VIEW_LINE_VALIDATION_RANKING, eroarea venea de la DS3_LINES_V. Problema era la partea Neo4j. După pornirea serviciului Neo4j și verificarea view-ului DS3_LINES_V, endpointul a funcționat.

10. Concluzie

Partea de REST endpoints a fost realizată prin adaptarea proiectului DSA-WEB-RESTService la view-urile analitice ale studiului de caz privind transportul public. Pentru fiecare view reprezentativ s-a creat o clasă @Entity, un repository JPA și o metodă REST în RESTViewService. Rezultatul este un Web Model funcțional, care expune rezultatele SparkSQL sub formă de JSON și poate fi consumat ulterior de o interfață web sau de un client extern.