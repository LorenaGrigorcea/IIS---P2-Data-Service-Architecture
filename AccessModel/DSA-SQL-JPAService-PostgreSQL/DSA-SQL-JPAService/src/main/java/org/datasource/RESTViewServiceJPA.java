package org.datasource;

import org.datasource.jpa.views.attendance.AttendanceView;
import org.datasource.jpa.views.attendance.AttendanceViewBuilder;
import org.datasource.jpa.views.depots.DepotsView;
import org.datasource.jpa.views.depots.DepotsViewBuilder;
import org.datasource.jpa.views.employees.EmployeesView;
import org.datasource.jpa.views.employees.EmployeesViewBuilder;
import org.datasource.jpa.views.shifts.ShiftsView;
import org.datasource.jpa.views.shifts.ShiftsViewBuilder;
import org.datasource.jpa.views.vehicles.VehiclesView;
import org.datasource.jpa.views.vehicles.VehiclesViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;
/*
http://localhost:8092/DSA_SQL_JPAService_PostgreSQL/rest/operations/EmployeesView
http://localhost:8092/DSA_SQL_JPAService_PostgreSQL/rest/operations/DepotsView
http://localhost:8092/DSA_SQL_JPAService_PostgreSQL/rest/operations/VehiclesView
http://localhost:8092/DSA_SQL_JPAService_PostgreSQL/rest/operations/ShiftsView
http://localhost:8092/DSA_SQL_JPAService_PostgreSQL/rest/operations/AttendanceView
 */
@RestController
@RequestMapping("/operations")
public class RESTViewServiceJPA {

	private static Logger logger = Logger.getLogger(RESTViewServiceJPA.class.getName());

	@Autowired
	private EmployeesViewBuilder employeesViewBuilder;

	@Autowired
	private DepotsViewBuilder depotsViewBuilder;

	@Autowired
	private VehiclesViewBuilder vehiclesViewBuilder;

	@Autowired
	private ShiftsViewBuilder shiftsViewBuilder;

	@Autowired
	private AttendanceViewBuilder attendanceViewBuilder;

	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String pingDataSource() {
		logger.info(">>>> DSA-SQL-JPAService-PostgreSQL:: RESTViewService is Up!");
		return "Ping response from DSA-SQL-JPAService-PostgreSQL!";
	}

	@RequestMapping(value = "/EmployeesView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public List<EmployeesView> get_EmployeesView() {
		return this.employeesViewBuilder.build().getEmployeesViewList();
	}

		@RequestMapping(value = "/DepotsView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public List<DepotsView> get_DepotsView() {
		return this.depotsViewBuilder.build().getDepotsViewList();
	}

	@RequestMapping(value = "/VehiclesView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public List<VehiclesView> get_VehiclesView() {
		return this.vehiclesViewBuilder.build().getVehiclesViewList();
	}

	@RequestMapping(value = "/ShiftsView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public List<ShiftsView> get_ShiftsView() {
		return this.shiftsViewBuilder.build().getShiftsViewList();
	}

	@RequestMapping(value = "/AttendanceView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public List<AttendanceView> get_AttendanceView() {
		return this.attendanceViewBuilder.build().getAttendanceViewList();
	}
}