package com.hrms.LeaveBalance;

import com.hrms.Entity.Enum.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveBalanceDto {

    private Long id;
    private LeaveType leaveType;
    private int totalLeaves;
    private int usedLeaves;
    private int remainingLeaves;

    private Long employeeId;
    private String employeeName;

}
