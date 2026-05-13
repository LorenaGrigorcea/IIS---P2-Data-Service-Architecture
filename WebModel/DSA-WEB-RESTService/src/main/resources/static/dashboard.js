const api = {
    dailySales: "OLAP/daily-sales-window",
    lineRanking: "OLAP/line-validation-ranking",
    eventsType: "OLAP/operational-events-type",
    salesCube: "OLAP/sales-ticket-area-cube",
    validationsNetwork: "OLAP/validations-network",
    telemetry: "OLAP/telemetry-window"
};

let dataStore = {
    dailySales: [],
    lineRanking: [],
    eventsType: [],
    salesCube: [],
    validationsNetwork: [],
    telemetry: []
};

const charts = {
    dailySales: null,
    lineRanking: null,
    events: null,
    telemetry: null,
    salesCube: null
};

function numberValue(value) {
    if (value === null || value === undefined || value === "") return 0;
    const parsed = Number(value);
    return Number.isFinite(parsed) ? parsed : 0;
}

function formatNumber(value, decimals = 2) {
    return numberValue(value).toLocaleString("en-US", {
        minimumFractionDigits: decimals,
        maximumFractionDigits: decimals
    });
}

function isTotalLike(value) {
    if (!value) return false;
    const text = String(value).toLowerCase();
    return text.includes("total") || text.includes("subtotal");
}

function setStatus(text) {
    document.getElementById("status").textContent = text;
}

function showError(message) {
    const box = document.getElementById("errorBox");
    box.style.display = "block";
    box.textContent = message;
}

function clearError() {
    const box = document.getElementById("errorBox");
    box.style.display = "none";
    box.textContent = "";
}

async function fetchJson(url) {
    const response = await fetch(url);
    if (!response.ok) {
        throw new Error(`${url} returned HTTP ${response.status}`);
    }
    return response.json();
}

async function loadDashboard() {
    clearError();
    setStatus("Loading data from REST endpoints...");

    try {
        const [
            dailySales,
            lineRanking,
            eventsType,
            salesCube,
            validationsNetwork,
            telemetry
        ] = await Promise.all([
            fetchJson(api.dailySales),
            fetchJson(api.lineRanking),
            fetchJson(api.eventsType),
            fetchJson(api.salesCube),
            fetchJson(api.validationsNetwork),
            fetchJson(api.telemetry)
        ]);

        dataStore = {
            dailySales,
            lineRanking,
            eventsType,
            salesCube,
            validationsNetwork,
            telemetry
        };

        renderKpis();
        prepareRankingDates();
        renderActiveSection();

        setStatus(`Loaded ${new Date().toLocaleTimeString()}`);
    } catch (error) {
        console.error(error);
        showError(
            "Dashboard data could not be loaded. Check if all REST services, SparkSQL and DSA-WEB-RESTService are running. Details: "
            + error.message
        );
        setStatus("Loading failed");
    }
}

function renderKpis() {
    const totalRevenue = dataStore.dailySales
        .reduce((sum, row) => sum + numberValue(row.daily_revenue), 0);

    const totalValidationsFromNetwork = dataStore.validationsNetwork
        .filter(row => isTotalLike(row.line_name))
        .reduce((sum, row) => sum + numberValue(row.validation_count), 0);

    const totalValidations = totalValidationsFromNetwork ||
        dataStore.lineRanking.reduce((sum, row) => sum + numberValue(row.validation_count), 0);

    const totalEventsRow = dataStore.eventsType
        .find(row => String(row.source_type).includes("Total"));

    const totalEvents = totalEventsRow
        ? numberValue(totalEventsRow.total_events)
        : dataStore.eventsType.reduce((sum, row) => sum + numberValue(row.total_events), 0);

    const speeds = dataStore.telemetry
        .map(row => numberValue(row.speed))
        .filter(value => value > 0);

    const avgSpeed = speeds.length
        ? speeds.reduce((sum, value) => sum + value, 0) / speeds.length
        : 0;

    document.getElementById("kpiRevenue").textContent = `${formatNumber(totalRevenue)} RON`;
    document.getElementById("kpiValidations").textContent = formatNumber(totalValidations, 0);
    document.getElementById("kpiEvents").textContent = formatNumber(totalEvents, 0);
    document.getElementById("kpiSpeed").textContent = `${formatNumber(avgSpeed)} km/h`;
}

