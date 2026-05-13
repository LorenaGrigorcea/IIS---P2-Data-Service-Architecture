package org.datasource.jpa.views.shifts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ShiftsView {

    @JsonProperty("shift_id")
    private Long shiftId;

    @JsonProperty("employee_id")
    private Long employeeId;

    @JsonProperty("vehicle_id")
    private Long vehicleId;

    @JsonProperty("line_id")
    private String lineId;

    @JsonProperty("route_id")
    private String routeId;

    @JsonProperty("shift_start_ts")
    private LocalDateTime shiftStartTs;

    @JsonProperty("shift_end_ts")
    private LocalDateTime shiftEndTs;

    @JsonProperty("shift_type")
    private String shiftType;

    public ShiftsView() {
    }

    public ShiftsView(Long shiftId, Long employeeId, Long vehicleId, String lineId, String routeId,
                      LocalDateTime shiftStartTs, LocalDateTime shiftEndTs, String shiftType) {
        this.shiftId = shiftId;
        this.employeeId = employeeId;
        this.vehicleId = vehicleId;
        this.lineId = lineId;
        this.routeId = routeId;
        this.shiftStartTs = shiftStartTs;
        this.shiftEndTs = shiftEndTs;
        this.shiftType = shiftType;
    }

    public Long getShiftId() {
        return shiftId;
    }

    public void setShiftId(Long shiftId) {
        this.shiftId = shiftId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
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

    public LocalDateTime getShiftStartTs() {
        return shiftStartTs;
    }

    public void setShiftStartTs(LocalDateTime shiftStartTs) {
        this.shiftStartTs = shiftStartTs;
    }

    public LocalDateTime getShiftEndTs() {
        return shiftEndTs;
    }

    public void setShiftEndTs(LocalDateTime shiftEndTs) {
        this.shiftEndTs = shiftEndTs;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }
}