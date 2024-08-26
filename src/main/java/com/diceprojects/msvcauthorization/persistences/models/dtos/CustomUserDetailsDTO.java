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
public class CustomUserDetailsDTO {

    private String id;
    private String username;
    private String password;
    private String status;
    private Set<RoleDTO> roles;

    /**
     * Constructor que inicializa todos los campos del DTO de usuario.
     *
     * @param id       el ID del usuario.
     * @param username el nombre de usuario.
     * @param password la contraseña del usuario.
     * @param status   el estado del usuario (por ejemplo, "Active", "Inactive").
     * @param roles    el conjunto de roles asociados al usuario.
     */
    public CustomUserDetailsDTO(String id, String username, String password, String status, Set<RoleDTO> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.roles = roles;
    }

}
