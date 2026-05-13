package org.datasource;

import org.datasource.mongodb.views.transportoperations.IncidentView;
import org.datasource.mongodb.views.transportoperations.MaintenanceWindowView;
import org.datasource.mongodb.views.transportoperations.SpecialEventView;
import org.datasource.mongodb.views.transportoperations.TelemetryLogView;
import org.datasource.mongodb.views.transportoperations.TransportOperationsView;
import org.datasource.mongodb.views.transportoperations.TransportOperationsViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

/* REST Service URL
   http://localhost:8093/DSA-NoSQL-MongoDBService/rest/transport/IncidentView
   http://localhost:8093/DSA-NoSQL-MongoDBService/rest/transport/MaintenanceWindowView
   http://localhost:8093/DSA-NoSQL-MongoDBService/rest/transport/SpecialEventView
   http://localhost:8093/DSA-NoSQL-MongoDBService/rest/transport/TelemetryLogView
   http://localhost:8093/DSA-NoSQL-MongoDBService/rest/transport/TransportOperationsView
*/
@RestController
@RequestMapping("/transport")
public class RESTViewServiceMongoDB {
	private static Logger logger = Logger.getLogger(RESTViewServiceMongoDB.class.getName());

	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String pingDataSource() {
		logger.info(">>>> RESTViewServiceMongoDB is Up!");
		return "Ping response from RESTViewServiceMongoDB!";
	}

	@RequestMapping(value = "/IncidentView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<IncidentView> get_IncidentView() throws Exception {
		return this.viewBuilder.build().getIncidentsViewList();
	}

	@RequestMapping(value = "/MaintenanceWindowView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<MaintenanceWindowView> get_MaintenanceWindowView() throws Exception {
		return this.viewBuilder.build().getMaintenanceWindowsViewList();
	}

	@RequestMapping(value = "/SpecialEventView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<SpecialEventView> get_SpecialEventView() throws Exception {
		return this.viewBuilder.build().getSpecialEventsViewList();
	}

	@RequestMapping(value = "/TelemetryLogView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<TelemetryLogView> get_TelemetryLogView() throws Exception {
		return this.viewBuilder.build().getTelemetryLogsViewList();
	}

	@RequestMapping(value = "/TransportOperationsView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public TransportOperationsView get_TransportOperationsView() throws Exception {
		return this.viewBuilder.build().getTransportOperationsView();
	}

	@Autowired
	private TransportOperationsViewBuilder viewBuilder;
}