package com.backend.meat_home.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Object buildErrorResponse(HttpServletRequest request, String message, HttpStatus status) throws IOException {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", message);
        errorResponse.put("statusCode", status.name());
        errorResponse.put("path", request.getRequestURI());
        errorResponse.put("timestamp", System.currentTimeMillis());

        try {
            return objectMapper.writeValueAsString(errorResponse);
        } catch (Exception e) {
            throw new RuntimeException("Error building JSON response", e);
        }
    }

    @ExceptionHandler(NoSuchElementException.class)
    public Object handleNoSuchElementException(HttpServletRequest request, NoSuchElementException ex) throws IOException {
        return buildErrorResponse(request, ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException ex) throws IOException {
        return buildErrorResponse(request, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public Object handleIllegalStateException(HttpServletRequest request, IllegalStateException ex) throws IOException {
        return buildErrorResponse(request, ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(HttpServletRequest request, RuntimeException ex) throws IOException {
        return buildErrorResponse(request, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
