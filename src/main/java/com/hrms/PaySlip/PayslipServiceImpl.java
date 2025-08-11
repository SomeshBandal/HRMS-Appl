package com.hrms.PaySlip;

import com.hrms.Employee.EmployeeDto;
import com.hrms.Employee.EmployeeNotFoundException;
import com.hrms.Employee.EmployeeRepository;
import com.hrms.Entity.Employee;
import com.hrms.Entity.Payslip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayslipServiceImpl implements PayslipService{

    @Autowired
    private PayslipRepository payslipRepository;

    @Autowired
    private PayslipMapper payslipMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public PayslipDto generatePayslip(PayslipDto payslipDto, Long employeeId, MultipartFile file) throws IOException {
        Employee employee= employeeRepository.findById(employeeId).orElseThrow(()->new EmployeeNotFoundException("Employee not found"));

        Payslip payslip = new Payslip();
        payslip.setEmployee(employee);
        payslip.setId(payslip.getId());
        payslip.setSalaryMonth(payslipDto.getSalaryMonth());
        payslip.setBasic(payslipDto.getBasic());
        payslip.setHra(payslipDto.getHra());
        payslip.setDeduction(payslipDto.getDeduction());
        payslip.setNetSalary(payslipDto.getNetSalary());
        payslip.setPayslipPdf(file.getBytes());

        Payslip savedPayslip = payslipRepository.save(payslip);
        return payslipMapper.toDto(savedPayslip);
    }

    @Override
    public List<PayslipDto> getAllPayslips(Long employeeID) {
        List<Payslip> payslip = payslipRepository.findByEmployeeId(employeeID);
        return payslip.stream().map(payslipMapper::toDto).toList();
    }

    @Override
    public List<PayslipResponseDto> getAllPayslipsWithDownloadLinks(Long employeeId, String baseUrl) {
        List<PayslipDto> payslips = payslipRepository.findByEmployeeId(employeeId).stream().map(payslipMapper::toDto).toList();

        if (payslips.isEmpty()) {
            throw new PayslipNotFoundException("No payslips found for employeeId: " + employeeId);
        }

        return payslips.stream()
                .map(p -> {
                    PayslipResponseDto dto = new PayslipResponseDto();
                    dto.setId(p.getId());
                    dto.setBasic(p.getBasic());
                    dto.setSalaryMonth(p.getSalaryMonth());
                    dto.setHra(p.getHra());
                    dto.setNetSalary(p.getNetSalary());
                    dto.setDeduction(p.getDeduction());
                    dto.setDownloadUrl(baseUrl + "/api/payslips/download/" + p.getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Payslip getPayslipById(Long payslipId) {
      return payslipRepository.findById(payslipId).orElseThrow(()-> new PayslipNotFoundException("payslip not found"));
    }

}
