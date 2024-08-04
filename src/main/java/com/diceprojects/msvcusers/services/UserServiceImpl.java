package com.diceprojects.msvcusers.services;

import com.diceprojects.msvcusers.persistences.models.entities.User;
import com.diceprojects.msvcusers.persistences.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

/**
 * Implementación de {@link UserService} que utiliza un repositorio MongoDB para la recuperación de datos del usuario.
 * Proporciona la lógica para cargar los datos de usuario necesarios para la autenticación de Spring Security.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Construye una instancia de {@link UserServiceImpl} con el repositorio necesario para operaciones de usuario.
     *
     * @param userRepository el repositorio para operaciones relacionadas con usuarios.
     */
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Recupera un usuario por su nombre de usuario y lo transforma a {@link UserDetails}.
     * Si el usuario no se encuentra, se lanza una excepción {@link UsernameNotFoundException}.
     *
     * @param username el nombre de usuario del usuario a buscar.
     * @return un {@link Mono} que, al ser resuelto, proporciona los {@link UserDetails} asociados al nombre de usuario.
     */
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::mapToUserDetails)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")));
    }

    /**
     * Convierte una entidad {@link User} a {@link UserDetails} adecuado para ser utilizado por Spring Security.
     *
     * @param user la entidad de usuario obtenida del repositorio.
     * @return un objeto {@link UserDetails} que contiene la información de seguridad del usuario.
     */
    private UserDetails mapToUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}


