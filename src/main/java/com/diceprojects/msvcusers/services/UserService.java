package com.diceprojects.msvcusers.services;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

/**
 * Servicio para gestionar la obtención de detalles del usuario de forma reactiva.
 * Extiende {@link ReactiveUserDetailsService} para proporcionar métodos específicos
 * para cargar objetos {@link UserDetails} basados en nombres de usuario.
 */
public interface UserService extends ReactiveUserDetailsService {

    /**
     * Encuentra un usuario por su nombre de usuario y retorna sus detalles como un {@link Mono} de {@link UserDetails}.
     * Este método es usado por Spring Security durante el proceso de autenticación para cargar los detalles necesarios del usuario.
     *
     * @param username el nombre de usuario del usuario a buscar.
     * @return un {@link Mono} que, al subscribirse, proporciona los {@link UserDetails} del usuario buscado.
     *         Retorna un {@link Mono} vacío si el usuario no se encuentra.
     */
    Mono<UserDetails> findByUsername(String username);
}
