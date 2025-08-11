package com.hrms.Employee;

import com.hrms.Entity.Employee;
import com.hrms.Entity.Enum.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);
    boolean existsByRole(String role);
    Employee findByRole(Role role);
    boolean existsByEmail(String email);

}
