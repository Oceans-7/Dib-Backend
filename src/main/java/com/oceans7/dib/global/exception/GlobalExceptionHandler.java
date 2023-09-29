package com.oceans7.dib.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { ApplicationException.class })
    protected ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException e) {
        log.error("handleApplicationException", e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ErrorResponse.error(e.getErrorCode()));
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.INVALID_VALUE_EXCEPTION;
        String message = e.getBindingResult().getFieldError().getField() +
                "은(는) " + errorCode.getMessage();

        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponse.error(errorCode, message));
    }
}
