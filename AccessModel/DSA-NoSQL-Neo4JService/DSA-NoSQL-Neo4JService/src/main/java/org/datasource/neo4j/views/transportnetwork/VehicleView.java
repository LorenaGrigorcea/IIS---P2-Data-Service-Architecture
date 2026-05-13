package org.datasource.neo4j.views.transportnetwork;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import java.io.Serializable;

@NodeEntity(label = "Vehicle")
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class VehicleView implements Serializable {

    @Id
    @Property(name = "vehicle_id")
    private String vehicleId;

    @Property(name = "vehicle_code")
    private String vehicleCode;

    @Property(name = "fleet_number")
    private String fleetNumber;

    @Property(name = "vehicle_type")
    private String vehicleType;

    @Property(name = "status")
    private String status;
}