package com.diceprojects.msvcauthorization.persistences.models.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * DTO que representa los detalles de un usuario en el sistema.
 * Este objeto es utilizado para transferir los datos de un usuario, incluyendo su nombre de usuario,
 * contraseña, estado y roles asociados.
 */
@Getter
@Setter
public class CreateUserDTO {

    private String username;
    private String password;
    private Set<RoleDTO> roles;

    /**
     * Constructor que inicializa todos los campos del CreateUserDTO.
     *
     * @param username El nombre de usuario.
     * @param password La contraseña del usuario.
     * @param roles    Los roles asociados al usuario.
     */
    public CreateUserDTO(String username, String password, Set<RoleDTO> roles) {

        this.username = username;
        this.password = password;
        this.roles = roles;
    }

}
