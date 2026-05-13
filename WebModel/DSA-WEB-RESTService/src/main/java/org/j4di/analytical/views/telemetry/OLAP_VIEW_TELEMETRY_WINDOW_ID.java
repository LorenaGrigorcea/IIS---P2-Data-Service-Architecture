package org.j4di.analytical.views.telemetry;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OLAP_VIEW_TELEMETRY_WINDOW_ID implements Serializable {

    private Timestamp ts;
    private String vehicle_id;
    private String line_id;
    private String route_id;
    private String stop_id;
}