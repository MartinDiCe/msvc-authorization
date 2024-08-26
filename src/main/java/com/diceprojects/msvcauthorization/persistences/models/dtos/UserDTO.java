package com.diceprojects.msvcauthorization.persistences.models.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * DTO personalizado que representa los detalles del usuario.
 * <p>
 * Esta clase contiene la información básica de un usuario, como su ID, nombre de usuario,
 * contraseña, estado y roles asociados. También incluye métodos para verificar si la cuenta
 * del usuario está activa y si no ha expirado.
 */
@Getter
@Setter
public class UserDTO {

    private String username;
    private String password;

    /**
     * Constructor que inicializa todos los campos del DTO de usuario.
     *
     * @param username el nombre de usuario.
     * @param password la contraseña del usuario.
     */
    public UserDTO(String username, String password) {

        this.username = username;
        this.password = password;
    }
}