package org.j4di.analytical.views.operations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OLAP_VIEW_OPERATIONAL_EVENTS_TYPE_Repository
        extends JpaRepository<OLAP_VIEW_OPERATIONAL_EVENTS_TYPE, OLAP_VIEW_OPERATIONAL_EVENTS_TYPE_ID> {

    @Query("SELECT v FROM OLAP_VIEW_OPERATIONAL_EVENTS_TYPE v")
    List<OLAP_VIEW_OPERATIONAL_EVENTS_TYPE> getOperationalEventsType();
}