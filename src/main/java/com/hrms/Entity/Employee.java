package com.hrms.Entity;

import com.hrms.Entity.Enum.EmployeeStatus;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Long Mobile;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus status = EmployeeStatus.ACTIVE; //default active

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

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
