package org.datasource.jpa.views.payments;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentsViewBuilder {

    @PersistenceContext
    private EntityManager entityManager;

    private List<PaymentsView> paymentsViewList = new ArrayList<>();

    public PaymentsViewBuilder build() {
        this.paymentsViewList.clear();

        String sql = """
                SELECT
                    p.PAYMENT_ID,
                    p.ORDER_ID,
                    p.METHOD,
                    p.STATUS,
                    p.PAY_TS
                FROM PAYMENTS p
                ORDER BY p.PAYMENT_ID
                """;

        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> rows = query.getResultList();

        for (Object[] row : rows) {
            Long paymentId = row[0] != null ? ((Number) row[0]).longValue() : null;
            Long orderId = row[1] != null ? ((Number) row[1]).longValue() : null;
            String method = row[2] != null ? row[2].toString() : null;
            String status = row[3] != null ? row[3].toString() : null;

            LocalDateTime payTs = null;
            if (row[4] instanceof Timestamp ts) {
                payTs = ts.toLocalDateTime();
            }

            PaymentsView view = new PaymentsView(paymentId, orderId, method, status, payTs);
            this.paymentsViewList.add(view);
        }

        return this;
    }

    public List<PaymentsView> getPaymentsViewList() {
        return paymentsViewList;
    }
}