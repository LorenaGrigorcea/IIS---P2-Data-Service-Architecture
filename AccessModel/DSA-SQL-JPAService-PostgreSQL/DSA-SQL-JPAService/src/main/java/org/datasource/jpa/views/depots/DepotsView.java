package org.datasource.jpa.views.depots;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DepotsView {

    @JsonProperty("depot_id")
    private Long depotId;

    @JsonProperty("depot_name")
    private String depotName;

    @JsonProperty("city")
    private String city;

    @JsonProperty("location")
    private String location;

    public DepotsView() {
    }

    public DepotsView(Long depotId, String depotName, String city, String location) {
        this.depotId = depotId;
        this.depotName = depotName;
        this.city = city;
        this.location = location;
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}