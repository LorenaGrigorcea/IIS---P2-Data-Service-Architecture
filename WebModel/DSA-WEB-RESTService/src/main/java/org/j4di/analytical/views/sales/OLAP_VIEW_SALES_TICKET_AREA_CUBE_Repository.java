package org.j4di.analytical.views.sales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OLAP_VIEW_SALES_TICKET_AREA_CUBE_Repository
        extends JpaRepository<OLAP_VIEW_SALES_TICKET_AREA_CUBE, OLAP_VIEW_SALES_TICKET_AREA_CUBE_ID> {

    @Query("SELECT v FROM OLAP_VIEW_SALES_TICKET_AREA_CUBE v")
    List<OLAP_VIEW_SALES_TICKET_AREA_CUBE> getSalesTicketAreaCube();
}