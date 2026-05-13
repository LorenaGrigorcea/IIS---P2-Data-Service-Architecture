package org.datasource.mongodb.views.transportoperations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@JsonIgnoreProperties({"_id"})
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class TelemetryLogView implements Serializable {
    private String telemetry_id;
    private String ts;
    private String vehicle_id;
    private String line_id;
    private String route_id;
    private String stop_id;
    private Double speed;
    private Integer occupancy_estimate;
}