package com.hrms.Attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceDto {
    private Long id;
    private LocalDate date;
    private String status;
    private String remarks;

    private Long employeeId;
    private String employeeName;
}
