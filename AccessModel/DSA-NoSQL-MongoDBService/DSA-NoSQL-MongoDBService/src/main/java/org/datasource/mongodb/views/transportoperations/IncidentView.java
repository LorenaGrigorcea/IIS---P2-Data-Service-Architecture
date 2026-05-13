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
public class IncidentView implements Serializable {
    private String incident_id;
    private String created_ts;
    private String category;
    private String severity;
    private String line_id;
    private String route_id;
    private String stop_id;
    private String description;
    private String status;
}