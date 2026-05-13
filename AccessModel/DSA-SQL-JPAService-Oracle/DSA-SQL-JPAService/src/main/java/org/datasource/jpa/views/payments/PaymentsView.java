package org.datasource.jpa.views.payments;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class PaymentsView {

    @JsonProperty("payment_id")
    private Long paymentId;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("method")
    private String method;

    @JsonProperty("status")
    private String status;

    @JsonProperty("pay_ts")
    private LocalDateTime payTs;

    public PaymentsView() {
    }

    public PaymentsView(Long paymentId, Long orderId, String method, String status, LocalDateTime payTs) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.method = method;
        this.status = status;
        this.payTs = payTs;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getPayTs() {
        return payTs;
    }

    public void setPayTs(LocalDateTime payTs) {
        this.payTs = payTs;
    }
}