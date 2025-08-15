package com.hrms.PaySlip;

import com.hrms.Entity.Employee;
import com.hrms.Entity.Payslip;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PayslipService {

    public PayslipDto generatePayslip(PayslipDto payslipDto, Long employeeId, MultipartFile file) throws IOException;
    public List<PayslipDto> getAllPayslips(Long employeeID);
    public List<PayslipResponseDto> getAllPayslipsWithDownloadLinks(Long employeeId, String baseUrl);

    Payslip getPayslipById(Long payslipId);
}
