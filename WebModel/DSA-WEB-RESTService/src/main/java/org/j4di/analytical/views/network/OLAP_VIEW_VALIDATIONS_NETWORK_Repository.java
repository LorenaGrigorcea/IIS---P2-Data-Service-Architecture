package org.j4di.analytical.views.network;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OLAP_VIEW_VALIDATIONS_NETWORK_Repository
        extends JpaRepository<OLAP_VIEW_VALIDATIONS_NETWORK, OLAP_VIEW_VALIDATIONS_NETWORK_ID> {

    @Query("SELECT v FROM OLAP_VIEW_VALIDATIONS_NETWORK v")
    List<OLAP_VIEW_VALIDATIONS_NETWORK> getValidationsNetwork();
}