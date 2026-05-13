package org.j4di.analytical.views.telemetry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OLAP_VIEW_TELEMETRY_WINDOW_Repository
        extends JpaRepository<OLAP_VIEW_TELEMETRY_WINDOW, OLAP_VIEW_TELEMETRY_WINDOW_ID> {

    @Query("SELECT v FROM OLAP_VIEW_TELEMETRY_WINDOW v")
    List<OLAP_VIEW_TELEMETRY_WINDOW> getTelemetryWindow();
}