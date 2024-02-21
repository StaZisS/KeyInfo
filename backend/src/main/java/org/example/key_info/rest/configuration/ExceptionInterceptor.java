package org.example.key_info.rest.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionInterceptor extends ResponseEntityExceptionHandler implements AuthenticationEntryPoint {
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
    public ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
        final String exceptionBody = "Internal server error";
        return handleExceptionInternal(ex, exceptionBody, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println("{ \"error\": \"" + authException.getMessage() + "\" }");
    }

    @ExceptionHandler(value = { AccessDeniedException.class })
    public void commence(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex ) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println("{ \"error\": \"" + ex.getMessage() + "\" }");
    }
}
