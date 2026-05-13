package org.datasource.mongodb.views.transportoperations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties({"_id"})
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class TransportOperationsView {
    private List<IncidentView> incidents;
    private List<MaintenanceWindowView> maintenance_windows;
    private List<SpecialEventView> special_events;
    private List<TelemetryLogView> telemetry_logs;
}