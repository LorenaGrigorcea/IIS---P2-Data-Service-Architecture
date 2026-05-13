package org.datasource.neo4j.views.transportnetwork;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import java.io.Serializable;

@NodeEntity(label = "Route")
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class RouteView implements Serializable {

    @Id
    @Property(name = "route_id")
    private String routeId;

    @Property(name = "line_id")
    private String lineId;

    @Property(name = "direction")
    private String direction;
}