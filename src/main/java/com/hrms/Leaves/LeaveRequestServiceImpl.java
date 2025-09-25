package com.hrms.Leaves;

import com.hrms.Employee.EmployeeNotFoundException;
import com.hrms.Employee.EmployeeRepository;
import com.hrms.Entity.Employee;
import com.hrms.Entity.Enum.LeaveStatus;
import com.hrms.Entity.Enum.LeaveType;
import com.hrms.Entity.LeaveBalance;
import com.hrms.Entity.LeaveRequest;
import com.hrms.LeaveBalance.LeaveBalanceNotFoundException;
import com.hrms.LeaveBalance.LeaveBalanceRepository;
import com.hrms.LeaveBalance.LeaveBalanceServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRepository;

    @Autowired
    private LeaveRequestMapper leaveMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepo;

    @Autowired
    private LeaveBalanceServiceImpl leaveBalanceService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public LeaveRequestDto raiseLeaveRequest(LeaveRequestDto dto) {

        int noOfDays = (int) ChronoUnit.DAYS.between(dto.getFromDate(), dto.getToDate()) + 1;

        // Check if employee has any leave balance records
        List<LeaveBalance> balances = leaveBalanceRepo.findByEmployeeId(dto.getEmployeeId());
        if (balances.isEmpty()) {
            throw new LeaveBalanceNotFoundException("Leave balance not found for employee");
        }

        LeaveType finalLeaveType = dto.getLeaveType();
        LeaveStatus status;

        boolean sufficient = leaveBalanceService.hasSufficientLeave(dto.getEmployeeId(), finalLeaveType.name(), noOfDays);

        String message;
        if (sufficient) {
            // Deduct leave
            leaveBalanceService.deductLeave(dto.getEmployeeId(), finalLeaveType.name(), noOfDays);
            status = LeaveStatus.PENDING;
            message = "Leave applied successfully for " + finalLeaveType.name();

            try {
                notificationService.notifyManager(dto.getEmployeeId(), dto.getReason());
            } catch (Exception e) {
                // Log but don't fail the request
                System.err.println("Failed to notify manager: " + e.getMessage());
            }

        } else {
            // Auto-convert to unpaid
            finalLeaveType = LeaveType.UNPAID_LEAVE;
            status = LeaveStatus.AUTO_CONVERTED_TO_UNPAID;
          message= "No sufficient leaves for " + dto.getLeaveType().name() +
                    ", converted to unpaid leave.";
        }
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Save leave request
        LeaveRequest request = new LeaveRequest();
        BeanUtils.copyProperties(dto, request);
        request.setLeaveType(finalLeaveType);
        request.setNoOfDays(noOfDays);
        request.setStatus(status);
        request.setEmployee(employee);

        LeaveRequest savedRequest = leaveRepository.save(request);
        return leaveMapper.toDto(savedRequest);
    }

    @Override
    public List<LeaveRequestDto> viewAppliedLeaves(Long employeeId) {
        List<LeaveRequest> leaves= leaveRepository.findLeavesByEmployeeId(employeeId);

        if(leaves.isEmpty()){
            throw new LeavesNotFoundException("\"No leave requests found for employee ID: \" + employeeId");
        }
        return leaves.stream().map(leaveMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public LeaveRequestDto addLeaveManually(ManualLeaveRequestDto dto) {
        Employee employee= employeeRepository.findById(dto.getEmployeeId()).orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);
        leaveRequest.setLeaveType(dto.getLeaveType());
        leaveRequest.setFromDate(dto.getStartDate());
        leaveRequest.setToDate(dto.getEndDate());
        leaveRequest.setReason(dto.getReason());
        leaveRequest.setStatus(LeaveStatus.APPROVED);
        leaveRequest.setCreatedByHR(true);

        int noOfDays = (int) ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate()) + 1;
        leaveBalanceService.deductLeave(dto.getEmployeeId(), String.valueOf(dto.getLeaveType()),noOfDays);
        leaveRequest.setNoOfDays(noOfDays);
        LeaveRequest savedLeave = leaveRepository.save(leaveRequest);
        return leaveMapper.toDto(savedLeave);
    }

    @Override
    public LeaveRequestDto approveOrRejectLeave(Long leaveId, String status) {
        LeaveRequest leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new LeavesNotFoundException("Leave ID " + leaveId + " not found"));

        try {
            LeaveStatus newStatus = LeaveStatus.valueOf(status.toUpperCase());
            if (newStatus != LeaveStatus.APPROVED && newStatus != LeaveStatus.REJECTED) {
                throw new IllegalArgumentException("Status must be either APPROVED or REJECTED");
            }

            leave.setStatus(newStatus);
            LeaveRequest updated = leaveRepository.save(leave);
            return leaveMapper.toDto(updated);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid leave status: " + status);
        }
    }

    @Override
    public List<LeaveRequestDto> getAllPendingLeaves() {
        List<LeaveRequest> leaves = leaveRepository.findByStatus(LeaveStatus.PENDING);

        if (leaves.isEmpty()){
            throw new LeavesNotFoundException("No leaves found");
        }
        return leaves.stream().map(leaveMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestDto> viewAllLeaveRequests() {
        List<LeaveRequestDto> listOfAllLeaves = leaveRepository.findAll().stream().map(leaveMapper::toDto).toList();
        if (listOfAllLeaves.isEmpty()){
            throw new LeavesNotFoundException("No leaves found");
        }

        return listOfAllLeaves;
    }
}





