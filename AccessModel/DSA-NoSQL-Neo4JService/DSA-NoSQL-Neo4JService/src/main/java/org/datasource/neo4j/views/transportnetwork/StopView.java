package org.datasource.neo4j.views.transportnetwork;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NodeEntity(label = "Stop")
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class StopView implements Serializable {

    @Id
    @Property(name = "stop_id")
    private String stopId;

    @Property(name = "stop_name")
    private String stopName;

    @Property(name = "area")
    private String area;

    @Property(name = "shared_core")
    private Boolean sharedCore;

    @JsonIgnore
    @Relationship(type = "NEXT_ON_ROUTE")
    private List<StopView> nextStops = new ArrayList<>();
}