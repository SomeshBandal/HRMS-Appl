package com.hrms.Employee;


import com.hrms.Entity.Employee;
import com.hrms.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registerHr")
    @Operation(summary = "Registration of Hr")
    public ResponseEntity<ApiResponse<EmployeeDto>> registerHr(@RequestBody EmployeeDto dto) {
        if (!"HR".equalsIgnoreCase(dto.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(HttpStatus.FORBIDDEN, "Only HR can be registered via this endpoint.", null));
        }
        try{
            EmployeeDto employeeDto = employeeService.saveEmployee(dto);
            return ResponseEntity.ok().body(ApiResponse.success("Hr registered successfully", employeeDto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Failed to register Hr", e.getMessage()));
        }
    }


    @Operation(summary = "Register a new employee")
    @PostMapping("/hr/employee")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<ApiResponse<EmployeeDto>> registerEmployee(
            @Parameter(description = "Employee details required", required = true)
            @Valid @RequestBody EmployeeDto employeeDto) {

        try {
            EmployeeDto employeeDto1 = employeeService.saveEmployee(employeeDto);
            return ResponseEntity.ok(ApiResponse.success("Employee saved successfully", employeeDto1));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Failed to save Employee", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('HR')")
    @Operation(summary = "Fetching all employees", description = "Retrieves all employees ")
    @GetMapping("/hr/employees")
    public ResponseEntity<ApiResponse<List<EmployeeDto>>> allEmployees() {
        try {
            List<EmployeeDto> allEmployees = employeeService.getAllEmployees();
            return ResponseEntity.ok().body(ApiResponse.success("All employees fetched successfully", allEmployees));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Failed to retrieve all Employees", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('HR')")
    @GetMapping("/hr/employee/{id}")
    @Operation(
            summary = "Fetching single employee" ,
            description = "Retrieves employee by unique identifier")

    public ResponseEntity<ApiResponse<EmployeeDto>>getEmployeeById(
            @Parameter(description = "unique identifier of employee", required = true)
            @PathVariable @Valid @Min(1) Long id){
        try {
            EmployeeDto employeeById = employeeService.getEmployeeById(id);
            return ResponseEntity.ok().body(ApiResponse.success("Employee details fetched successfully", employeeById));
        }catch(EmployeeNotFoundException e){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND,"Employee with id: "+ id +"not found", e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"Failed to get employee details", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('HR')")
    @PatchMapping("hr/employee/{id}")
    @Operation(summary = "Updating employee details")

    public ResponseEntity<ApiResponse<EmployeeDto>>updateEmployee(
            @Parameter(description = "unique identifier of employee", required = true)
            @PathVariable @Min(1) Long id,
            @Parameter(description = "employee details to update",required = true)
            @Valid @RequestBody EmployeeDto employeeDto){

        try{
             EmployeeDto employeeDto1 = employeeService.updateEmployee(id, employeeDto);
             return ResponseEntity.ok().body(ApiResponse.success("Employee details updated successfully", employeeDto1));
        }catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND, "Employee does not exist", e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Failed to update employee", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('HR')")
    @PatchMapping("/hr/employee/deactivate/{id}")
    @Operation(summary = "Deactivating an employee")

    public ResponseEntity<ApiResponse<Void>>deactivateEmployee(
            @Parameter(description = "employee id",required = true)
            @PathVariable Long id) {

        try {
            employeeService.deactivateEmployee(id);
            return ResponseEntity.ok().body(ApiResponse.success("Employee deactivated successfully"));
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Employee does not exist with id ", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Failed to deactivate Employee", e.getMessage()));
        }
    }

    @Operation(summary = "Activating an employee")
    @PatchMapping("/hr/employee/activate/{id}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<ApiResponse<Void>>activateEmployee(
            @Parameter(description = "Employee id", required = true)
            @PathVariable Long id){

        try{
            employeeService.activateEmployee(id);
            return ResponseEntity.ok().body(ApiResponse.success("Employee activated successfully"));
        }catch (EmployeeNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND,"Employee does not exist",e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST,"Failed to activate employee with id :" + id,e.getMessage()));
        }
    }

    @Operation(summary = "Fetching all active employees")
    @PreAuthorize("hasRole('HR')")
    @GetMapping("/hr/employee/allActiveEmployees")
    public ResponseEntity<ApiResponse<List<EmployeeDto>>>getAllActiveEmployees(){
        try{
            List<EmployeeDto> activeEmployees = employeeService.getActiveEmployees();
            return ResponseEntity.ok()
                    .body(ApiResponse.success("Active employees fetched successfully", activeEmployees));
        }catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST,"Failed to fetch all activeEmployees", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/employee/profile")
    @Operation(summary = "Fetching own profile" ,
               description = "Retrieves employee details by email and password")

    public ResponseEntity<ApiResponse<EmployeeDto>>ownProfile() {

        try {
            EmployeeDto employee = employeeService.ownProfile();
            return ResponseEntity.ok().body(ApiResponse.success("Profile details fetched successfully", employee));
        }catch(EmployeeNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND,"Employee profile not found", e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"Failed to get profile details", e.getMessage()));
        }
    }
}
