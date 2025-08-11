package com.hrms.PaySlip;

import com.hrms.Employee.EmployeeDto;
import com.hrms.Entity.Employee;
import com.hrms.Entity.Payslip;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class PayslipMapper {

    public PayslipDto toDto(Payslip payslip) {
        if (payslip == null) {
            return null;
        }
        PayslipDto dto = new PayslipDto();
        dto.setId(payslip.getId());
        dto.setSalaryMonth(payslip.getSalaryMonth());
        dto.setNetSalary(payslip.getNetSalary());
        dto.setBasic(payslip.getBasic());
        dto.setHra(payslip.getHra());
        dto.setDeduction(payslip.getDeduction());
        dto.setEmployeeId(payslip.getEmployee().getId());
        dto.setEmployeeName(payslip.getEmployee().getName());

        return dto;
    }

    public Payslip toEntity(PayslipDto dto, Employee employee, MultipartFile file) {
        if (dto == null) {
            return null;
        }

        Payslip payslip = new Payslip();

        payslip.setId(dto.getId());
        payslip.setSalaryMonth(dto.getSalaryMonth());
        payslip.setNetSalary(dto.getNetSalary());
        payslip.setBasic(dto.getBasic());
        payslip.setHra(dto.getHra());
        payslip.setDeduction(dto.getDeduction());
        payslip.setEmployee(employee);


        if (file != null && !file.isEmpty()) {
            try {
                payslip.setPayslipPdf(file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return payslip;
    }

    public PayslipResponseDto toResponseDto(Payslip payslip, String baseUrl){
        if (payslip==null){
            return null;
        }

        PayslipResponseDto dto = new PayslipResponseDto();

        dto.setId(payslip.getId());
        dto.setSalaryMonth(payslip.getSalaryMonth());
        dto.setBasic(payslip.getBasic());
        dto.setHra(payslip.getHra());
        dto.setDeduction(payslip.getDeduction());
        dto.setNetSalary(payslip.getNetSalary());
        dto.setDownloadUrl(baseUrl + "/employee/payslips/download/" + payslip.getId());

        return dto;
}
}
