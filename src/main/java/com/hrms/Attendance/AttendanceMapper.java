package com.hrms.Attendance;

import com.hrms.Entity.Attendance;
import com.hrms.Entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttendanceMapper {

    public AttendanceDto toDto(Attendance attendance){
        if(attendance==null){
            return null;
        }
        AttendanceDto dto = new AttendanceDto();
        dto.setId(attendance.getId());
        dto.setDate(attendance.getDate());
        dto.setRemarks(attendance.getRemarks());
        dto.setStatus(attendance.getStatus());

        if(attendance.getEmployee()!=null){
            dto.setEmployeeId(attendance.getEmployee().getId());
            dto.setEmployeeName(attendance.getEmployee().getName());
        }

        return dto;
    }

    public Attendance toEntity(AttendanceDto attendanceDto, Employee employee){
        if(attendanceDto==null){
            return null;
        }

        Attendance attendance = new Attendance();
        attendance.setId(attendanceDto.getId());
        attendance.setDate(attendanceDto.getDate());
        attendance.setStatus(attendanceDto.getStatus());
        attendance.setRemarks(attendanceDto.getRemarks());

        attendance.setEmployee(employee);

        return attendance;
    }
}
