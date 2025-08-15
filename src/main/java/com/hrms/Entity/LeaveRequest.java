package com.hrms.Entity;

import com.hrms.Entity.Enum.LeaveStatus;
import com.hrms.Entity.Enum.LeaveType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequest {

    @Id
    @GeneratedValue

    private Long id;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String reason;

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType; // PAID or UNPAID
    private int noOfDays;
    @Enumerated(EnumType.STRING)
    private LeaveStatus status;
    // PENDING, APPROVED, REJECTED
    private boolean createdByHR = false; // true = posted directly by HR

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}
