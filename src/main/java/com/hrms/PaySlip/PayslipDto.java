package com.hrms.PaySlip;

import com.hrms.Entity.Employee;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Data
@AllArgsConstructor
@NoArgsConstructor
    public class PayslipDto {

        private Long id;
        private YearMonth salaryMonth;
        private double basic;
        private double hra;
        private double deduction;
        private double netSalary;
        @Lob
        private byte[] payslipPdf;

        private Long employeeId;
        private String employeeName;

    }
