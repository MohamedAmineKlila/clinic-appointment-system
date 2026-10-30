package com.amine.clinic.exception;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        String timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldErrorDetail> errors
) {

    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(Instant.now().toString(), status, error, message, path, null);
    }

    public static ErrorResponse of(int status, String error, String message, String path, List<FieldErrorDetail> errors) {
        return new ErrorResponse(Instant.now().toString(), status, error, message, path, errors);
    }

    public record FieldErrorDetail(String field, String message) {
    }
}
