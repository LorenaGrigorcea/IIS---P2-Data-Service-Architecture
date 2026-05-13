package org.datasource.jpa.views.ticketvalidations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TicketValidationsViewBuilder {

    @PersistenceContext
    private EntityManager entityManager;

    private List<TicketValidationsView> ticketValidationsViewList = new ArrayList<>();

    public TicketValidationsViewBuilder build() {
        this.ticketValidationsViewList.clear();

        String sql = """
                SELECT
                    tv.VALIDATION_ID,
                    tv.ORDER_ID,
                    tv.VALIDATION_TS,
                    tv.TICKET_TYPE_ID,
                    tv.LINE_ID,
                    tv.ROUTE_ID,
                    tv.STOP_ID,
                    tv.VEHICLE_ID
                FROM TICKET_VALIDATIONS tv
                ORDER BY tv.VALIDATION_ID
                """;

        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> rows = query.getResultList();

        for (Object[] row : rows) {
            Long validationId = toLong(row[0]);
            Long orderId = toLong(row[1]);
            LocalDateTime validationTs = toLocalDateTime(row[2]);
            Long ticketTypeId = toLong(row[3]);

            String lineId = toStringValue(row[4]);
            String routeId = toStringValue(row[5]);
            String stopId = toStringValue(row[6]);
            String vehicleId = toStringValue(row[7]);

            TicketValidationsView view = new TicketValidationsView(
                    validationId,
                    orderId,
                    validationTs,
                    ticketTypeId,
                    lineId,
                    routeId,
                    stopId,
                    vehicleId
            );

            this.ticketValidationsViewList.add(view);
        }

        return this;
    }

    public List<TicketValidationsView> getTicketValidationsViewList() {
        return ticketValidationsViewList;
    }

    private Long toLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.parseLong(value.toString());
    }

    private String toStringValue(Object value) {
        return value == null ? null : value.toString();
    }

    private LocalDateTime toLocalDateTime(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDateTime ldt) {
            return ldt;
        }
        if (value instanceof Timestamp ts) {
            return ts.toLocalDateTime();
        }
        if (value instanceof Date date) {
            return date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        }
        throw new IllegalArgumentException("Unsupported date type for VALIDATION_TS: " + value.getClass().getName());
    }
}