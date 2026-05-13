package org.j4di.analytical.views.daily;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface OLAP_VIEW_DAILY_SALES_WINDOW_Repository
        extends JpaRepository<OLAP_VIEW_DAILY_SALES_WINDOW, Date> {

    @Query("SELECT v FROM OLAP_VIEW_DAILY_SALES_WINDOW v")
    List<OLAP_VIEW_DAILY_SALES_WINDOW> getDailySalesWindow();
}