function chartBaseOption() {
    return {
        backgroundColor: "transparent",
        textStyle: {
            color: "#e5e7eb"
        },
        tooltip: {
            trigger: "axis",
            backgroundColor: "rgba(15, 23, 42, 0.94)",
            borderColor: "rgba(148, 163, 184, 0.35)",
            textStyle: {
                color: "#f8fafc"
            }
        },
        legend: {
            top: 0,
            textStyle: {
                color: "#cbd5e1"
            }
        },
        grid: {
            left: 48,
            right: 26,
            top: 58,
            bottom: 58
        },
        toolbox: {
            right: 8,
            feature: {
                saveAsImage: {},
                restore: {}
            },
            iconStyle: {
                borderColor: "#cbd5e1"
            }
        }
    };
}

function renderDailySalesChart() {
    const element = document.getElementById("dailySalesChart");
    if (!element) return;

    const rows = [...dataStore.dailySales].sort((a, b) =>
        String(a.full_date).localeCompare(String(b.full_date))
    );

    charts.dailySales = charts.dailySales || echarts.init(element);

    charts.dailySales.setOption({
        ...chartBaseOption(),
        xAxis: {
            type: "category",
            data: rows.map(row => row.full_date),
            axisLabel: { color: "#cbd5e1" },
            axisLine: { lineStyle: { color: "#334155" } }
        },
        yAxis: [
            {
                type: "value",
                name: "Revenue",
                axisLabel: { color: "#cbd5e1" },
                splitLine: { lineStyle: { color: "rgba(148, 163, 184, 0.14)" } }
            },
            {
                type: "value",
                name: "Running total",
                axisLabel: { color: "#cbd5e1" },
                splitLine: { show: false }
            }
        ],
        dataZoom: [
            { type: "inside" },
            { type: "slider", bottom: 12, textStyle: { color: "#cbd5e1" } }
        ],
        series: [
            {
                name: "Daily revenue",
                type: "line",
                smooth: true,
                symbolSize: 8,
                data: rows.map(row => numberValue(row.daily_revenue)),
                areaStyle: { opacity: 0.16 },
                emphasis: { focus: "series" }
            },
            {
                name: "7-day moving average",
                type: "line",
                smooth: true,
                symbolSize: 7,
                data: rows.map(row => numberValue(row.moving_avg_7_days)),
                emphasis: { focus: "series" }
            },
            {
                name: "Running revenue",
                type: "line",
                smooth: true,
                yAxisIndex: 1,
                symbolSize: 6,
                data: rows.map(row => numberValue(row.running_revenue)),
                emphasis: { focus: "series" }
            }
        ]
    });
}

function prepareRankingDates() {
    const select = document.getElementById("rankingDateSelect");
    if (!select) return;

    const dates = [...new Set(dataStore.lineRanking.map(row => row.full_date))]
        .sort();

    select.innerHTML = "";

    dates.forEach(date => {
        const option = document.createElement("option");
        option.value = date;
        option.textContent = date;
        select.appendChild(option);
    });

    if (dates.length) {
        select.value = dates[dates.length - 1];
    }
}

