package com.hrms.LeaveBalance;

import com.hrms.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaveBalance")
@RequiredArgsConstructor
public class LeaveBalanceController {

    @Autowired
    private LeaveBalanceService leaveBalanceService;


    @PreAuthorize("hasRole('HR')")
    @Operation(summary = "Create leave balance for an employee")
    @PostMapping("/{employeeId}")
    public ResponseEntity<ApiResponse<LeaveBalanceDto>> createLeaveBalance(
            @PathVariable @Parameter(description = "Employee ID") Long employeeId,
            @RequestBody LeaveBalanceDto dto) {

        try {
            LeaveBalanceDto created = leaveBalanceService.createLeaveBalance(employeeId, dto);
            return ResponseEntity.ok()
                    .body(ApiResponse.success("Leave balance created successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().
                    body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Failed to create leave balance", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('HR')")
    @Operation(summary = "Update leave balance by leaveBalanceId")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LeaveBalanceDto>> updateLeaveBalance(
            @PathVariable @Parameter(description = "LeaveBalance ID") Long id,
            @RequestBody UpdateLeaveBalanceDto dto) {

        try {
            LeaveBalanceDto updated = leaveBalanceService.updateLeaveBalance(id, dto);
            return ResponseEntity.ok().body(ApiResponse.success("Leave balance updated successfully"));
        } catch (LeaveBalanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Leave balance not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, "updating leave balance failed", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('HR','ADMIN')")
    @Operation(summary = "Get leave balance by employeeId")
    @GetMapping("/{employeeId}")
    public ResponseEntity<ApiResponse<List<LeaveBalanceDto>>> getLeaveBalanceById(
            @PathVariable @Parameter(description = "Employee ID") Long employeeId) {
        try {
            List<LeaveBalanceDto> balance = leaveBalanceService.getLeaveBalanceById(employeeId);
            return ResponseEntity.ok().body(ApiResponse.success("Leave balance retrieved successfully", balance));
        } catch (LeaveBalanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND, "Leave balance not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Failed to retrieve leave balance", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('HR')")
    @Operation(summary = "Delete leave balance by Leave balance id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLeaveBalance(
            @PathVariable @Parameter(description = "LeaveBalance Id") Long id){

        leaveBalanceService.deleteLeaveBalance(id);
        return ResponseEntity.ok().body("Leave balance deleted successfully");
    }


    @Operation(summary = "Check if employee has sufficient leave balance")
    @GetMapping("/check-balance")
    public ResponseEntity<ApiResponse<Boolean>> checkSufficientLeave(
            @RequestParam Long employeeId,
            @RequestParam String leaveType,
            @RequestParam int requestedDays) {

        try {
            boolean result = leaveBalanceService.hasSufficientLeave(employeeId, leaveType, requestedDays);
            return ResponseEntity.ok().body(ApiResponse.success("Leave balance check completed", result));
        } catch (LeaveBalanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Leave balance not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Failed to check leave balance", e.getMessage()));
        }
    }

    @Operation(summary = "Deduct leave from employee's balance")
    @PutMapping("/deduct-leave")
    public ResponseEntity<ApiResponse<String>> deductLeave(
            @RequestParam Long employeeId,
            @RequestParam String leaveType,
            @RequestParam int daysUsed) {

            leaveBalanceService.deductLeave(employeeId, leaveType, daysUsed);
            return ResponseEntity.ok().body(ApiResponse.success("Leave deducted successfully"));
        }
    }

