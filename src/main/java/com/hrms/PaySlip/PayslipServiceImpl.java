package com.hrms.PaySlip;

import com.hrms.Employee.EmployeeDto;
import com.hrms.Employee.EmployeeNotFoundException;
import com.hrms.Employee.EmployeeRepository;
import com.hrms.Entity.Employee;
import com.hrms.Entity.Payslip;
import com.hrms.util.SecurityUtil;
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
        Employee employee= employeeRepository.findById(employeeId)
                .orElseThrow(()->new EmployeeNotFoundException("Employee not found"));

        Payslip payslip = new Payslip();
        payslip.setEmployee(employee);
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
        Long loggedInEmployeeId = Long.valueOf(SecurityUtil.getLoggedInEmployeeId());

        if (!loggedInEmployeeId.equals(employeeId)){
            throw new UnauthorizedPayslipAccessException("You are not allowed to view payslips of other employees");
        }

        List<PayslipDto> payslips = payslipRepository.findByEmployeeId(employeeId).stream().map(payslipMapper::toDto).toList();

        if (payslips.isEmpty()){
            throw new PayslipNotFoundException("Payslip not found for employeeId :" + loggedInEmployeeId);
        }

        return payslips.stream()
                    .map(payslip -> {
                        PayslipResponseDto dto = new PayslipResponseDto();
                        dto.setId(payslip.getId());
                        dto.setSalaryMonth(payslip.getSalaryMonth());
                        dto.setDownloadUrl(baseUrl + "/api/download/" + payslip.getId());
                        return dto;
                    })
                    .collect(Collectors.toList());
        }



    @Override
    public Payslip getPayslipById(Long payslipId) {
        return payslipRepository.findById(payslipId).orElseThrow(() -> new PayslipNotFoundException("payslip not found"));
    }

}
