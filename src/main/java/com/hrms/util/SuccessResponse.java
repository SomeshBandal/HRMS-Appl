package com.hrms.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor

public class SuccessResponse{

    private HttpStatus code;
    private String status;
    private Object data;
}
