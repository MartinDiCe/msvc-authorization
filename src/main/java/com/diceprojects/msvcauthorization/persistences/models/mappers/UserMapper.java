package com.diceprojects.msvcauthorization.persistences.models.mappers;

import com.diceprojects.msvcauthorization.persistences.models.dtos.CustomUserDetailsDTO;
import com.diceprojects.msvcauthorization.persistences.models.dtos.RoleDTO;
import com.diceprojects.msvcauthorization.persistences.models.entities.Role;
import com.diceprojects.msvcauthorization.persistences.models.entities.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Clase que proporciona métodos para transformar entidades User a CustomUserDetailsDTO y para crear nuevas instancias de User.
 */
@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Transforma una entidad de usuario y un conjunto de roles en un objeto CustomUserDetailsDTO.
     *
     * @param user  La entidad de usuario obtenida de la base de datos.
     * @param roles El conjunto de roles asociados al usuario.
     * @return Un objeto CustomUserDetailsDTO que representa los detalles del usuario.
     */
    public CustomUserDetailsDTO mapToUserDetails(User user, Set<Role> roles) {
        Set<RoleDTO> roleDTOs = roles != null ? roles.stream()
                .map(role -> new RoleDTO(role.getId(), role.getRole(), role.getStatus()))
                .collect(Collectors.toSet()) : Set.of(); // Manejo de roles vacíos

        return new CustomUserDetailsDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getStatus(),
                roleDTOs
        );
    }

    /**
     * Transforma una entidad User a un objeto CustomUserDetailsDTO sin roles.
     *
     * @param user La entidad de usuario obtenida de la base de datos.
     * @return Un objeto CustomUserDetailsDTO que representa los detalles del usuario sin roles.
     */
    public CustomUserDetailsDTO mapToUserDetails(User user) {
        return new CustomUserDetailsDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getStatus(),
                Set.of()
        );
    }

    /**
     * Crea e inicializa una nueva instancia de User.
     *
     * @param username El nombre de usuario.
     * @param password La contraseña del usuario.
     * @param status   El estado del usuario.
     * @return Una instancia inicializada de User.
     */
    public User createNewUser(String username, String password, String status) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(status);
        user.setDeleted(false);
        user.setCreateDate(new Date());
        user.setForcePasswordChange(true);
        return user;
    }
}
