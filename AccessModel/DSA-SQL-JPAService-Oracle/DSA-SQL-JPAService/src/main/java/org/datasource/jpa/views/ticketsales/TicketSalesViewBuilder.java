package org.datasource.jpa.views.ticketsales;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketSalesViewBuilder {

    @PersistenceContext
    private EntityManager entityManager;

    private List<TicketSalesView> ticketSalesViewList = new ArrayList<>();

    public TicketSalesViewBuilder build() {
        this.ticketSalesViewList.clear();

        String sql = """
                SELECT
                    ts.order_id,
                    ts.city,
                    ts.zone,
                    ts.customer_category,
                    ts.amount_paid,
                    ts.purchase_date
                FROM TICKET_SALES ts
                ORDER BY ts.order_id
                """;

        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> rows = query.getResultList();

        for (Object[] row : rows) {
            Long orderId = row[0] != null ? ((Number) row[0]).longValue() : null;
            String city = row[1] != null ? row[1].toString() : null;
            String zone = row[2] != null ? row[2].toString() : null;
            String customerCategory = row[3] != null ? row[3].toString() : null;
            BigDecimal amountPaid = (BigDecimal) row[4];

            LocalDateTime purchaseDate = null;
            if (row[5] instanceof Timestamp ts) {
                purchaseDate = ts.toLocalDateTime();
            }

            TicketSalesView view = new TicketSalesView(
                    orderId,
                    city,
                    zone,
                    customerCategory,
                    amountPaid,
                    purchaseDate
            );

            this.ticketSalesViewList.add(view);
        }

        return this;
    }

    public List<TicketSalesView> getTicketSalesViewList() {
        return ticketSalesViewList;
    }
}