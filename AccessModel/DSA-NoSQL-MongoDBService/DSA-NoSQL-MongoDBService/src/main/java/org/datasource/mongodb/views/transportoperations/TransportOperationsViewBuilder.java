package org.datasource.mongodb.views.transportoperations;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.datasource.mongodb.MongoDataSourceConnector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransportOperationsViewBuilder {

    private List<IncidentView> incidentsViewList;
    private List<MaintenanceWindowView> maintenanceWindowsViewList;
    private List<SpecialEventView> specialEventsViewList;
    private List<TelemetryLogView> telemetryLogsViewList;

    private TransportOperationsView transportOperationsView;

    public List<IncidentView> getIncidentsViewList() {
        return incidentsViewList;
    }

    public List<MaintenanceWindowView> getMaintenanceWindowsViewList() {
        return maintenanceWindowsViewList;
    }

    public List<SpecialEventView> getSpecialEventsViewList() {
        return specialEventsViewList;
    }

    public List<TelemetryLogView> getTelemetryLogsViewList() {
        return telemetryLogsViewList;
    }

    public TransportOperationsView getTransportOperationsView() {
        return transportOperationsView;
    }

    private MongoDataSourceConnector dataSourceConnector;

    public TransportOperationsViewBuilder(MongoDataSourceConnector dataSourceConnector) {
        this.dataSourceConnector = dataSourceConnector;
    }

    public TransportOperationsViewBuilder build() throws Exception {
        return this.select().map();
    }

    private TransportOperationsViewBuilder select() throws Exception {
        MongoDatabase db = dataSourceConnector.getMongoDatabase();

        MongoCollection<IncidentView> incidentsCollection =
                db.getCollection("incidents", IncidentView.class);

        MongoCollection<MaintenanceWindowView> maintenanceCollection =
                db.getCollection("maintenance_windows", MaintenanceWindowView.class);

        MongoCollection<SpecialEventView> specialEventsCollection =
                db.getCollection("special_events", SpecialEventView.class);

        MongoCollection<TelemetryLogView> telemetryCollection =
                db.getCollection("telemetry_logs", TelemetryLogView.class);

        this.incidentsViewList = incidentsCollection.find().into(new ArrayList<>());
        this.maintenanceWindowsViewList = maintenanceCollection.find().into(new ArrayList<>());
        this.specialEventsViewList = specialEventsCollection.find().into(new ArrayList<>());
        this.telemetryLogsViewList = telemetryCollection.find().into(new ArrayList<>());

        return this;
    }

    private TransportOperationsViewBuilder map() {
        this.transportOperationsView = new TransportOperationsView(
                incidentsViewList,
                maintenanceWindowsViewList,
                specialEventsViewList,
                telemetryLogsViewList
        );
        return this;
    }
}