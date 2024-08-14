package com.diceprojects.msvcauthorization.exceptions;

/**
 * Excepción personalizada que se lanza cuando un usuario no es encontrado.
 * <p>
 * Esta excepción extiende de {@link RuntimeException} y se utiliza para indicar que
 * un usuario solicitado no pudo ser encontrado en el sistema.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructor que crea una nueva instancia de {@link UserNotFoundException} con un mensaje detallado.
     *
     * @param message el mensaje que describe la razón por la cual el usuario no fue encontrado.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
