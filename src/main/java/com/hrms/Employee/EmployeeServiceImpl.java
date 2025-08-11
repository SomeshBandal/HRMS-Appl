package com.hrms.Employee;

import com.hrms.Entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {

        if (employeeDto == null) {
            throw new IllegalArgumentException("employee data cannot be null");
        }
        if(employeeRepository.existsByEmail(employeeDto.getEmail())){
            throw new EmployeeAlreadyExistsException("Employee already exists");
        }
        employeeDto.setPassword(passwordEncoder.encode(employeeDto.getPassword()));

        Employee entity = employeeMapper.toEntity(employeeDto);
        Employee savedEmployee = employeeRepository.save(entity);

        return employeeMapper.toDto(savedEmployee);

    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDto getEmployeeById(Long id) {
        return employeeRepository.findById(id).map(employeeMapper::toDto).orElseThrow(
                () -> new EmployeeNotFoundException("Employee not found with id :" + id));

    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll().stream().map(employeeMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        EmployeeDto employee = employeeRepository.findById(id).map(employeeMapper::toDto)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        if (employee.getName() != null) {
            employee.setName(employeeDto.getName());
        }
        if (employee.getEmail() != null) {
            employee.setEmail(employeeDto.getEmail());
        }
        if (employee.getDepartment() != null) {
            employee.setDepartment(employeeDto.getDepartment());
        }
        if (employee.getDateOfJoining() != null) {
            employee.setDateOfJoining(employeeDto.getDateOfJoining());
        }
        if (employee.getPassword() != null) {
            employee.setPassword(employeeDto.getPassword());
        }
        if (employee.getRole() != null) {
            employee.setRole(employeeDto.getRole());
        }

        Employee entity = employeeMapper.toEntity(employee);
        Employee savedEmployee = employeeRepository.save(entity);
        return employeeMapper.toDto(savedEmployee);

    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("employee does not exist"));

        employeeRepository.delete(employee);
    }

    @Override
    public EmployeeDto ownProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Employee employee = employeeRepository.findByEmail(email).orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
        return employeeMapper.toDto(employee);
    }
}