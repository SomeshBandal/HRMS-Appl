package com.hrms.Attendance;

import com.hrms.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PreAuthorize("hasRole('HR')")
    @PostMapping("/hr/attendance")
    @Operation(summary = "saving attendance", description = "saving attendance of employees")

    public ResponseEntity<ApiResponse<AttendanceDto>>saveAttendance(
            @Parameter(description = "Attendance details",required = true)
            @RequestBody AttendanceDto attendanceDto) {

        try {
            AttendanceDto attendanceDto1 = attendanceService.saveAttendance(attendanceDto);
            return ResponseEntity.ok().body(ApiResponse.success("Attendance saved successfully", attendanceDto1));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Failed to save attendance", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('HR')")
    @GetMapping("/hr/attendance/monthly/{employeeId}")
    @Operation(summary = "getting employee monthly attendance")

    public ResponseEntity<ApiResponse<List<AttendanceDto>>>getAttendanceOfMonth(
                @PathVariable Long employeeId,
                @RequestParam int year,
                @RequestParam int month){

        try{
            List<AttendanceDto> employeeMonthlyAttendance = attendanceService.getEmployeeMonthlyAttendance(employeeId, year, month);
            return ResponseEntity.ok().body(ApiResponse.success("Monthly attendance of employee fetched successfully",employeeMonthlyAttendance));
        }catch(AttendanceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND,"Monthly attendance not found", e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"Failed to fetch monthly attendance", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/employee/attendance")
    @Operation(summary = "Getting attendance details", description = "getting own attendance details by employee")

    public ResponseEntity<ApiResponse<List<AttendanceDto>>>getOwnRecords(@RequestParam Long employeeId){
        try{
            List<AttendanceDto> attendance = attendanceService.getAttendance(employeeId);
            return ResponseEntity.ok().body(ApiResponse.success("Fetched attendance successfully", attendance));
        }catch (AttendanceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND,"attendance not found", e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"Failed to fetch attendance", e.getMessage()));
        }
    }
}
