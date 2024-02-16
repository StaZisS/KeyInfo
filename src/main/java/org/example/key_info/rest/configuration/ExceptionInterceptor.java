package org.example.key_info.rest.configuration;

import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ExceptionInterceptor extends ResponseEntityExceptionHandler {
    private static final Map<ExceptionType, HttpStatus> STATUS_MAP = Map.of(
            ExceptionType.FATAL, HttpStatus.INTERNAL_SERVER_ERROR,
            ExceptionType.INVALID, HttpStatus.BAD_REQUEST,
            ExceptionType.ALREADY_EXISTS, HttpStatus.BAD_REQUEST,
            ExceptionType.NOT_FOUND, HttpStatus.NOT_FOUND,
            ExceptionType.UNAUTHORIZED, HttpStatus.UNAUTHORIZED,
            ExceptionType.ILLEGAL, HttpStatus.BAD_REQUEST,
            ExceptionType.FORBIDDEN, HttpStatus.FORBIDDEN
    );

    @ExceptionHandler(value = {ExceptionInApplication.class})
    protected ResponseEntity<Object> handleException(ExceptionInApplication ex, WebRequest request) {
        final HttpStatus status = STATUS_MAP.get(ex.getType());

        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), status, request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
        final String exceptionBody = "Internal server error";
        return handleExceptionInternal(ex, exceptionBody, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
