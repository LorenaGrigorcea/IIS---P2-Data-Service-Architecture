package org.j4di.analytical.views.network;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(OLAP_VIEW_VALIDATIONS_NETWORK_ID.class)
@Table(name = "OLAP_VIEW_VALIDATIONS_NETWORK")
public class OLAP_VIEW_VALIDATIONS_NETWORK {

    @Id
    private String line_name;

    @Id
    private String route_id;

    @Id
    private String stop_name;

    private Long validation_count;
}