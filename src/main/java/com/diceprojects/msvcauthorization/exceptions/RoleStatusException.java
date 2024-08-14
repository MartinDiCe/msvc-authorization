package com.diceprojects.msvcauthorization.exceptions;

/**
 * Excepción personalizada que se lanza cuando se intenta cambiar el estado de un rol
 * al mismo estado que ya tiene.
 */
public class RoleStatusException extends RuntimeException {

    /**
     * Crea una nueva instancia de {@code RoleStatusException} con el mensaje especificado.
     *
     * @param message el mensaje detallado de la excepción
     */
    public RoleStatusException(String message) {
        super(message);
    }
}
