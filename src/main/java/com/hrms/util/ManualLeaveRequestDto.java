package com.hrms.util;

import com.hrms.Entity.Enum.LeaveStatus;
import com.hrms.Entity.Enum.LeaveType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class  ManualLeaveRequestDto {
    private Long id;
    private Long employeeId;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String employeeName;
}