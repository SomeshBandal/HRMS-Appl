package com.hrms.Employee;

public class EmployeeAlreadyExistsException extends RuntimeException {
    public EmployeeAlreadyExistsException(String msg) {
        super(msg);
    }
}
