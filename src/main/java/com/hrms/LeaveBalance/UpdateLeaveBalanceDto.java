package com.hrms.LeaveBalance;

import com.hrms.Entity.Enum.LeaveType;
import lombok.Data;

@Data
public class UpdateLeaveBalanceDto {
    private LeaveType leaveType;
    private int totalLeaves;
    private int usedLeaves;
    private int remainingLeaves;
}
