package com.hrms.Entity;

import com.hrms.Entity.Enum.LeaveType;
import jakarta.persistence.*;
import lombok.*;

@Entity
    @Table(name = "leave_balance")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class LeaveBalance {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private LeaveType leaveType; // Example: PAID, UNPAID, SICK, etc.

        @Column(nullable = false)
        private int totalLeaves; // e.g., 12 paid leaves annually

        @Column(nullable = false)
        private int usedLeaves; // how many leaves already used

        @Column(nullable = false)
        private int remainingLeaves; // = total - used

        // Relationship with Employee
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "employee_id", nullable = false)
        private Employee employee;
    }

