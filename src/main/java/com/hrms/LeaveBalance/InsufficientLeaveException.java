package com.hrms.LeaveBalance;

public class InsufficientLeaveException extends RuntimeException {
    public InsufficientLeaveException(String message) {
        super(message);
    }
}
