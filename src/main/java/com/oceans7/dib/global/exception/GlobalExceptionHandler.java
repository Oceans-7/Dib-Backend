package com.oceans7.dib.global.exception;

import com.oceans7.dib.global.response.ApplicationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { ApplicationException.class })
    protected ResponseEntity<ApplicationResponse<ErrorCode>> handleApplicationException(ApplicationException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ApplicationResponse.error(e.getErrorCode()));
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    protected ResponseEntity<ApplicationResponse<ErrorCode>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        return ResponseEntity.status(ErrorCode.NOT_VALID_EXCEPTION.getHttpStatus())
                .body(ApplicationResponse.error(ErrorCode.NOT_VALID_EXCEPTION, e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }
}
