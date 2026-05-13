package org.datasource.jpa.views.tickettypes;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketTypesViewBuilder {

    @PersistenceContext
    private EntityManager entityManager;

    private List<TicketTypesView> ticketTypesViewList = new ArrayList<>();

    public TicketTypesViewBuilder build() {
        this.ticketTypesViewList.clear();

        String sql = """
                SELECT
                    tt.TICKET_TYPE_ID,
                    tt.TICKET_NAME,
                    tt.BASE_PRICE,
                    tt.VALIDITY_MINUTES,
                    tt.VALIDITY_DAYS
                FROM TICKET_TYPES tt
                ORDER BY tt.TICKET_TYPE_ID
                """;

        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> rows = query.getResultList();

        for (Object[] row : rows) {
            Long ticketTypeId = row[0] != null ? ((Number) row[0]).longValue() : null;
            String ticketName = row[1] != null ? row[1].toString() : null;
            BigDecimal basePrice = (BigDecimal) row[2];
            Integer validityMinutes = row[3] != null ? ((Number) row[3]).intValue() : null;
            Integer validityDays = row[4] != null ? ((Number) row[4]).intValue() : null;

            TicketTypesView view = new TicketTypesView(
                    ticketTypeId,
                    ticketName,
                    basePrice,
                    validityMinutes,
                    validityDays
            );

            this.ticketTypesViewList.add(view);
        }

        return this;
    }

    public List<TicketTypesView> getTicketTypesViewList() {
        return ticketTypesViewList;
    }
}