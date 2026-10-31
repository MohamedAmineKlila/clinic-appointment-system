package com.amine.clinic.exception;

/**
 * Thrown when a request clashes with the current state of an existing
 * resource (e.g. booking a doctor at an exact time that is already BOOKED).
 * Mapped to HTTP 409 Conflict, while validation problems stay on HTTP 400.
 */
public class AppointmentConflictException extends RuntimeException {

    public AppointmentConflictException(String message) {
        super(message);
    }
}
