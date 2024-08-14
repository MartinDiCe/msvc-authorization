package com.diceprojects.msvcauthorization.persistences.models.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO que representa un rol en el sistema.
 */
@Getter
@Setter
public class RoleDTO {

    private String id;
    private String role;
    private String status;

    public RoleDTO(String id, String role, String status) {
        this.id = id;
        this.role = role;
        this.status = status;
    }

}
