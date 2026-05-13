package org.datasource.neo4j.views.transportnetwork;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import java.io.Serializable;

@NodeEntity(label = "Line")
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class LineView implements Serializable {

    @Id
    @Property(name = "line_id")
    private String lineId;

    @Property(name = "display_name")
    private String displayName;

    @Property(name = "real_line_no")
    private String realLineNo;

    @Property(name = "mode")
    private String mode;

    @Property(name = "source_overlap")
    private Boolean sourceOverlap;
}