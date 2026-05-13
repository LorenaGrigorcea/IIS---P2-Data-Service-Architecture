package org.j4di.analytical.views.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OLAP_VIEW_OPERATIONAL_EVENTS_TYPE_ID implements Serializable {

    private String source_type;
    private String event_subtype;
    private String impact_label;
}