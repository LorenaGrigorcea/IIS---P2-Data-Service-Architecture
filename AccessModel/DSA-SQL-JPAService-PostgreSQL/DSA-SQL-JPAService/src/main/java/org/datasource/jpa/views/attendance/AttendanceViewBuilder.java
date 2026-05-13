package org.datasource.jpa.views.attendance;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class AttendanceViewBuilder {

    @PersistenceContext
    private EntityManager entityManager;

    private List<AttendanceView> attendanceViewList = new ArrayList<>();

    public AttendanceViewBuilder build() {
        this.attendanceViewList.clear();

        String sql = """
                SELECT
                    a.attendance_id,
                    a.employee_id,
                    a.work_date,
                    a.status,
                    a.hours_worked
                FROM attendance a
                ORDER BY a.attendance_id
                """;

        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> rows = query.getResultList();

        for (Object[] row : rows) {
            Long attendanceId = row[0] != null ? ((Number) row[0]).longValue() : null;
            Long employeeId = row[1] != null ? ((Number) row[1]).longValue() : null;

            LocalDate workDate = null;
            if (row[2] instanceof Date date) {
                workDate = date.toLocalDate();
            }

            String status = row[3] != null ? row[3].toString() : null;
            BigDecimal hoursWorked = (BigDecimal) row[4];

            AttendanceView view = new AttendanceView(attendanceId, employeeId, workDate, status, hoursWorked);
            this.attendanceViewList.add(view);
        }

        return this;
    }

    public List<AttendanceView> getAttendanceViewList() {
        return attendanceViewList;
    }
}