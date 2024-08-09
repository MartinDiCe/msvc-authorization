package com.diceprojects.msvclogin.controllers;

import com.diceprojects.msvclogin.persistences.models.entities.Role;
import com.diceprojects.msvclogin.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * Controlador para manejar las solicitudes relacionadas con los usuarios.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Obtiene los detalles de un usuario por su nombre de usuario.
     *
     * @param username el nombre de usuario del usuario a buscar
     * @return un {@link Mono} que emite los detalles del usuario encontrado
     */
    @GetMapping("/{username}")
    public Mono<UserDetails> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    /**
     * Crea un nuevo usuario con el nombre de usuario, contraseña y roles especificados.
     *
     * @param username el nombre de usuario del nuevo usuario
     * @param password la contraseña del nuevo usuario
     * @param roles    los roles asignados al nuevo usuario
     * @return un {@link Mono} que emite los detalles del usuario creado
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserDetails> createUser(@RequestParam String username, @RequestParam String password, @RequestParam Set<Role> roles) {
        return userService.createUser(username, password, roles);
    }
}
