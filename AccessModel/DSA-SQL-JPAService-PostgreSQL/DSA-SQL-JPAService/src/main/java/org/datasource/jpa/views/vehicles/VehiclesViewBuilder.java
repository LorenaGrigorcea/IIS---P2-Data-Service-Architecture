package org.datasource.jpa.views.vehicles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VehiclesViewBuilder {

    @PersistenceContext
    private EntityManager entityManager;

    private List<VehiclesView> vehiclesViewList = new ArrayList<>();

    public VehiclesViewBuilder build() {
        this.vehiclesViewList.clear();

        String sql = """
                SELECT
                    v.vehicle_id,
                    v.fleet_number,
                    v.vehicle_type,
                    v.capacity,
                    v.status,
                    v.depot_id
                FROM vehicles v
                ORDER BY v.vehicle_id
                """;

        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> rows = query.getResultList();

        for (Object[] row : rows) {
            Long vehicleId = row[0] != null ? ((Number) row[0]).longValue() : null;
            String fleetNumber = row[1] != null ? row[1].toString() : null;
            String vehicleType = row[2] != null ? row[2].toString() : null;
            Integer capacity = row[3] != null ? ((Number) row[3]).intValue() : null;
            String status = row[4] != null ? row[4].toString() : null;
            Long depotId = row[5] != null ? ((Number) row[5]).longValue() : null;

            VehiclesView view = new VehiclesView(vehicleId, fleetNumber, vehicleType, capacity, status, depotId);
            this.vehiclesViewList.add(view);
        }

        return this;
    }

    public List<VehiclesView> getVehiclesViewList() {
        return vehiclesViewList;
    }
}