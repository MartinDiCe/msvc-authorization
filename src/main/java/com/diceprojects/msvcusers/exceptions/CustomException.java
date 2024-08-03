package com.diceprojects.calculationsservice.exceptions;

/**
 * CustomException is a custom runtime exception class that extends RuntimeException.
 * It is used throughout the application to represent exceptions that occur due to
 * specific business logic or operational failures, providing more context than a
 * standard runtime exception.
 */
public class CustomException extends RuntimeException {

    /**
     * Constructs a new CustomException with the specified detail message.
     * The detail message is saved for later retrieval by the Throwable.getMessage() method.
     *
     * @param message the detail message. The detail message is saved for later retrieval
     *                by the Throwable.getMessage() method.
     */
    public CustomException(String message) {
        super(message);
    }

    /**
     * Constructs a new CustomException with the specified detail message and cause.
     * Note that the detail message associated with cause is not automatically incorporated
     * into this exception's detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval
     *                by the Throwable.getMessage() method.
     * @param cause   the cause (which is saved for later retrieval by the Throwable.getCause() method).
     *                (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}

