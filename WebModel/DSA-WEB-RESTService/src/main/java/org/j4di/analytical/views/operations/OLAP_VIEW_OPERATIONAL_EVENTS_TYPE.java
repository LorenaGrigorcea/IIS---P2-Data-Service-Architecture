package org.j4di.analytical.views.operations;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(OLAP_VIEW_OPERATIONAL_EVENTS_TYPE_ID.class)
@Table(name = "OLAP_VIEW_OPERATIONAL_EVENTS_TYPE")
public class OLAP_VIEW_OPERATIONAL_EVENTS_TYPE {

    @Id
    private String source_type;

    @Id
    private String event_subtype;

    @Id
    private String impact_label;

    private Long total_events;
    private Double avg_speed;
    private Double avg_occupancy_estimate;
    private Double avg_traffic_multiplier;
}