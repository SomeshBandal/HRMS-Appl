package com.hrms.Employee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Employee DTO for data transfer")
    public class EmployeeDto {

        @Schema(description = "Employee ID", example = "101")
        private Long id;

        @Schema(description = "Full name of the employee", example = "Ashok Jadhav")
        private String name;

        @Schema(description = "Email address of the employee", example = "ashokjadhav@example.com")
        private String email;

        @Schema(description = "Department of the employee", example = "Testing")
        private String department;

        @Schema(description = "Role of the employee (HR or EMPLOYEE)", example = "EMPLOYEE")
        private String role;

        @Schema(description = "Date when the employee joined", example = "2023-05-15")
        private LocalDate dateOfJoining;

        @Schema(description = "Password (hashed)", example = "$2a$10$hashedPasswordValue")
        private String password;

    }

