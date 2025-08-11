package com.hrms.Leaves;

import com.hrms.Entity.Enum.LeaveStatus;
import com.hrms.Entity.Enum.LeaveType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequestDto {

    private Long id;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String reason;
    private int noOfDays;
    private LeaveType leaveType; // PAID or UNPAID
    private LeaveStatus status;  // PENDING, APPROVED, REJECTED

    private String employeeName;
    private Long employeeId;
}
