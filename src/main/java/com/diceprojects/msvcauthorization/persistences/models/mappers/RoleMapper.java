package com.diceprojects.msvcauthorization.persistences.models.mappers;

import com.diceprojects.msvcauthorization.persistences.models.dtos.CreateRoleDTO;
import com.diceprojects.msvcauthorization.persistences.models.entities.Role;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Mapper para transformar objetos entre CreateRoleDTO y Role.
 */
@Component
public class RoleMapper {

    /**
     * Convierte un CreateRoleDTO a una entidad Role.
     *
     * @param createRoleDTO DTO con los datos necesarios para crear un rol.
     * @param activeStatus El estado activo de la entidad.
     * @return La entidad Role construida a partir del DTO.
     */
    public Role mapToRole(CreateRoleDTO createRoleDTO, String activeStatus) {
        Role role = new Role();
        role.setRole(createRoleDTO.getRoleName());
        role.setDescription(createRoleDTO.getDescription());
        role.setCreateDate(ZonedDateTime.now(ZoneId.systemDefault()).toLocalDateTime());
        role.setDeleted(false);
        role.setStatus(activeStatus);
        return role;
    }
}
