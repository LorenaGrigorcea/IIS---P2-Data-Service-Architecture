package org.j4di.analytical.views.telemetry;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Entity
@Immutable
@IdClass(OLAP_VIEW_TELEMETRY_WINDOW_ID.class)
@Table(name = "OLAP_VIEW_TELEMETRY_WINDOW")
public class OLAP_VIEW_TELEMETRY_WINDOW {

    @Id
    private Timestamp ts;

    @Id
    private String vehicle_id;

    @Id
    private String line_id;

    @Id
    private String route_id;

    @Id
    private String stop_id;

    private BigDecimal speed;
    private Integer occupancy_estimate;
    private Integer previous_occupancy;
    private Integer occupancy_change;
    private Double moving_avg_speed_3_logs;
}