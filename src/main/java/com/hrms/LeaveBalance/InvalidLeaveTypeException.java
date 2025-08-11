package com.hrms.LeaveBalance;

public class InvalidLeaveTypeException extends RuntimeException {
    public InvalidLeaveTypeException(String message) {
        super(message);
    }
}
