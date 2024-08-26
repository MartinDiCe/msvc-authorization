package com.diceprojects.msvcauthorization.services;

import com.diceprojects.msvcauthorization.persistences.models.dtos.CustomUserDetailsDTO;
import reactor.core.publisher.Mono;

/**
 * Interfaz de servicio para gestionar los detalles del usuario de forma reactiva.
 * <p>
 * Proporciona métodos para la creación, búsqueda, actualización y gestión de usuarios,
 * incluyendo la codificación de contraseñas y la asignación de roles.
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
     * Crea un nuevo usuario con el nombre de usuario y contraseña dados.
     *
     * @param username el nombre de usuario del nuevo usuario.
     * @param password la contraseña del nuevo usuario.
     * @return un {@link Mono} que emite los {@link CustomUserDetailsDTO} del usuario creado.
     */
    Mono<CustomUserDetailsDTO> create(String username, String password);

    /**
     * Registra un nuevo usuario con los roles especificados.
     *
     * @param username el nombre de usuario.
     * @param password la contraseña del usuario.
     * @return un {@link Mono} que emite los detalles del usuario creado.
     */
    Mono<CustomUserDetailsDTO> registerUser(String username, String password);

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

    /**
     * Asigna un rol existente a un usuario.
     *
     * @param username el nombre de usuario al que se le asignará el rol.
     * @param roleId   el ID del rol que se asignará al usuario.
     * @return un {@link Mono} que emite los detalles del usuario actualizado.
     */
    Mono<CustomUserDetailsDTO> assignRoleToUser(String username, String roleId);

}
