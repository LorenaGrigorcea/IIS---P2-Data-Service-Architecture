package org.datasource.jpa.views.tickettypes;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class TicketTypesView {

    @JsonProperty("ticket_type_id")
    private Long ticketTypeId;

    @JsonProperty("ticket_name")
    private String ticketName;

    @JsonProperty("base_price")
    private BigDecimal basePrice;

    @JsonProperty("validity_minutes")
    private Integer validityMinutes;

    @JsonProperty("validity_days")
    private Integer validityDays;

    public TicketTypesView() {
    }

    public TicketTypesView(Long ticketTypeId, String ticketName, BigDecimal basePrice,
                           Integer validityMinutes, Integer validityDays) {
        this.ticketTypeId = ticketTypeId;
        this.ticketName = ticketName;
        this.basePrice = basePrice;
        this.validityMinutes = validityMinutes;
        this.validityDays = validityDays;
    }

    public Long getTicketTypeId() {
        return ticketTypeId;
    }

    public void setTicketTypeId(Long ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public Integer getValidityMinutes() {
        return validityMinutes;
    }

    public void setValidityMinutes(Integer validityMinutes) {
        this.validityMinutes = validityMinutes;
    }

    public Integer getValidityDays() {
        return validityDays;
    }

    public void setValidityDays(Integer validityDays) {
        this.validityDays = validityDays;
    }
}