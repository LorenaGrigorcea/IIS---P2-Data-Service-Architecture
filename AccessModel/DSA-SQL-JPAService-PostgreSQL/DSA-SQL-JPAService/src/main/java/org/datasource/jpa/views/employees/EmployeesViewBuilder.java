package org.datasource.jpa.views.employees;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeesViewBuilder {

    @PersistenceContext
    private EntityManager entityManager;

    private List<EmployeesView> employeesViewList = new ArrayList<>();

    public EmployeesViewBuilder build() {
        this.employeesViewList.clear();

        String sql = """
                SELECT
                    e.employee_id,
                    e.first_name,
                    e.last_name,
                    e.role,
                    e.hire_date,
                    e.status,
                    e.depot_id
                FROM employees e
                ORDER BY e.employee_id
                """;

        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> rows = query.getResultList();

        for (Object[] row : rows) {
            Long employeeId = row[0] != null ? ((Number) row[0]).longValue() : null;
            String firstName = row[1] != null ? row[1].toString() : null;
            String lastName = row[2] != null ? row[2].toString() : null;
            String role = row[3] != null ? row[3].toString() : null;

            LocalDate hireDate = null;
            if (row[4] instanceof Date date) {
                hireDate = date.toLocalDate();
            }

            String status = row[5] != null ? row[5].toString() : null;
            Long depotId = row[6] != null ? ((Number) row[6]).longValue() : null;

            EmployeesView view = new EmployeesView(
                    employeeId,
                    firstName,
                    lastName,
                    role,
                    hireDate,
                    status,
                    depotId
            );

            this.employeesViewList.add(view);
        }

        return this;
    }

    public List<EmployeesView> getEmployeesViewList() {
        return employeesViewList;
    }
}