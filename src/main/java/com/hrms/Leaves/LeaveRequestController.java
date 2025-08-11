package com.hrms.Leaves;

import com.hrms.Employee.EmployeeNotFoundException;
import com.hrms.util.ApiResponse;
import com.hrms.util.ManualLeaveRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @PreAuthorize("hasRole('EMPLOYEE')")
    @Operation(summary = "Apply for Unpaid Leave", description = "Allows an employee to raise a request for unpaid leave")
    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<LeaveRequestDto>> applyForLeave(
            @Parameter(description = "leave request details required", required = true)
            @Valid @RequestBody LeaveRequestDto dto) {
        try {
            LeaveRequestDto leave = leaveRequestService.raiseLeaveRequest(dto);
            return ResponseEntity.ok().body(ApiResponse.success("Request saved successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Request failed", e.getMessage()));
        }
    }


    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/appliedLeaves/{employeeId}")
    @Operation(
            summary = "fetching all leave requests of employee",
            description = "Employee can fetch all his leave requests")

    public ResponseEntity<ApiResponse<List<LeaveRequestDto>>> getAppliedLeaves(
            @Parameter(description = "unique identifier of employee", required = true)
            @PathVariable @Min(1) Long employeeId) {
        try {
            List<LeaveRequestDto> appliedLeaves = leaveRequestService.viewAppliedLeaves(employeeId);
            return ResponseEntity.ok().body(ApiResponse.success("applied leaves fetched successfully", appliedLeaves));
        } catch (LeavesNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND, "Leaves not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Failed to fetch leaves", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('HR')")
    @PostMapping("/manual")
    @Operation(
            summary = "Manually Add Paid/Unpaid Leave",
            description = "HR can directly add an approved paid or unpaid leave for an employee")

    public ResponseEntity<ApiResponse<LeaveRequestDto>> addLeaveManually(
            @Parameter(description = "leave details required", required = true)
            @Valid @RequestBody ManualLeaveRequestDto dto) {

        try {
            LeaveRequestDto savedLeave = leaveRequestService.addLeaveManually(dto);
            return ResponseEntity.ok(ApiResponse.success("Leave added successfully", savedLeave));
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Employee not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Failed to add leave", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('HR')")
    @PutMapping("/{leaveId}/{status}")
    @Operation(
            summary = "Approve or Reject Leave Request",
            description = "HR can approve or reject a leave request using its ID and the new status (APPROVED or REJECTED)"
    )
    public ResponseEntity<ApiResponse<LeaveRequestDto>> approveOrRejectLeave(
            @Parameter(description = "Leave ID to update", required = true)
            @PathVariable Long leaveId,
            @Parameter(description = "New status (APPROVED or REJECTED)", required = true, example = "APPROVED")
            @PathVariable String status) {

        try {
            LeaveRequestDto updatedLeave = leaveRequestService.approveOrRejectLeave(leaveId, status);
            return ResponseEntity.ok(ApiResponse.success("Leave status updated successfully", updatedLeave));
        } catch (LeavesNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Leave not found", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Invalid status", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update leave status", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('HR')")
    @GetMapping("/pending")
    @Operation(summary = "Get All Pending Leave Requests", description = "Returns a list of all leave requests with status PENDING")
    public ResponseEntity<ApiResponse<List<LeaveRequestDto>>> getPendingLeaves() {
        try {
            List<LeaveRequestDto> pendingLeaves = leaveRequestService.getAllPendingLeaves();
            return ResponseEntity.ok(ApiResponse.success("Pending leaves fetched successfully", pendingLeaves));
        } catch (LeavesNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "No pending leaves", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Failed to fetch leaves", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('HR')")
    @GetMapping("/all")
    @Operation(
            summary = "Fetch All Leave Requests",
            description = "HR can fetch all leave requests from all employees, regardless of status")

    public ResponseEntity<ApiResponse<List<LeaveRequestDto>>> getAllLeaveRequests() {
        try {
            List<LeaveRequestDto> leaves = leaveRequestService.viewAllLeaveRequests();
            return ResponseEntity.ok(ApiResponse.success("All leave requests fetched successfully", leaves));
        } catch (LeavesNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "No leave requests found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Failed to fetch leave requests", e.getMessage()));
        }
    }


}