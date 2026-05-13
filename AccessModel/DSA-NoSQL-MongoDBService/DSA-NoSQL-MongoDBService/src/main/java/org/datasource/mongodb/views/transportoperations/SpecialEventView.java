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
public class SpecialEventView implements Serializable {
    private String event_id;
    private String event_date;
    private String area;
    private String event_type;
    private Double expected_traffic_multiplier;
}