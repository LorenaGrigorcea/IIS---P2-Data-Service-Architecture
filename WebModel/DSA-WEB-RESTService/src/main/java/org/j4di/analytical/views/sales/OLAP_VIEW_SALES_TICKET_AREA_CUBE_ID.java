package org.j4di.analytical.views.sales;

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
public class OLAP_VIEW_SALES_TICKET_AREA_CUBE_ID implements Serializable {

    private String ticket_name;
    private String zone;
}