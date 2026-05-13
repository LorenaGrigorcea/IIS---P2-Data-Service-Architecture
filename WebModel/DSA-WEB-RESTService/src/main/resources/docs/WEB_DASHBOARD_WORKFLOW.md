Web Analytics Dashboard Workflow
1. Rolul interfeței grafice

După implementarea endpointurilor REST în DSA-WEB-RESTService, a fost creată o interfață grafică de tip dashboard pentru vizualizarea rezultatelor analitice. Dashboardul nu accesează direct SparkSQL și nu interoghează sursele de date inițiale. El consumă endpointurile REST expuse anterior și transformă răspunsurile JSON în grafice interactive.

Fluxul general este:

SparkSQL analytical view
→ REST endpoint
→ JSON response
→ JavaScript fetch()
→ ECharts visualization
2. Structura fișierelor

Interfața grafică a fost implementată ca pagină statică în proiectul DSA-WEB-RESTService.

Structura folosită:

DSA-WEB-RESTService
└── src
└── main
└── resources
└── static
├── dashboard.html
├── dashboard.css
└── dashboard.js

Pagina este accesibilă în browser la:

http://localhost:8096/DSA-WEB-RESTService/rest/dashboard.html
3. Fișierul dashboard.html

Fișierul dashboard.html definește structura interfeței:

meniu lateral
secțiune de Overview
secțiune Sales Analytics
secțiune Network Analytics
secțiune Operational Events
secțiune Telemetry Analytics
containerele pentru grafice
cardurile KPI
tabelul pentru validări pe rețea

În <head> sunt încărcate fișierele:

<link rel="stylesheet" href="dashboard.css">

<script src="https://cdn.jsdelivr.net/npm/echarts@5/dist/echarts.min.js"></script>
<script src="dashboard.js" defer></script>

Pentru rulare fără internet, biblioteca ECharts poate fi descărcată local și inclusă astfel:

<script src="echarts.min.js"></script>
4. Fișierul dashboard.css

Fișierul dashboard.css conține partea vizuală a interfeței:

tema dark
layout cu meniu lateral
carduri KPI
containere pentru grafice
stil pentru tabel
stil pentru butoane și secțiuni active
design responsive

Interfața este organizată cu un layout de tip:

sidebar + content area

Meniul lateral permite navigarea între secțiuni fără schimbarea paginii.

5. Fișierul dashboard.js

Fișierul dashboard.js conține logica aplicației web:

apelarea endpointurilor REST cu fetch()
stocarea datelor în dataStore
calcularea indicatorilor KPI
randarea graficelor ECharts
schimbarea secțiunilor din meniu
redimensionarea graficelor
afișarea erorilor dacă serviciile nu răspund

Endpointurile consumate sunt:

const api = {
dailySales: "OLAP/daily-sales-window",
lineRanking: "OLAP/line-validation-ranking",
eventsType: "OLAP/operational-events-type",
salesCube: "OLAP/sales-ticket-area-cube",
validationsNetwork: "OLAP/validations-network",
telemetry: "OLAP/telemetry-window"
};

Pentru încărcarea datelor se folosește:

fetchJson(api.dailySales)
fetchJson(api.lineRanking)
fetchJson(api.eventsType)
fetchJson(api.salesCube)
fetchJson(api.validationsNetwork)
fetchJson(api.telemetry)

Datele sunt încărcate asincron, iar după primirea răspunsurilor JSON se actualizează KPI-urile și graficele.

6. Secțiunile dashboardului

Dashboardul este împărțit în cinci secțiuni principale.

Overview

Această secțiune oferă o vedere generală asupra sistemului. Include patru carduri KPI:

Total revenue
Total validations
Operational events
Average telemetry speed

Tot aici sunt afișate endpointurile implementate și fluxul arhitectural:

SparkSQL View → JPA Entity → Repository → REST endpoint → Dashboard
Sales Analytics

Această secțiune folosește endpointurile:

/OLAP/daily-sales-window
/OLAP/sales-ticket-area-cube

Graficele afișate sunt:

Daily sales and moving average
Sales by ticket and area

Primul grafic evidențiază venitul zilnic, media mobilă și venitul cumulativ. Al doilea grafic prezintă combinațiile reprezentative între tipul de bilet și zona de vânzare.

Network Analytics

Această secțiune folosește endpointurile:

/OLAP/line-validation-ranking
/OLAP/validations-network

Elementele afișate sunt:

bar chart pentru clasamentul liniilor
tabel pentru validări pe linie, rută și stație

Pentru clasament a fost adăugat un selector de dată, astfel încât utilizatorul poate analiza validările pentru o anumită zi.

Operational Events

Această secțiune folosește endpointul:

/OLAP/operational-events-type

Graficul afișează distribuția evenimentelor operaționale pe surse:

INCIDENT
MAINTENANCE
SPECIAL_EVENT
TELEMETRY

Vizualizarea este realizată printr-un grafic de tip donut chart.

Telemetry Analytics

Această secțiune folosește endpointul:

/OLAP/telemetry-window

Graficul afișează:

viteza vehiculului
media mobilă a vitezei
estimarea ocupării

Acest grafic evidențiază folosirea funcțiilor window aplicate pe datele de telemetrie.

7. Interactivitate

Dashboardul include mai multe elemente interactive:

meniu lateral pentru navigare între secțiuni
tooltip la hover pe grafice
legende interactive
zoom pe graficele temporale
buton de refresh
opțiune save as image în graficele ECharts
selector de dată pentru clasamentul liniilor
tabel scrollabil pentru validări

Aceste elemente fac interfața potrivită pentru demonstrarea rezultatelor în timpul prezentării.

8. Ordinea corectă de rulare

Pentru ca dashboardul să funcționeze, trebuie pornite serviciile în ordinea corectă:

1. Serviciile Access Model:
    - Oracle
    - PostgreSQL
    - MongoDB
    - Neo4j

2. DSA-SparkSQL-Service

3. DSA-WEB-RESTService

4. dashboard.html în browser

Dacă un serviciu de acces nu este pornit, unele endpointuri pot da eroare, iar dashboardul nu va putea încărca toate datele.

9. Probleme întâlnite
   CSS și JavaScript nu se încărcau

Inițial, fișierele erau referite ca:

<link rel="stylesheet" href="css/dashboard.css">
<script src="js/dashboard.js"></script>

Dar fișierele erau plasate direct în folderul static.

Soluția corectă a fost:

<link rel="stylesheet" href="dashboard.css">
<script src="dashboard.js" defer></script>

În Spring Boot, folderul static devine rădăcina resurselor statice, deci fișierele pot fi accesate direct relativ la dashboard.html.

Cache în browser

După modificarea fișierelor statice, a fost necesar refresh forțat:

Ctrl + F5

pentru ca browserul să încarce varianta nouă a fișierelor CSS și JavaScript.

10. Concluzie

Interfața grafică a fost implementată ca un dashboard web static integrat în DSA-WEB-RESTService. Dashboardul consumă endpointurile REST construite peste view-urile analitice SparkSQL și le prezintă prin grafice interactive realizate cu Apache ECharts. Rezultatul este o interfață organizată pe secțiuni, potrivită pentru demonstrarea Web Model-ului și pentru evidențierea valorii analitice a datelor integrate.