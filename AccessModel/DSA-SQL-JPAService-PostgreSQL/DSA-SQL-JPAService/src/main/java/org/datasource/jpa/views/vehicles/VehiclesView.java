package org.datasource.jpa.views.vehicles;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VehiclesView {

    @JsonProperty("vehicle_id")
    private Long vehicleId;

    @JsonProperty("fleet_number")
    private String fleetNumber;

    @JsonProperty("vehicle_type")
    private String vehicleType;

    @JsonProperty("capacity")
    private Integer capacity;

    @JsonProperty("status")
    private String status;

    @JsonProperty("depot_id")
    private Long depotId;

    public VehiclesView() {
    }

    public VehiclesView(Long vehicleId, String fleetNumber, String vehicleType,
                        Integer capacity, String status, Long depotId) {
        this.vehicleId = vehicleId;
        this.fleetNumber = fleetNumber;
        this.vehicleType = vehicleType;
        this.capacity = capacity;
        this.status = status;
        this.depotId = depotId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getFleetNumber() {
        return fleetNumber;
    }

    public void setFleetNumber(String fleetNumber) {
        this.fleetNumber = fleetNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }
}