package com.hrms.Leaves;

import com.hrms.Employee.EmployeeRepository;
import com.hrms.Entity.Employee;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import static com.hrms.Entity.Enum.Role.MANAGER;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;
    private final EmployeeRepository employeeRepo; // to get manager info

    public NotificationService(JavaMailSender mailSender, EmployeeRepository employeeRepo) {
        this.mailSender = mailSender;
        this.employeeRepo = employeeRepo;
    }

    public void notifyManager(Long employeeId, String reason) {
        // Fetch employee
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Get manager email (you may store it in employee/manager table)
        Employee manager = employeeRepo.findByRole(MANAGER);
        if (manager.getEmail() == null) {
            throw new RuntimeException("manager email not found");
        }

        String subject = "Leave Request Notification";
        String message = String.format("Employee %s (%d) has applied for leave.\nReason: %s",
                employee.getName(), employee.getId(), reason);

        sendEmail(manager.getEmail(), subject, message);
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your_email@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
