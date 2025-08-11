package com.hrms.Controller;

import com.hrms.Employee.EmployeeRepository;
import com.hrms.Entity.Employee;
import com.hrms.Entity.Enum.Role;
import com.hrms.JWT.JwtResponse;
import com.hrms.JWT.JwtServiceImpl;
import com.hrms.JWT.JwtRequest;
import com.hrms.Security.CustomUserDetails;
import com.hrms.Security.CustomUserDetailsService;
import com.hrms.util.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtServiceImpl jwtService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService userDetailsService;


    //    @PreAuthorize("hasAnyRole('EMPLOYEE', 'HR')")
    @PostMapping("/login")
    public JwtResponse login(@RequestBody JwtRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        authenticationManager.authenticate(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token1 = jwtService.generateToken((CustomUserDetails) userDetails);
        return new JwtResponse(token1);

    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        if (employeeRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        Employee employee = new Employee();
        employee.setName(registerRequest.getName());
        employee.setEmail(registerRequest.getEmail());
        employee.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        employee.setRole(Role.valueOf(registerRequest.getRole().toUpperCase())); // e.g., ADMIN, HR, EMPLOYEE
        employeeRepository.save(employee);

        return ResponseEntity.ok("User registered successfully");
    }
}