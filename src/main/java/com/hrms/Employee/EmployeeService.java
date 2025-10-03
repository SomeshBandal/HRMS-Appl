package com.hrms.Employee;

import com.hrms.Entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    EmployeeDto saveEmployee(EmployeeDto employeeDto);
    EmployeeDto getEmployeeById(Long id);
    List<EmployeeDto> getAllEmployees();
    EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto);
    void deactivateEmployee(Long id);
    void activateEmployee(Long id);
    List<EmployeeDto>getActiveEmployees();
    EmployeeDto ownProfile();

}

