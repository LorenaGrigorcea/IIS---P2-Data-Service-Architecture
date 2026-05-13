package org.datasource.jpa.views.ticketvalidations;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class TicketValidationsView {

    @JsonProperty("validation_id")
    private Long validationId;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("validation_ts")
    private LocalDateTime validationTs;

    @JsonProperty("ticket_type_id")
    private Long ticketTypeId;

    @JsonProperty("line_id")
    private String lineId;

    @JsonProperty("route_id")
    private String routeId;

    @JsonProperty("stop_id")
    private String stopId;

    @JsonProperty("vehicle_id")
    private String vehicleId;

    public TicketValidationsView() {
    }

    public TicketValidationsView(Long validationId, Long orderId, LocalDateTime validationTs,
                                 Long ticketTypeId, String lineId, String routeId,
                                 String stopId, String vehicleId) {
        this.validationId = validationId;
        this.orderId = orderId;
        this.validationTs = validationTs;
        this.ticketTypeId = ticketTypeId;
        this.lineId = lineId;
        this.routeId = routeId;
        this.stopId = stopId;
        this.vehicleId = vehicleId;
    }

    public Long getValidationId() {
        return validationId;
    }

    public void setValidationId(Long validationId) {
        this.validationId = validationId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getValidationTs() {
        return validationTs;
    }

    public void setValidationTs(LocalDateTime validationTs) {
        this.validationTs = validationTs;
    }

    public Long getTicketTypeId() {
        return ticketTypeId;
    }

    public void setTicketTypeId(Long ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }
}