package com.project.finance.exception;

import com.project.finance.exception.impl.NoCompanyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = AbstractException.class)
    protected ResponseEntity<?> handleCustomException(AbstractException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code(e.getStatusCode())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(e.getStatusCode()).body(response);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    protected ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .message("권한이 필요합니다.")
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
