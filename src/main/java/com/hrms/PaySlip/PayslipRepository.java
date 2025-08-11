package com.hrms.PaySlip;

import com.hrms.Entity.Payslip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.List;

public interface PayslipRepository extends JpaRepository<Payslip, Long> {

   List<Payslip> findByEmployeeId(Long employeeId);
}
