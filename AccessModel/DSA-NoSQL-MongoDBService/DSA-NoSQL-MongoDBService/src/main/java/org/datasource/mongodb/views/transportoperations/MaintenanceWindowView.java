package org.datasource.mongodb.views.transportoperations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties({"_id"})
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class MaintenanceWindowView implements Serializable {
    private String work_id;
    private String start_ts;
    private String end_ts;
    private String line_id;
    private String route_id;
    private List<String> affected_stops;
    private String work_type;
    private String impact_level;
}