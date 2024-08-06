package com.diceprojects.msvcusers.persistences.models.mappers;

import com.diceprojects.msvcusers.persistences.models.entities.Role;
import com.diceprojects.msvcusers.persistences.models.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Clase que proporciona métodos para transformar entidades User a UserDetails.
 */
@Component
public class UserMapper {

    /**
     * Transforma una entidad de usuario en un objeto UserDetails requerido por Spring Security.
     * Este método se utiliza para convertir un usuario de la base de datos en un formato que
     * Spring Security puede utilizar para realizar tareas de autenticación y autorización.
     *
     * @param user La entidad de usuario obtenida de la base de datos.
     * @return Un objeto UserDetails que representa los detalles de seguridad del usuario.
     */
    public UserDetails mapToUserDetails(User user, Set<Role> roles) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                roles.stream().map(role -> new org.springframework.security.core.GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return role.getRole();
                    }
                }).collect(Collectors.toList())
        );
    }
}
