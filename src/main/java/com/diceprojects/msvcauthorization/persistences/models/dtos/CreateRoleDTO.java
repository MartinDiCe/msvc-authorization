package com.diceprojects.msvcauthorization.persistences.models.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para la creación de roles.
 * Este objeto es utilizado para transferir los datos necesarios para la creación de un rol en el sistema.
 */
@Getter
@Setter
public class CreateRoleDTO {

    private String roleName;
    private String description;

    /**
     * Constructor para inicializar todos los campos del CreateRoleDTO.
     *
     * @param roleName    El nombre del rol.
     * @param description La descripción del rol.
     */
    public CreateRoleDTO(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }
}
