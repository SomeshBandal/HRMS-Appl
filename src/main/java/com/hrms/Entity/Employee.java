package com.hrms.Entity;

import com.hrms.Entity.Enum.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String department;

    @Enumerated(EnumType.STRING)
    private Role role; // HR or EMPLOYEE
    private LocalDate dateOfJoining;
    private String password; // store hashed

    @OneToMany(mappedBy = "employee")
    private List<Document> documents;

    @OneToMany(mappedBy = "employee")
    private List<Attendance> attendances;

    @OneToMany(mappedBy = "employee")
    private List<Payslip> payslips;

    @OneToMany(mappedBy = "employee")
    private List<LeaveRequest> leaves;
}
