package com.diceprojects.msvcauthorization.controllers;

import com.diceprojects.msvcauthorization.persistences.models.dtos.CustomUserDetailsDTO;
import com.diceprojects.msvcauthorization.persistences.models.dtos.UserDTO;
import com.diceprojects.msvcauthorization.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
    public Mono<CustomUserDetailsDTO> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    /**
     * Crea un nuevo usuario con el nombre de usuario y contraseña especificados.
     * Luego, asigna el rol por defecto al usuario creado.
     *
     * @param userDTO el objeto que contiene los detalles del nuevo usuario
     * @return un {@link Mono} que emite los detalles del usuario creado
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomUserDetailsDTO> createUser(@RequestBody UserDTO userDTO) {
        return userService.registerUser(userDTO.getUsername(), userDTO.getPassword());
    }

    /**
     * Asigna un rol a un usuario existente.
     *
     * @param username el nombre de usuario al que se le asignará el rol
     * @param roleId el ID del rol a asignar
     * @return un {@link Mono} que emite los detalles del usuario con el rol asignado
     */
    @PostMapping("/assign-role")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CustomUserDetailsDTO> assignRoleToUser(@RequestParam String username, @RequestParam String roleId) {
        return userService.assignRoleToUser(username, roleId);
    }

    /**
     * Actualiza el token de seguridad de un usuario.
     *
     * @param userId el ID del usuario.
     * @param token el nuevo token de seguridad.
     * @return un {@link Mono} que emite los detalles del usuario actualizado.
     */
    @PutMapping("/updateToken/{userId}")
    public Mono<CustomUserDetailsDTO> updateUserToken(@PathVariable String userId, @RequestParam String token) {
        return userService.updateUserToken(userId, token);
    }

    /**
     * Obtiene los detalles de un usuario por su ID.
     *
     * @param userId el ID del usuario a buscar.
     * @return un {@link Mono} que emite los detalles del usuario encontrado.
     */
    @GetMapping("/findById/{userId}")
    public Mono<CustomUserDetailsDTO> getUserById(@PathVariable String userId) {
        return userService.findById(userId);
    }
}

