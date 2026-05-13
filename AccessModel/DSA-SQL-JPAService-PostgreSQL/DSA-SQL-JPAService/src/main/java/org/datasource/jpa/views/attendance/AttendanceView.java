package org.datasource.jpa.views.attendance;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AttendanceView {

    @JsonProperty("attendance_id")
    private Long attendanceId;

    @JsonProperty("employee_id")
    private Long employeeId;

    @JsonProperty("work_date")
    private LocalDate workDate;

    @JsonProperty("status")
    private String status;

    @JsonProperty("hours_worked")
    private BigDecimal hoursWorked;

    public AttendanceView() {
    }

    public AttendanceView(Long attendanceId, Long employeeId, LocalDate workDate, String status, BigDecimal hoursWorked) {
        this.attendanceId = attendanceId;
        this.employeeId = employeeId;
        this.workDate = workDate;
        this.status = status;
        this.hoursWorked = hoursWorked;
    }

    public Long getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Long attendanceId) {
        this.attendanceId = attendanceId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(BigDecimal hoursWorked) {
        this.hoursWorked = hoursWorked;
    }
}