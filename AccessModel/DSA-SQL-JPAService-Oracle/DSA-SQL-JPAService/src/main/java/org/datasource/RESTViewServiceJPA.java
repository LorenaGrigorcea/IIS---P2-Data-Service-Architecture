package org.datasource;

import org.datasource.jpa.views.payments.PaymentsView;
import org.datasource.jpa.views.payments.PaymentsViewBuilder;
import org.datasource.jpa.views.ticketsales.TicketSalesView;
import org.datasource.jpa.views.ticketsales.TicketSalesViewBuilder;
import org.datasource.jpa.views.tickettypes.TicketTypesView;
import org.datasource.jpa.views.tickettypes.TicketTypesViewBuilder;
import org.datasource.jpa.views.ticketvalidations.TicketValidationsView;
import org.datasource.jpa.views.ticketvalidations.TicketValidationsViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;
/*
http://localhost:8091/DSA_SQL_JPAService/rest/ticketing/TicketSalesView
http://localhost:8091/DSA_SQL_JPAService/rest/ticketing/PaymentsView
http://localhost:8091/DSA_SQL_JPAService/rest/ticketing/TicketValidationsView
http://localhost:8091/DSA_SQL_JPAService/rest/ticketing/TicketTypesView
*/
@RestController
@RequestMapping("/ticketing")
public class RESTViewServiceJPA {

	private static Logger logger = Logger.getLogger(RESTViewServiceJPA.class.getName());

	@Autowired
	private TicketSalesViewBuilder ticketSalesViewBuilder;

	@Autowired
	private PaymentsViewBuilder paymentsViewBuilder;

	@Autowired
	private TicketValidationsViewBuilder ticketValidationsViewBuilder;

	@Autowired
	private TicketTypesViewBuilder ticketTypesViewBuilder;

	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String pingDataSource() {
		logger.info(">>>> DSA-SQL-JPAService:: RESTViewService is Up!");
		return "Ping response from DSA-SQL-JPAService!";
	}

	@RequestMapping(value = "/TicketSalesView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public List<TicketSalesView> get_TicketSalesView() {
		return this.ticketSalesViewBuilder.build().getTicketSalesViewList();
	}

	@RequestMapping(value = "/PaymentsView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public List<PaymentsView> get_PaymentsView() {
		return this.paymentsViewBuilder.build().getPaymentsViewList();
	}

	@RequestMapping(value = "/TicketValidationsView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public List<TicketValidationsView> get_TicketValidationsView() {
		return this.ticketValidationsViewBuilder.build().getTicketValidationsViewList();
	}

	@RequestMapping(value = "/TicketTypesView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public List<TicketTypesView> get_TicketTypesView() {
		return this.ticketTypesViewBuilder.build().getTicketTypesViewList();
	}
}