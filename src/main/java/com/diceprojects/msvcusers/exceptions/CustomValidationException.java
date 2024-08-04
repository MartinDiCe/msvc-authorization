package com.diceprojects.msvcusers.exceptions;

/**
 * CustomValidationException is a custom runtime exception class that extends RuntimeException.
 * It is specifically used to represent validation failures within the application, providing
 * a clear indication that an error occurred due to invalid data or state.
 */
public class CustomValidationException extends RuntimeException {

    /**
     * Constructs a new CustomValidationException with the specified detail message.
     * The detail message is used to provide a specific reason for the validation failure.
     *
     * @param message the detail message which provides specific information about the
     *                validation failure. The detail message is saved for later retrieval
     *                by the Throwable.getMessage() method.
     */
    public CustomValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new CustomValidationException with the specified detail message and cause.
     * This constructor is useful for validation exceptions that are the result of another
     * underlying issue.
     *
     * @param message the detail message which provides specific information about the
     *                validation failure. The detail message is saved for later retrieval
     *                by the Throwable.getMessage() method.
     * @param cause   the cause of the exception, which represents the underlying reason
     *                for the validation failure (saved for later retrieval by the
     *                Throwable.getCause() method). A null value is permitted and indicates
     *                that the cause is nonexistent or unknown.
     */
    public CustomValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
