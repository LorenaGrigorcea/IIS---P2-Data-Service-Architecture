package org.datasource.neo4j.views.transportnetwork;

import org.datasource.neo4j.Neo4JDataSourceConnector;
import org.neo4j.ogm.session.Session;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class TransportNetworkViewBuilder {

    private static Logger logger = Logger.getLogger(TransportNetworkViewBuilder.class.getName());

    private List<LineView> linesViewList;
    private List<RouteView> routesViewList;
    private List<StopView> stopsViewList;
    private List<VehicleView> vehiclesViewList;
    private List<DepotView> depotsViewList;

    public List<LineView> getLinesViewList() {
        return linesViewList;
    }

    public List<RouteView> getRoutesViewList() {
        return routesViewList;
    }

    public List<StopView> getStopsViewList() {
        return stopsViewList;
    }

    public List<VehicleView> getVehiclesViewList() {
        return vehiclesViewList;
    }

    public List<DepotView> getDepotsViewList() {
        return depotsViewList;
    }

    private Neo4JDataSourceConnector dataSourceConnector;

    public TransportNetworkViewBuilder(Neo4JDataSourceConnector dataSourceConnector) {
        this.dataSourceConnector = dataSourceConnector;
    }

    public TransportNetworkViewBuilder build() throws Exception {
        return this.select();
    }

    public TransportNetworkViewBuilder select() throws Exception {
        Session session = dataSourceConnector.getNeo4JSession();
        logger.info(">>> Building TransportNetworkView ... session open!");

        try {
            logger.info(">>> Query LineView ...");
            this.linesViewList = new ArrayList<>(session.loadAll(LineView.class));

            logger.info(">>> Query RouteView ...");
            this.routesViewList = new ArrayList<>(session.loadAll(RouteView.class));

            logger.info(">>> Query StopView ...");
            this.stopsViewList = new ArrayList<>(session.loadAll(StopView.class));

            logger.info(">>> Query VehicleView ...");
            this.vehiclesViewList = new ArrayList<>(session.loadAll(VehicleView.class));

            logger.info(">>> Query DepotView ...");
            this.depotsViewList = new ArrayList<>(session.loadAll(DepotView.class));

            session.clear();
        } catch (Exception e) {
            session.clear();
            throw new RuntimeException(e);
        }

        return this;
    }
}