function renderLineRankingChart() {
    const element = document.getElementById("lineRankingChart");
    const select = document.getElementById("rankingDateSelect");

    if (!element || !select) return;

    const selectedDate = select.value;

    const rows = dataStore.lineRanking
        .filter(row => row.full_date === selectedDate)
        .sort((a, b) => numberValue(a.rank_by_day) - numberValue(b.rank_by_day))
        .slice(0, 10);

    charts.lineRanking = charts.lineRanking || echarts.init(element);

    charts.lineRanking.setOption({
        ...chartBaseOption(),
        tooltip: {
            trigger: "axis",
            axisPointer: { type: "shadow" },
            backgroundColor: "rgba(15, 23, 42, 0.94)",
            borderColor: "rgba(148, 163, 184, 0.35)",
            textStyle: { color: "#f8fafc" }
        },
        xAxis: {
            type: "value",
            axisLabel: { color: "#cbd5e1" },
            splitLine: { lineStyle: { color: "rgba(148, 163, 184, 0.14)" } }
        },
        yAxis: {
            type: "category",
            data: rows.map(row => `${row.rank_by_day}. ${row.line_name}`),
            axisLabel: { color: "#cbd5e1" },
            axisLine: { lineStyle: { color: "#334155" } }
        },
        grid: {
            left: 110,
            right: 20,
            top: 28,
            bottom: 30
        },
        series: [
            {
                name: "Validations",
                type: "bar",
                data: rows.map(row => numberValue(row.validation_count)),
                barWidth: 18,
                itemStyle: {
                    borderRadius: [0, 10, 10, 0]
                },
                label: {
                    show: true,
                    position: "right",
                    color: "#e5e7eb"
                }
            }
        ]
    });
}

function renderEventsChart() {
    const element = document.getElementById("eventsChart");
    if (!element) return;

    const grouped = {};

    dataStore.eventsType
        .filter(row => !isTotalLike(row.source_type))
        .forEach(row => {
            const key = row.source_type || "UNKNOWN";
            grouped[key] = (grouped[key] || 0) + numberValue(row.total_events);
        });

    const pieData = Object.entries(grouped)
        .map(([name, value]) => ({ name, value }));

    charts.events = charts.events || echarts.init(element);

    charts.events.setOption({
        backgroundColor: "transparent",
        tooltip: {
            trigger: "item",
            backgroundColor: "rgba(15, 23, 42, 0.94)",
            borderColor: "rgba(148, 163, 184, 0.35)",
            textStyle: { color: "#f8fafc" },
            formatter: "{b}<br/>Events: {c}<br/>Share: {d}%"
        },
        legend: {
            bottom: 0,
            textStyle: { color: "#cbd5e1" }
        },
        toolbox: {
            right: 8,
            feature: {
                saveAsImage: {},
                restore: {}
            },
            iconStyle: {
                borderColor: "#cbd5e1"
            }
        },
        series: [
            {
                name: "Operational events",
                type: "pie",
                radius: ["42%", "72%"],
                center: ["50%", "45%"],
                avoidLabelOverlap: true,
                itemStyle: {
                    borderRadius: 10,
                    borderColor: "#0f172a",
                    borderWidth: 3
                },
                label: {
                    color: "#e5e7eb",
                    formatter: "{b}: {c}"
                },
                emphasis: {
                    label: {
                        show: true,
                        fontSize: 16,
                        fontWeight: "bold"
                    }
                },
                data: pieData
            }
        ]
    });
}

function renderTelemetryChart() {
    const element = document.getElementById("telemetryChart");
    if (!element) return;

    const rows = [...dataStore.telemetry].sort((a, b) =>
        new Date(a.ts).getTime() - new Date(b.ts).getTime()
    );

    charts.telemetry = charts.telemetry || echarts.init(element);

    charts.telemetry.setOption({
        ...chartBaseOption(),
        xAxis: {
            type: "category",
            data: rows.map(row => row.ts),
            axisLabel: {
                color: "#cbd5e1",
                formatter: value => String(value).substring(5, 16)
            },
            axisLine: { lineStyle: { color: "#334155" } }
        },
        yAxis: [
            {
                type: "value",
                name: "Speed",
                axisLabel: { color: "#cbd5e1" },
                splitLine: { lineStyle: { color: "rgba(148, 163, 184, 0.14)" } }
            },
            {
                type: "value",
                name: "Occupancy",
                axisLabel: { color: "#cbd5e1" },
                splitLine: { show: false }
            }
        ],
        dataZoom: [
            { type: "inside" },
            { type: "slider", bottom: 12, textStyle: { color: "#cbd5e1" } }
        ],
        series: [
            {
                name: "Speed",
                type: "line",
                smooth: true,
                symbolSize: 7,
                data: rows.map(row => numberValue(row.speed)),
                emphasis: { focus: "series" }
            },
            {
                name: "Moving average speed",
                type: "line",
                smooth: true,
                symbolSize: 6,
                data: rows.map(row => numberValue(row.moving_avg_speed_3_logs)),
                emphasis: { focus: "series" }
            },
            {
                name: "Occupancy",
                type: "bar",
                yAxisIndex: 1,
                data: rows.map(row => numberValue(row.occupancy_estimate)),
                barWidth: 10,
                opacity: 0.34
            }
        ]
    });
}

