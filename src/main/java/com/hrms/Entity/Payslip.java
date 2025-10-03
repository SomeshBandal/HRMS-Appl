package com.hrms.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payslip {
    @Id @GeneratedValue
    private Long id;
    private YearMonth salaryMonth;
    private double basic;
    private double hra;
    private double deduction;
    private double netSalary;

    @Lob
    private byte[] payslipPdf;

    @ManyToOne
    private Employee employee;
}
