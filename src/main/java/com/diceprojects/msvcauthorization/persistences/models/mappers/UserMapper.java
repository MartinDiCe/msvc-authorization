package com.diceprojects.msvcauthorization.persistences.models.mappers;

import com.diceprojects.msvcauthorization.persistences.models.dtos.CustomUserDetailsDTO;
import com.diceprojects.msvcauthorization.persistences.models.dtos.RoleDTO;
import com.diceprojects.msvcauthorization.persistences.models.entities.Role;
import com.diceprojects.msvcauthorization.persistences.models.entities.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Clase que proporciona métodos para transformar entidades User a CustomUserDetailsDTO.
 */
@Component
public class UserMapper {

    /**
     * Transforma una entidad de usuario y un conjunto de roles en un objeto CustomUserDetailsDTO.
     *
     * @param user  La entidad de usuario obtenida de la base de datos.
     * @param roles El conjunto de roles asociados al usuario.
     * @return Un objeto CustomUserDetailsDTO que representa los detalles del usuario.
     */
    public CustomUserDetailsDTO mapToUserDetails(User user, Set<Role> roles) {
        Set<RoleDTO> roleDTOs = roles.stream()
                .map(role -> new RoleDTO(role.getId(), role.getRole(), role.getStatus()))
                .collect(Collectors.toSet());

        return new CustomUserDetailsDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getStatus(),
                roleDTOs
        );
    }
}