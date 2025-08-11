package com.hrms.LeaveBalance;
import com.hrms.Entity.Employee;
import com.hrms.Entity.LeaveBalance;
import com.hrms.Entity.Enum.LeaveType;
import org.springframework.stereotype.Component;

@Component
public class LeaveBalanceMapper {

    public LeaveBalanceDto toDto(LeaveBalance leaveBalance) {
        if (leaveBalance == null) {
            return null;
        }

        LeaveBalanceDto dto = new LeaveBalanceDto();

        if (leaveBalance.getId() != null) {
            dto.setEmployeeId(leaveBalance.getId());
        }
        dto.setLeaveType(leaveBalance.getLeaveType());
        dto.setTotalLeaves(leaveBalance.getTotalLeaves());
        dto.setRemainingLeaves(leaveBalance.getRemainingLeaves());
        dto.setUsedLeaves(leaveBalance.getUsedLeaves());
        dto.setEmployeeId(leaveBalance.getEmployee().getId());
        dto.setEmployeeName(leaveBalance.getEmployee().getName());

        return dto;
    }


    public LeaveBalance toEntity(LeaveBalanceDto dto, Employee employee) {
        if (dto == null || employee == null) {
            return null;
        }

        LeaveBalance leaveBalance = new LeaveBalance();
        leaveBalance.setId(dto.getId());
        leaveBalance.setTotalLeaves(dto.getTotalLeaves());
        leaveBalance.setUsedLeaves(dto.getUsedLeaves());
        leaveBalance.setRemainingLeaves(dto.getRemainingLeaves());
        leaveBalance.setLeaveType(dto.getLeaveType());
        leaveBalance.setEmployee(employee);


        return leaveBalance;
    }
}
