package com.diceprojects.msvcauthorization.services;

import com.diceprojects.msvcauthorization.persistences.models.dtos.CustomUserDetailsDTO;
import com.diceprojects.msvcauthorization.persistences.models.entities.Role;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * Interfaz de servicio para gestionar los detalles del usuario de forma reactiva.
 */
public interface UserService {

    /**
     * Encuentra un usuario por su nombre de usuario y retorna sus detalles como un {@link Mono} de {@link CustomUserDetailsDTO}.
     * Este método es utilizado para cargar los detalles necesarios del usuario.
     *
     * @param username el nombre de usuario del usuario a buscar.
     * @return un {@link Mono} que emite los {@link CustomUserDetailsDTO} del usuario encontrado, o un {@link Mono} vacío si el usuario no es encontrado.
     */
    Mono<CustomUserDetailsDTO> findByUsername(String username);

    /**
     * Crea un nuevo usuario con el nombre de usuario, contraseña y roles dados.
     *
     * @param username el nombre de usuario del nuevo usuario.
     * @param password la contraseña del nuevo usuario.
     * @param roles los roles asignados al nuevo usuario.
     * @return un {@link Mono} que emite los {@link CustomUserDetailsDTO} del usuario creado.
     */
    Mono<CustomUserDetailsDTO> createUser(String username, String password, Set<Role> roles);

    /**
     * Encuentra o crea un usuario con el nombre de usuario, contraseña y nombre del rol dados.
     *
     * @param username el nombre de usuario del usuario a encontrar o crear.
     * @param password la contraseña del usuario a encontrar o crear.
     * @param roleName el nombre del rol a asignar si se crea un nuevo usuario.
     * @return un {@link Mono} que emite los {@link CustomUserDetailsDTO} del usuario encontrado o creado.
     */
    Mono<CustomUserDetailsDTO> findOrCreateUser(String username, String password, String roleName);

    /**
     * Actualiza el token de seguridad de un usuario.
     *
     * @param userId el ID del usuario.
     * @param token el nuevo token de seguridad.
     * @return un {@link Mono} que emite los detalles del usuario actualizado.
     */
    Mono<CustomUserDetailsDTO> updateUserToken(String userId, String token);

    /**
     * Busca un usuario por su ID.
     *
     * @param userId el ID del usuario a buscar.
     * @return un {@link Mono} que emite los detalles del usuario encontrado.
     */
    Mono<CustomUserDetailsDTO> findById(String userId);
}
