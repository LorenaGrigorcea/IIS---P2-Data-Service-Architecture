package org.j4di;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.j4di.analytical.views.daily.OLAP_VIEW_DAILY_SALES_WINDOW;
import org.j4di.analytical.views.daily.OLAP_VIEW_DAILY_SALES_WINDOW_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.j4di.analytical.views.network.OLAP_VIEW_LINE_VALIDATION_RANKING;
import org.j4di.analytical.views.network.OLAP_VIEW_LINE_VALIDATION_RANKING_Repository;
import org.j4di.analytical.views.operations.OLAP_VIEW_OPERATIONAL_EVENTS_TYPE;
import org.j4di.analytical.views.operations.OLAP_VIEW_OPERATIONAL_EVENTS_TYPE_Repository;
import org.j4di.analytical.views.sales.OLAP_VIEW_SALES_TICKET_AREA_CUBE;
import org.j4di.analytical.views.sales.OLAP_VIEW_SALES_TICKET_AREA_CUBE_Repository;
import org.j4di.analytical.views.network.OLAP_VIEW_VALIDATIONS_NETWORK;
import org.j4di.analytical.views.network.OLAP_VIEW_VALIDATIONS_NETWORK_Repository;
import org.j4di.analytical.views.telemetry.OLAP_VIEW_TELEMETRY_WINDOW;
import org.j4di.analytical.views.telemetry.OLAP_VIEW_TELEMETRY_WINDOW_Repository;

import java.util.List;

import java.util.logging.Logger;

@RestController
@RequestMapping("/OLAP")
public class RESTViewService {

	private static final Logger logger =
			Logger.getLogger(RESTViewService.class.getName());
	@Autowired
	private OLAP_VIEW_DAILY_SALES_WINDOW_Repository dailySalesWindowRepository;
	@Autowired
	private OLAP_VIEW_LINE_VALIDATION_RANKING_Repository lineValidationRankingRepository;
	@Autowired
	private OLAP_VIEW_OPERATIONAL_EVENTS_TYPE_Repository operationalEventsTypeRepository;
	@Autowired
	private OLAP_VIEW_SALES_TICKET_AREA_CUBE_Repository salesTicketAreaCubeRepository;

	@Autowired
	private OLAP_VIEW_VALIDATIONS_NETWORK_Repository validationsNetworkRepository;

	@Autowired
	private OLAP_VIEW_TELEMETRY_WINDOW_Repository telemetryWindowRepository;

	@GetMapping(value = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public String pingDataSource() {
		logger.info(">>>> DSA-WEB-RESTService is Up!");
		return "Ping response from DSA-WEB-RESTService!";
	}
	@RequestMapping(value = "/daily-sales-window", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<OLAP_VIEW_DAILY_SALES_WINDOW> getDailySalesWindow() {
		logger.info(">>>> Get OLAP_VIEW_DAILY_SALES_WINDOW");
		return dailySalesWindowRepository.getDailySalesWindow();
	}

	@RequestMapping(value = "/line-validation-ranking", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<OLAP_VIEW_LINE_VALIDATION_RANKING> getLineValidationRanking() {
		logger.info(">>>> Get OLAP_VIEW_LINE_VALIDATION_RANKING");
		return lineValidationRankingRepository.getLineValidationRanking();
	}
	@RequestMapping(value = "/operational-events-type", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<OLAP_VIEW_OPERATIONAL_EVENTS_TYPE> getOperationalEventsType() {
		logger.info(">>>> Get OLAP_VIEW_OPERATIONAL_EVENTS_TYPE");
		return operationalEventsTypeRepository.getOperationalEventsType();
	}
	@RequestMapping(value = "/sales-ticket-area-cube", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<OLAP_VIEW_SALES_TICKET_AREA_CUBE> getSalesTicketAreaCube() {
		logger.info(">>>> Get OLAP_VIEW_SALES_TICKET_AREA_CUBE");
		return salesTicketAreaCubeRepository.getSalesTicketAreaCube();
	}

	@RequestMapping(value = "/validations-network", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<OLAP_VIEW_VALIDATIONS_NETWORK> getValidationsNetwork() {
		logger.info(">>>> Get OLAP_VIEW_VALIDATIONS_NETWORK");
		return validationsNetworkRepository.getValidationsNetwork();
	}

	@RequestMapping(value = "/telemetry-window", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<OLAP_VIEW_TELEMETRY_WINDOW> getTelemetryWindow() {
		logger.info(">>>> Get OLAP_VIEW_TELEMETRY_WINDOW");
		return telemetryWindowRepository.getTelemetryWindow();
	}
}