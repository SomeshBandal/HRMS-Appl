package com.hrms.EmployeeTest;

import com.hrms.Employee.*;
import com.hrms.Entity.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeDto employeeDto;
    private EmployeeDto employeeDto1;
    private Employee employee;
    private Employee employee1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employeeDto = new EmployeeDto();
        employeeDto.setId(1L);
        employeeDto.setName("Somesh");
        employeeDto.setEmail("somesh1312@gmail.com");
        employeeDto.setPassword("plainPassword");

        employeeDto1 = new EmployeeDto();
        employeeDto1.setId(2L);
        employeeDto1.setName("Arun");
        employeeDto1.setEmail("arun@gmail.com");
        employeeDto1.setPassword("plainPassword");

        employee = new Employee();
        employee.setId(1L);
        employee.setName("Somesh");
        employee.setEmail("somesh@gmail.com");
        employee.setPassword("encodedPassword");

        employee1 = new Employee();
       employee1.setName("Arun");
       employee1.setId(2L);
       employee1.setEmail("arun@gmail.com");
       employee1.setPassword("encodedPassword");
    }

    @Test
    public void saveEmployee(){

        when(employeeRepository.existsByEmail(employeeDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(employeeMapper.toEntity(employeeDto)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toDto(employee)).thenReturn(employeeDto);

        EmployeeDto result = employeeService.saveEmployee(employeeDto);

        Assertions.assertNotNull(result);
        assertEquals("Somesh", result.getName());
        assertEquals("somesh@gmail.com", result.getEmail());

        verify(passwordEncoder, times(1)).encode("plainPassword");
        verify(employeeRepository, times(1)).save(employee);
        verify(employeeMapper,times(1)).toEntity(employeeDto);
        verify(employeeMapper, times(1)).toDto(employee);

    }

    @Test
    public void testForGettingEmployeeById(){

        when(employeeRepository.findById(employeeDto.getId())).thenReturn(Optional.ofNullable(employee));
        when(employeeMapper.toDto(employee)).thenReturn(employeeDto);

        EmployeeDto result = employeeService.getEmployeeById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertSame(1l, result.getId());
        Assertions.assertEquals("Somesh", result.getName());

        // Verify interactions
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeMapper, times(1)).toDto(employee);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        EmployeeNotFoundException exception = assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeById(1L)
        );

        assertEquals("Employee not found with id :2", exception.getMessage());

        // Verify interactions
        verify(employeeRepository, times(1)).findById(1L);
        verifyNoInteractions(employeeMapper);
    }

    @Test
    void testForGettingAllEmployees(){

        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee,employee1));
        when(employeeMapper.toDto(employee)).thenReturn((employeeDto));
        when(employeeMapper.toDto(employee1)).thenReturn(employeeDto1);

        List<EmployeeDto> allEmployees = employeeService.getAllEmployees();

        assertNotNull(allEmployees);
        assertEquals(employee.getName(), employeeDto.getName());
        assertEquals(employee.getId(), employeeDto.getId());
        assertEquals(employee.getPassword(),employee.getPassword());

    }
}


