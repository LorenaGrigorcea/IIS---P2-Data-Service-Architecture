package org.j4di.analytical.views.daily;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.sql.Date;

@Getter
@Entity
@Immutable
@Table(name = "OLAP_VIEW_DAILY_SALES_WINDOW")
public class OLAP_VIEW_DAILY_SALES_WINDOW {

    @Id
    private Date full_date;

    private Double daily_revenue;
    private Long daily_sales_count;
    private Double running_revenue;
    private Double moving_avg_7_days;
    private Double previous_day_revenue;
    private Double revenue_change_vs_previous_day;
}