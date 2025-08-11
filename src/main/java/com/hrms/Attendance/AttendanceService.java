package com.hrms.Attendance;

import java.util.List;

public interface AttendanceService {

    AttendanceDto saveAttendance(AttendanceDto attendanceDto);
    List<AttendanceDto> getEmployeeMonthlyAttendance(Long employeeId, int year, int month );
    List<AttendanceDto> getAttendance(Long employeeId);
}
