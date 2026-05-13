package org.j4di.analytical.views.network;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OLAP_VIEW_LINE_VALIDATION_RANKING_Repository
        extends JpaRepository<OLAP_VIEW_LINE_VALIDATION_RANKING, OLAP_VIEW_LINE_VALIDATION_RANKING_ID> {

    @Query("SELECT v FROM OLAP_VIEW_LINE_VALIDATION_RANKING v")
    List<OLAP_VIEW_LINE_VALIDATION_RANKING> getLineValidationRanking();
}