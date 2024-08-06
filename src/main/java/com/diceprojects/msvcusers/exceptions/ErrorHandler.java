package com.diceprojects.msvcusers.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Clase utilitaria para manejar errores y registrar logs.
 */
public class ErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    /**
     * Maneja errores registrando el mensaje y lanzando una {@link ResponseStatusException}.
     *
     * @param message el mensaje de error
     * @param e       la excepción a manejar
     * @param status  el estado HTTP a retornar
     */
    public static void handleError(String message, Throwable e, HttpStatus status) {
        logger.error(message, e);
        throw new ResponseStatusException(status, message, e);
    }
}
