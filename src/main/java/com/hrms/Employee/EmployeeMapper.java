package com.hrms.Employee;
import com.hrms.Entity.Employee;
import com.hrms.Entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {


    public EmployeeDto toDto(Employee employee) {

        if (employee == null) {
            return null;
        }
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setPassword(employee.getPassword());
        dto.setDepartment(employee.getDepartment());
        dto.setRole(employee.getRole());
        dto.setDateOfJoining(employee.getDateOfJoining());

        return dto;
    }


    public Employee toEntity(EmployeeDto dto){
        if(dto==null){
            return null;
        }
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setPassword(dto.getPassword());
        employee.setDepartment(dto.getDepartment());
        employee.setRole(employee.getRole());
        employee.setDateOfJoining(dto.getDateOfJoining());

        return employee;
    }


}
