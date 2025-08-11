package com.hrms.PaySlip;

import com.hrms.Employee.EmployeeDto;
import com.hrms.Employee.EmployeeNotFoundException;
import com.hrms.Employee.EmployeeService;
import com.hrms.Entity.Payslip;
import com.hrms.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Payslip Controller", description = "APIs for generating, viewing, and downloading payslips")
@RequiredArgsConstructor
public class PayslipController {

    private final PayslipService payslipService;
    private final EmployeeService employeeService; // Needed to get employee by userDetails

    @PreAuthorize("hasRole('HR')")
    @PostMapping("hr/payslip/generate")
    @Operation(summary = "Generate Payslip",
               description = "HR uploads a payslip for an employee")

    public ResponseEntity<ApiResponse<PayslipDto>> generatePayslip(
            @PathVariable Long employeeId,
            @RequestParam MultipartFile file,
            @ModelAttribute PayslipDto payslipDto) {

        try {
            PayslipDto savedPayslip = payslipService.generatePayslip(payslipDto, employeeId, file);
            return ResponseEntity.ok(ApiResponse.success("Payslip uploaded successfully",savedPayslip));
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Employee not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Failed to upload payslip", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('HR')")
    @GetMapping("/all/{employeeId}")
    @Operation(summary = "View Payslips",
            description = "Fetching all payslips of employee")

    public ResponseEntity<ApiResponse<List<PayslipDto>>> getAllPayslips(
            @PathVariable Long employeeId) {

        try {
            List<PayslipDto> allPayslips = payslipService.getAllPayslips(employeeId);
            return ResponseEntity.ok().body(ApiResponse.success("payslips fetched successfully", allPayslips));
        } catch (PayslipNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND, "Payslips not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Failed to fetch payslips", e.getMessage()));
        }

    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get list of employee payslips with download URLs")

    public ResponseEntity<ApiResponse<List<PayslipResponseDto>>> getPayslipList(
            @PathVariable Long employeeId,
            HttpServletRequest request) {

        try {
            String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                    .replacePath(null)
                    .build()
                    .toUriString();

            List<PayslipResponseDto> payslips = payslipService.getAllPayslipsWithDownloadLinks(employeeId, baseUrl);
            return ResponseEntity.ok().body(ApiResponse.success("All payslips fetched successfully", payslips));
        } catch (PayslipNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND, "Payslips not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Failed to fetch payslips", e.getMessage()));

        }
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/download/{payslipId}")
    @Operation(summary = "Download a specific payslip file by ID")

    public ResponseEntity<Resource> downloadPayslip(@PathVariable Long payslipId) {
        Payslip payslip = payslipService.getPayslipById(payslipId);

        ByteArrayResource resource = new ByteArrayResource(payslip.getPayslipPdf());

        String fileName = "Payslip_" + payslip.getSalaryMonth() + "_" + payslip.getId() + ".pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body((Resource) resource);
    }

}