function renderSalesCubeChart() {
    const element = document.getElementById("salesCubeChart");
    if (!element) return;

    const rows = dataStore.salesCube
        .filter(row => !isTotalLike(row.ticket_name) && !isTotalLike(row.zone))
        .sort((a, b) => numberValue(b.total_amount_paid) - numberValue(a.total_amount_paid))
        .slice(0, 10);

    charts.salesCube = charts.salesCube || echarts.init(element);

    charts.salesCube.setOption({
        ...chartBaseOption(),
        tooltip: {
            trigger: "axis",
            axisPointer: { type: "shadow" },
            backgroundColor: "rgba(15, 23, 42, 0.94)",
            borderColor: "rgba(148, 163, 184, 0.35)",
            textStyle: { color: "#f8fafc" }
        },
        xAxis: {
            type: "category",
            data: rows.map(row => `${row.ticket_name} | ${row.zone}`),
            axisLabel: {
                color: "#cbd5e1",
                rotate: 30,
                interval: 0
            },
            axisLine: { lineStyle: { color: "#334155" } }
        },
        yAxis: {
            type: "value",
            axisLabel: { color: "#cbd5e1" },
            splitLine: { lineStyle: { color: "rgba(148, 163, 184, 0.14)" } }
        },
        grid: {
            left: 48,
            right: 20,
            top: 32,
            bottom: 104
        },
        dataZoom: [
            { type: "inside" },
            { type: "slider", bottom: 12, textStyle: { color: "#cbd5e1" } }
        ],
        series: [
            {
                name: "Paid amount",
                type: "bar",
                data: rows.map(row => numberValue(row.total_amount_paid)),
                itemStyle: {
                    borderRadius: [10, 10, 0, 0]
                },
                label: {
                    show: true,
                    position: "top",
                    color: "#e5e7eb"
                }
            }
        ]
    });
}

function renderNetworkTable() {
    const body = document.getElementById("networkTableBody");
    if (!body) return;

    const rows = dataStore.validationsNetwork
        .sort((a, b) => numberValue(b.validation_count) - numberValue(a.validation_count))
        .slice(0, 20);

    body.innerHTML = rows.map(row => `
        <tr>
            <td>${row.line_name ?? ""}</td>
            <td>${row.route_id ?? ""}</td>
            <td>${row.stop_name ?? ""}</td>
            <td>${numberValue(row.validation_count)}</td>
        </tr>
    `).join("");
}

function showSection(sectionId) {
    document.querySelectorAll(".section").forEach(section => {
        section.classList.remove("active");
    });

    document.querySelectorAll(".nav-item").forEach(button => {
        button.classList.toggle("active", button.dataset.section === sectionId);
    });

    const selectedSection = document.getElementById(sectionId);
    if (selectedSection) {
        selectedSection.classList.add("active");
    }

    setTimeout(() => {
        renderActiveSection();
        resizeCharts();
    }, 80);
}

function renderActiveSection() {
    const activeSection = document.querySelector(".section.active");
    if (!activeSection) return;

    const id = activeSection.id;

    if (id === "sales") {
        renderDailySalesChart();
        renderSalesCubeChart();
    }

    if (id === "network") {
        renderLineRankingChart();
        renderNetworkTable();
    }

    if (id === "operations") {
        renderEventsChart();
    }

    if (id === "telemetry") {
        renderTelemetryChart();
    }

    resizeCharts();
}

function resizeCharts() {
    Object.values(charts).forEach(chart => {
        if (chart) {
            chart.resize();
        }
    });
}

window.addEventListener("resize", resizeCharts);

loadDashboard();