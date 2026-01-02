package com.example.ticket.api.error;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    public String code;
    public String message;
    public String path;
    public String requestId;
    public LocalDateTime timestamp = LocalDateTime.now();
    public List<FieldError> errors;

    public static class FieldError {
        public String field;
        public String reason;

        public FieldError(String field, String reason) {
            this.field = field;
            this.reason = reason;
        }
    }

    public static ErrorResponse of(String code, String message) {
        ErrorResponse r = new ErrorResponse();
        r.code = code;
        r.message = message;
        return r;
    }

    public static ErrorResponse of(String code, String message, List<FieldError> errors) {
        ErrorResponse r = of(code, message);
        r.errors = errors;
        return r;
    }

    public static ErrorResponse of(String code, String message, String path, String requestId) {
        ErrorResponse r = of(code, message);
        r.path = path;
        r. requestId = requestId;
        return r;
    }

    public static ErrorResponse of(String code, String message, List<FieldError> errors, String path, String requestId) {
        ErrorResponse r = of(code, message, errors);
        r.path = path;
        r.requestId = requestId;
        return r;
    }
}
