package org.datasource;

import org.datasource.neo4j.views.transportnetwork.DepotView;
import org.datasource.neo4j.views.transportnetwork.LineView;
import org.datasource.neo4j.views.transportnetwork.RouteView;
import org.datasource.neo4j.views.transportnetwork.StopView;
import org.datasource.neo4j.views.transportnetwork.TransportNetworkViewBuilder;
import org.datasource.neo4j.views.transportnetwork.VehicleView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

/* REST Service URL
   http://localhost:8094/DSA-NoSQL-Neo4JService/rest/network/LineView
   http://localhost:8094/DSA-NoSQL-Neo4JService/rest/network/RouteView
   http://localhost:8094/DSA-NoSQL-Neo4JService/rest/network/StopView
   http://localhost:8094/DSA-NoSQL-Neo4JService/rest/network/VehicleView
   http://localhost:8094/DSA-NoSQL-Neo4JService/rest/network/DepotView
*/
@RestController
@RequestMapping("/network")
public class RESTViewServiceNeo4J {

	private static Logger logger = Logger.getLogger(RESTViewServiceNeo4J.class.getName());

	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String pingDataSource() {
		logger.info(">>>> RESTViewServiceNeo4J is Up!");
		return "Ping response from RESTViewServiceNeo4J!";
	}

	@RequestMapping(value = "/LineView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<LineView> get_LineView() throws Exception {
		return this.viewBuilder.build().getLinesViewList();
	}

	@RequestMapping(value = "/RouteView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<RouteView> get_RouteView() throws Exception {
		return this.viewBuilder.build().getRoutesViewList();
	}

	@RequestMapping(value = "/StopView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public List<StopView> get_StopView() throws Exception {
		return this.viewBuilder.build().getStopsViewList();
	}

	@RequestMapping(value = "/VehicleView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<VehicleView> get_VehicleView() throws Exception {
		return this.viewBuilder.build().getVehiclesViewList();
	}

	@RequestMapping(value = "/DepotView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<DepotView> get_DepotView() throws Exception {
		return this.viewBuilder.build().getDepotsViewList();
	}

	@Autowired
	private TransportNetworkViewBuilder viewBuilder;
}