package org.datasource.jpa.views.depots;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DepotsViewBuilder {

    @PersistenceContext
    private EntityManager entityManager;

    private List<DepotsView> depotsViewList = new ArrayList<>();

    public DepotsViewBuilder build() {
        this.depotsViewList.clear();

        String sql = """
                SELECT
                    d.depot_id,
                    d.depot_name,
                    d.city,
                    d.location
                FROM depots d
                ORDER BY d.depot_id
                """;

        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> rows = query.getResultList();

        for (Object[] row : rows) {
            Long depotId = row[0] != null ? ((Number) row[0]).longValue() : null;
            String depotName = row[1] != null ? row[1].toString() : null;
            String city = row[2] != null ? row[2].toString() : null;
            String location = row[3] != null ? row[3].toString() : null;

            DepotsView view = new DepotsView(depotId, depotName, city, location);
            this.depotsViewList.add(view);
        }

        return this;
    }

    public List<DepotsView> getDepotsViewList() {
        return depotsViewList;
    }
}