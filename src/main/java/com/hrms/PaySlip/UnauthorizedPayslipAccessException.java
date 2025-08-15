package com.hrms.PaySlip;

public class UnauthorizedPayslipAccessException extends RuntimeException {
    public UnauthorizedPayslipAccessException(String message) {
        super(message);
    }
}
