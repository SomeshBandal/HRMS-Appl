package com.hrms.Exception;

import com.hrms.util.BaseResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponseDto>handleBaseException(BaseResponseDto responseDto){

        log.error("Base exception occurred: {}", responseDto.getMessage());

        BaseResponseDto response = BaseResponseDto.builder()
                .code(responseDto.getCode())
                .message(responseDto.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
