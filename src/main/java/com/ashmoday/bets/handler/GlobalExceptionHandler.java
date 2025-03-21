package com.ashmoday.bets.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.ashmoday.bets.handler.BusinessErrorCodes.USER_EXISTS;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleException(DataIntegrityViolationException exp)
    {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(USER_EXISTS.getCode())
                                .businessErrorDescription(USER_EXISTS.getDescription())
                                .error(USER_EXISTS.getDescription())
                                .build()
                );

    }
}
