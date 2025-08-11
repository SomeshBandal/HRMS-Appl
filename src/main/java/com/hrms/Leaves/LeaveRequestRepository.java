package com.hrms.Leaves;


import com.hrms.Entity.Enum.LeaveStatus;
import com.hrms.Entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findLeavesByEmployeeId(Long employeeId);

    List<LeaveRequest> findByStatus(LeaveStatus status);

}
