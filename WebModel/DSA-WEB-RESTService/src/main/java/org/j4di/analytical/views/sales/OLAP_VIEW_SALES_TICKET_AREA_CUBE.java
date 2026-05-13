package org.j4di.analytical.views.sales;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(OLAP_VIEW_SALES_TICKET_AREA_CUBE_ID.class)
@Table(name = "OLAP_VIEW_SALES_TICKET_AREA_CUBE")
public class OLAP_VIEW_SALES_TICKET_AREA_CUBE {

    @Id
    private String ticket_name;

    @Id
    private String zone;

    private Long sales_count;
    private Double total_amount_paid;
    private Double avg_amount_per_sale;
}