package com.diceprojects.msvcusers.services;

import com.diceprojects.msvcusers.persistences.models.entities.Role;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * Interfaz de servicio para gestionar los detalles del usuario de forma reactiva.
 * Extiende {@link ReactiveUserDetailsService} para proporcionar métodos específicos
 * para cargar objetos {@link UserDetails} basados en nombres de usuario.
 */
public interface UserService extends ReactiveUserDetailsService {

    /**
     * Encuentra un usuario por su nombre de usuario y retorna sus detalles como un {@link Mono} de {@link UserDetails}.
     * Este método es utilizado por Spring Security durante el proceso de autenticación para cargar los detalles necesarios del usuario.
     *
     * @param username el nombre de usuario del usuario a buscar.
     * @return un {@link Mono} que emite los {@link UserDetails} del usuario encontrado, o un {@link Mono} vacío si el usuario no es encontrado.
     */
    Mono<UserDetails> findByUsername(String username);

    /**
     * Crea un nuevo usuario con el nombre de usuario, contraseña y roles dados.
     *
     * @param username el nombre de usuario del nuevo usuario.
     * @param password la contraseña del nuevo usuario.
     * @param roles los roles asignados al nuevo usuario.
     * @return un {@link Mono} que emite los {@link UserDetails} del usuario creado.
     */
    Mono<UserDetails> createUser(String username, String password, Set<Role> roles);

    /**
     * Encuentra o crea un usuario con el nombre de usuario, contraseña y nombre del rol dados.
     *
     * @param username el nombre de usuario del usuario a encontrar o crear.
     * @param password la contraseña del usuario a encontrar o crear.
     * @param roleName el nombre del rol a asignar si se crea un nuevo usuario.
     * @return un {@link Mono} que emite los {@link UserDetails} del usuario encontrado o creado.
     */
    Mono<UserDetails> findOrCreateUser(String username, String password, String roleName);
}
