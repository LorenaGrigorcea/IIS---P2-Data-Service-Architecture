package org.j4di.analytical.views.network;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.sql.Date;

@Getter
@Entity
@Immutable
@IdClass(OLAP_VIEW_LINE_VALIDATION_RANKING_ID.class)
@Table(name = "OLAP_VIEW_LINE_VALIDATION_RANKING")
public class OLAP_VIEW_LINE_VALIDATION_RANKING {

    @Id
    private Date full_date;

    @Id
    private String line_id;

    private String line_name;
    private Long validation_count;
    private Integer rank_by_day;
    private Integer dense_rank_by_day;
    private Double percent_rank_by_day;
}