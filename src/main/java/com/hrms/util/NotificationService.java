package com.hrms.util;

import com.hrms.Employee.EmployeeRepository;
import com.hrms.Entity.Employee;
import com.hrms.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;
    private final EmployeeRepository employeeRepo;// to get manager info
    private final RoleRepository roleRepository;



    public void notifyManager(Long employeeId, String reason) {
        // Fetch employee
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Get manager email
        roleRepository.findByName();
        if (employee.getEmail() == null) {
            throw new RuntimeException("manager email not found");
        }

        String subject = "Leave Request Notification";
        String message = String.format("Employee %s (%d) has applied for leave.\nReason: %s",
                                            employee.getName(), employee.getId(), reason);

        sendEmail(employee.getEmail(), subject, message);
    }

    private void sendEmail(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("someshbandal2108@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
