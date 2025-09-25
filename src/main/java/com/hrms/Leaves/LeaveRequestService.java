package com.hrms.Leaves;


import java.util.List;

public interface LeaveRequestService {

    LeaveRequestDto raiseLeaveRequest(LeaveRequestDto leaveRequestDto);

    List<LeaveRequestDto> viewAppliedLeaves(Long employeeId);

    LeaveRequestDto addLeaveManually(ManualLeaveRequestDto dto);

    LeaveRequestDto approveOrRejectLeave(Long leaveId, String status);

    List<LeaveRequestDto> getAllPendingLeaves();

    List<LeaveRequestDto>viewAllLeaveRequests();
}
