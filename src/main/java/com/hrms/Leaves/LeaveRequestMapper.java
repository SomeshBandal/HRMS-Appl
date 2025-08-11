package com.hrms.Leaves;

import com.hrms.Entity.Employee;
import com.hrms.Entity.Enum.LeaveStatus;
import com.hrms.Entity.LeaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LeaveRequestMapper {

    public LeaveRequestDto toDto(LeaveRequest leaveRequest) {
        if (leaveRequest == null) {
            return null;
        }
        LeaveRequestDto dto = new LeaveRequestDto();
        dto.setId(leaveRequest.getId());
        dto.setFromDate(leaveRequest.getFromDate());
        dto.setToDate(leaveRequest.getToDate());
        dto.setReason(leaveRequest.getReason());
        dto.setLeaveType(leaveRequest.getLeaveType());
        dto.setStatus(leaveRequest.getStatus());

        if (leaveRequest.getEmployee() != null) {
            dto.setEmployeeId(leaveRequest.getEmployee().getId());
            dto.setEmployeeName(leaveRequest.getEmployee().getName());
        }

        return dto;
    }

        public LeaveRequest toEntity(LeaveRequestDto dto, Employee employee) {
            if (dto == null || employee == null) {
                return null;
            }
            LeaveRequest leaveRequest = new LeaveRequest();
            leaveRequest.setId(dto.getId());
            leaveRequest.setLeaveType(dto.getLeaveType());
            leaveRequest.setFromDate(dto.getFromDate());
            leaveRequest.setToDate(dto.getToDate());
            leaveRequest.setReason(dto.getReason());
            leaveRequest.setStatus(LeaveStatus.PENDING);
            leaveRequest.setEmployee(employee);

            return leaveRequest;
        }
    }
