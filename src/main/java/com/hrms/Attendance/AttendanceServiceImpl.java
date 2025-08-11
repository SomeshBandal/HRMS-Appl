package com.hrms.Attendance;

import com.hrms.Employee.EmployeeRepository;
import com.hrms.Entity.Attendance;
import com.hrms.Entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public AttendanceDto saveAttendance(AttendanceDto attendanceDto) {
        Employee employee = employeeRepository.findById(attendanceDto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + attendanceDto.getEmployeeId()));

        Attendance entity = attendanceMapper.toEntity(attendanceDto, employee);
        Attendance savedAttendance = attendanceRepository.save(entity);
        return attendanceMapper.toDto(savedAttendance);
    }

    @Override
    public List<AttendanceDto> getEmployeeMonthlyAttendance(Long employeeId, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

       return attendanceRepository.findByEmployeeAndMonth(employeeId, startDate, endDate)
                .stream().map(attendanceMapper::toDto).toList();

    }

    @Override
    public List<AttendanceDto> getAttendance(Long employeeId) {
        return attendanceRepository.findByEmployeeId(employeeId).stream().map(attendanceMapper::toDto).toList();
    }
}
