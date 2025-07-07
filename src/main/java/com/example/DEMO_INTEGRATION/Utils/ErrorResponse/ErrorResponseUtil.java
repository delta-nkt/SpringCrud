package com.example.DEMO_INTEGRATION.Utils.ErrorResponse;

import com.example.DEMO_INTEGRATION.Utils.Logger.LoggerUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Generic Error Response Utility for standardized error handling in Spring Boot applications.
 * Covers all common Spring Boot exceptions, custom errors, single/multiple errors, and detailed logging.
 */
@Component
public class ErrorResponseUtil {

    /**
     * Represents a single error with detailed information.
     */
    public static class ErrorDetail {
        private String code;
        private String message;
        private String field;
        private LocalDateTime timestamp;

        public ErrorDetail(String code, String message, String field) {
            this.code = code;
            this.message = message;
            this.field = field;
            this.timestamp = LocalDateTime.now();
        }

        // Getters
        public String getCode() { return code; }
        public String getMessage() { return message; }
        public String getField() { return field; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }

    /**
     * Represents the error response structure.
     */
    public static class ErrorResponse {
        private String status;
        private int statusCode;
        private List<ErrorDetail> errors;
        private LocalDateTime timestamp;

        public ErrorResponse(HttpStatus status, List<ErrorDetail> errors) {
            this.status = status.getReasonPhrase();
            this.statusCode = status.value();
            this.errors = errors != null ? errors : new ArrayList<>();
            this.timestamp = LocalDateTime.now();
        }

        // Getters
        public String getStatus() { return status; }
        public int getStatusCode() { return statusCode; }
        public List<ErrorDetail> getErrors() { return errors; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }

    /**
     * Creates a ResponseEntity for a single error.
     * @param status HTTP status.
     * @param code Error code.
     * @param message Error message.
     * @param field Affected field (optional).
     * @return ResponseEntity with ErrorResponse.
     */
    public static ResponseEntity<ErrorResponse> createErrorResponse(
            HttpStatus status, String code, String message, String field) {
        List<ErrorDetail> errors = new ArrayList<>();
        errors.add(new ErrorDetail(code, message, field));
        ErrorResponse errorResponse = new ErrorResponse(status, errors);
        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Creates a ResponseEntity for multiple errors.
     * @param status HTTP status.
     * @param errors List of ErrorDetail objects.
     * @return ResponseEntity with ErrorResponse.
     */
    public static ResponseEntity<ErrorResponse> createErrorResponse(
            HttpStatus status, List<ErrorDetail> errors) {
        ErrorResponse errorResponse = new ErrorResponse(status, errors);
        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Creates a custom error response with developer-configured details.
     * @param status HTTP status.
     * @param code Custom error code.
     * @param message Custom error message.
     * @param field Affected field (optional).
     * @param logger LoggerUtil instance for logging.
     * @param context Additional logging context.
     * @return ResponseEntity with ErrorResponse.
     */
    public static ResponseEntity<ErrorResponse> createCustomErrorResponse(
            HttpStatus status, String code, String message, String field,
            LoggerUtil logger, Map<String, String> context) {
        logger.log(LoggerUtil.LogLevel.ERROR, "Custom error: " + message, null, context);
        return createErrorResponse(status, code, message, field);
    }

    /**
     * Handles all common Spring Boot exceptions and custom exceptions with detailed logging.
     * @param ex Exception to handle.
     * @param logger LoggerUtil instance for logging.
     * @return ResponseEntity with ErrorResponse.
     */
    public static ResponseEntity<ErrorResponse> handleException(Exception ex, LoggerUtil logger) {
        HttpStatus status;
        String code;
        String message;
        List<ErrorDetail> errors = new ArrayList<>();
        Map<String, String> context = Map.of("exception", ex.getClass().getSimpleName());

        if (ex instanceof MethodArgumentNotValidException validationEx) {
            // Handles @Valid annotation failures
            status = HttpStatus.BAD_REQUEST;
            code = "VALIDATION_ERROR";
            for (FieldError fieldError : validationEx.getBindingResult().getFieldErrors()) {
                errors.add(new ErrorDetail(
                        code,
                        fieldError.getDefaultMessage(),
                        fieldError.getField()
                ));
            }
            message = "Validation failed for request";
        } else if (ex instanceof ConstraintViolationException constraintEx) {
            // Handles javax.validation constraints
            status = HttpStatus.BAD_REQUEST;
            code = "CONSTRAINT_VIOLATION";
            constraintEx.getConstraintViolations().forEach(violation ->
                    errors.add(new ErrorDetail(
                            code,
                            violation.getMessage(),
                            violation.getPropertyPath().toString()
                    ))
            );
            message = "Constraint violation in request";
        } else if (ex instanceof MissingServletRequestParameterException paramEx) {
            // Handles missing request parameters
            status = HttpStatus.BAD_REQUEST;
            code = "MISSING_PARAMETER";
            message = "Missing parameter: " + paramEx.getParameterName();
            errors.add(new ErrorDetail(code, message, paramEx.getParameterName()));
        } else if (ex instanceof MethodArgumentTypeMismatchException typeEx) {
            // Handles type mismatch in request parameters
            status = HttpStatus.BAD_REQUEST;
            code = "TYPE_MISMATCH";
            message = "Invalid parameter type for: " + typeEx.getName();
            errors.add(new ErrorDetail(code, message, typeEx.getName()));
        } else if (ex instanceof NoResourceFoundException resourceEx) {
            // Handles 404 Not Found for resources
            status = HttpStatus.NOT_FOUND;
            code = "RESOURCE_NOT_FOUND";
            message = "Resource not found: " + resourceEx.getResourcePath();
            errors.add(new ErrorDetail(code, message, null));
        } else if (ex instanceof ResponseStatusException statusEx) {
            // Handles exceptions with @ResponseStatus
            status = HttpStatus.valueOf(statusEx.getStatusCode().value());
            code = "RESPONSE_STATUS_ERROR";
            message = statusEx.getReason() != null ? statusEx.getReason() : "Error occurred";
            errors.add(new ErrorDetail(code, message, null));
        } else if (ex instanceof IllegalArgumentException illegalArgEx) {
            // Handles invalid arguments
            status = HttpStatus.BAD_REQUEST;
            code = "INVALID_INPUT";
            message = illegalArgEx.getMessage();
            errors.add(new ErrorDetail(code, message, null));
        } else if (ex instanceof NullPointerException) {
            // Handles null pointer exceptions
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            code = "NULL_POINTER";
            message = "Unexpected null value encountered";
            errors.add(new ErrorDetail(code, message, null));
        } else if (ex instanceof RuntimeException runtimeEx) {
            // Handles other runtime exceptions
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            code = "RUNTIME_ERROR";
            message = runtimeEx.getMessage() != null ? runtimeEx.getMessage() : "Unexpected runtime error";
            errors.add(new ErrorDetail(code, message, null));
        } else {
            // Fallback for unhandled exceptions
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            code = "GENERIC_ERROR";
            message = ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred";
            errors.add(new ErrorDetail(code, message, null));
        }

        // Log the error with context
        logger.log(LoggerUtil.LogLevel.ERROR, message, ex, null, context);

        return createErrorResponse(status, errors);
    }


}