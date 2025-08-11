package com.hrms.PaySlip;

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
        private Long employeeId;
        private String employeeName;
    }
