package com.diceprojects.msvcusers.persistences.models.mappers;

import com.diceprojects.msvcusers.persistences.models.entities.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

/**
 * Transforma una entidad de usuario en un objeto UserDetails requerido por Spring Security.
 * Este método se utiliza para convertir un usuario de la base de datos en un formato que
 * Spring Security puede utilizar para realizar tareas de autenticación y autorización.
 *
 * @param user La entidad de usuario obtenida de la base de datos.
 * @return Un objeto UserDetails que representa los detalles de seguridad del usuario.
 */
private UserDetails mapToUserDetails(User user) {
    return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            new ArrayList<>()
    );
}
