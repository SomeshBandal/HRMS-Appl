package com.hrms.PaySlip;

public class PayslipNotFoundException extends RuntimeException {
    public PayslipNotFoundException(String message) {
        super(message);
    }
}
