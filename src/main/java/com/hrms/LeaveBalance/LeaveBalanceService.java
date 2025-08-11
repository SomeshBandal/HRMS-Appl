package com.hrms.LeaveBalance;
import java.util.List;

public interface LeaveBalanceService {

    LeaveBalanceDto createLeaveBalance(Long employeeId, LeaveBalanceDto dto);
    LeaveBalanceDto updateLeaveBalance(Long id, LeaveBalanceDto dto);
    List<LeaveBalanceDto> getLeaveBalanceById(Long employeeId);
    void deleteLeaveBalance(Long id);
    boolean hasSufficientLeave(Long employeeId, String leaveType, int requestedDays);
    void deductLeave(Long employeeId, String leaveType, int daysUsed);
}
