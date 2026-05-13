package org.datasource.jpa.views.shifts;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ShiftsViewBuilder {

    @PersistenceContext
    private EntityManager entityManager;

    private List<ShiftsView> shiftsViewList = new ArrayList<>();

    public ShiftsViewBuilder build() {
        this.shiftsViewList.clear();

        String sql = """
                SELECT
                    s.shift_id,
                    s.employee_id,
                    s.vehicle_id,
                    s.line_id,
                    s.route_id,
                    s.shift_start_ts,
                    s.shift_end_ts,
                    s.shift_type
                FROM shifts s
                ORDER BY s.shift_id
                """;

        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> rows = query.getResultList();

        for (Object[] row : rows) {
            Long shiftId = row[0] != null ? ((Number) row[0]).longValue() : null;
            Long employeeId = row[1] != null ? ((Number) row[1]).longValue() : null;
            Long vehicleId = row[2] != null ? ((Number) row[2]).longValue() : null;
            String lineId = row[3] != null ? row[3].toString() : null;
            String routeId = row[4] != null ? row[4].toString() : null;

            LocalDateTime shiftStartTs = null;
            if (row[5] instanceof Timestamp ts) {
                shiftStartTs = ts.toLocalDateTime();
            }

            LocalDateTime shiftEndTs = null;
            if (row[6] instanceof Timestamp ts) {
                shiftEndTs = ts.toLocalDateTime();
            }

            String shiftType = row[7] != null ? row[7].toString() : null;

            ShiftsView view = new ShiftsView(
                    shiftId, employeeId, vehicleId, lineId, routeId,
                    shiftStartTs, shiftEndTs, shiftType
            );

            this.shiftsViewList.add(view);
        }

        return this;
    }

    public List<ShiftsView> getShiftsViewList() {
        return shiftsViewList;
    }
}