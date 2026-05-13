package org.datasource.jpa.views.ticketsales;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TicketSalesView {

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("city")
    private String city;

    @JsonProperty("zone")
    private String zone;

    @JsonProperty("customer_category")
    private String customerCategory;

    @JsonProperty("amount_paid")
    private BigDecimal amountPaid;

    @JsonProperty("purchase_date")
    private LocalDateTime purchaseDate;

    public TicketSalesView() {
    }

    public TicketSalesView(Long orderId, String city, String zone, String customerCategory,
                           BigDecimal amountPaid, LocalDateTime purchaseDate) {
        this.orderId = orderId;
        this.city = city;
        this.zone = zone;
        this.customerCategory = customerCategory;
        this.amountPaid = amountPaid;
        this.purchaseDate = purchaseDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getCustomerCategory() {
        return customerCategory;
    }

    public void setCustomerCategory(String customerCategory) {
        this.customerCategory = customerCategory;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}