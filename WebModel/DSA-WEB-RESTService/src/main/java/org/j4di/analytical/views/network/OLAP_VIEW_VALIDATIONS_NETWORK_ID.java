package org.j4di.analytical.views.network;

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
public class OLAP_VIEW_VALIDATIONS_NETWORK_ID implements Serializable {

    private String line_name;
    private String route_id;
    private String stop_name;
}