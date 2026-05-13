package org.datasource.neo4j.views.transportnetwork;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import java.io.Serializable;

@NodeEntity(label = "Depot")
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class DepotView implements Serializable {

    @Id
    @Property(name = "depot_id")
    private String depotId;

    @Property(name = "depot_name")
    private String depotName;

    @Property(name = "city")
    private String city;

    @Property(name = "location")
    private String location;
}