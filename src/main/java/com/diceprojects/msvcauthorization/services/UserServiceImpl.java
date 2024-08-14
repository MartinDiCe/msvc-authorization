package com.diceprojects.msvcauthorization.services;

import com.diceprojects.msvcauthorization.exceptions.ErrorHandler;
import com.diceprojects.msvcauthorization.persistences.models.dtos.RoleDTO;
import com.diceprojects.msvcauthorization.persistences.models.entities.Role;
import com.diceprojects.msvcauthorization.persistences.models.entities.User;
import com.diceprojects.msvcauthorization.persistences.repositories.UserRepository;
import com.diceprojects.msvcauthorization.persistences.models.dtos.CustomUserDetailsDTO;
import com.diceprojects.msvcauthorization.utils.EntityStatusService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementación de la interfaz {@link UserService} que proporciona servicios de gestión de usuarios.
 * <p>
 * Esta clase maneja la creación, búsqueda, actualización y gestión de usuarios en el sistema,
 * incluyendo la codificación de contraseñas y la asignación de roles. También interactúa con otros
 * microservicios para obtener configuraciones y parámetros necesarios.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final EntityStatusService entityStatusService;

    /**
     * Constructor para inyectar las dependencias necesarias.
     *
     * @param userRepository     el repositorio para gestionar usuarios.
     * @param roleService        el servicio para gestionar roles.
     * @param passwordEncoder    el codificador de contraseñas.
     * @param entityStatusService el servicio para manejar el estado activo de las entidades.
     */
    public UserServiceImpl(UserRepository userRepository, RoleService roleService,
                           @Lazy PasswordEncoder passwordEncoder, EntityStatusService entityStatusService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.entityStatusService = entityStatusService;
    }

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username el nombre de usuario.
     * @return un {@link Mono} que emite los detalles del usuario encontrado.
     */
    @Override
    public Mono<CustomUserDetailsDTO> findByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username)
                .flatMap(user -> {
                    if (user.getStatus() == null || !user.getStatus().equalsIgnoreCase("Active")) {
                        return Mono.empty();
                    }

                    return roleService.findRolesByIds(user.getRoleIds())
                            .collectList()
                            .flatMap(roles -> {
                                Set<RoleDTO> roleDTOs = roles.stream()
                                        .map(role -> new RoleDTO(role.getId(), role.getRole(), role.getStatus()))
                                        .collect(Collectors.toSet());

                                return Mono.just(new CustomUserDetailsDTO(
                                        user.getId(),
                                        user.getUsername(),
                                        user.getPassword(),
                                        user.getStatus(),
                                        roleDTOs
                                ));
                            });
                })
                .switchIfEmpty(Mono.empty())
                .doOnError(e -> ErrorHandler.handleError("Error encontrando usuario por nombre de usuario", e, HttpStatus.NOT_FOUND));
    }

    /**
     * Crea un nuevo usuario con los roles especificados.
     *
     * @param username el nombre de usuario.
     * @param password la contraseña del usuario.
     * @param roles    los roles que se asignarán al usuario.
     * @return un {@link Mono} que emite los detalles del usuario creado.
     */
    @Override
    public Mono<CustomUserDetailsDTO> createUser(String username, String password, Set<Role> roles) {
        return userRepository.findByUsername(username)
                .flatMap(existingUser -> Mono.just(new CustomUserDetailsDTO(
                        existingUser.getId(),
                        existingUser.getUsername(),
                        existingUser.getPassword(),
                        existingUser.getStatus(),
                        roles.stream()
                                .map(role -> new RoleDTO(role.getId(), role.getRole(), role.getStatus()))
                                .collect(Collectors.toSet())
                )))
                .switchIfEmpty(
                        entityStatusService.obtenerEstadoActivo()
                                .flatMap(activeStatus -> roleService.findRolesByIds(roles.stream().map(Role::getId).collect(Collectors.toSet()))
                                        .filter(role -> activeStatus.equalsIgnoreCase(role.getStatus()))  // Filtra solo roles con estado activo
                                        .collectList()
                                        .flatMap(activeRoles -> {
                                            if (activeRoles.isEmpty()) {
                                                return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pueden asignar roles inactivos"));
                                            }

                                            User user = new User();
                                            user.setUsername(username);
                                            user.setPassword(passwordEncoder.encode(password));
                                            user.setStatus(activeStatus);
                                            user.setDeleted(false);
                                            user.setCreateDate(new Date());
                                            user.setForcePasswordChange(true);
                                            user.setRoleIds(activeRoles.stream().map(Role::getId).collect(Collectors.toSet()));

                                            return userRepository.save(user)
                                                    .map(savedUser -> {
                                                        Set<RoleDTO> roleDTOs = activeRoles.stream()
                                                                .map(role -> new RoleDTO(role.getId(), role.getRole(), role.getStatus()))
                                                                .collect(Collectors.toSet());

                                                        return new CustomUserDetailsDTO(
                                                                savedUser.getId(),
                                                                savedUser.getUsername(),
                                                                savedUser.getPassword(),
                                                                savedUser.getStatus(),
                                                                roleDTOs
                                                        );
                                                    });
                                        }))
                )
                .cast(CustomUserDetailsDTO.class)
                .doOnError(e -> ErrorHandler.handleError("Error creando usuario", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * Busca un usuario por su nombre de usuario o lo crea si no existe.
     *
     * @param username el nombre de usuario.
     * @param password la contraseña del usuario.
     * @param roleName el nombre del rol a asignar al usuario.
     * @return un {@link Mono} que emite los detalles del usuario encontrado o creado.
     */
    public Mono<CustomUserDetailsDTO> findOrCreateUser(String username, String password, String roleName) {
        return findByUsername(username)
                .switchIfEmpty(
                        roleService.findByRoleName(roleName)
                                .flatMap(role -> {
                                    Set<Role> roles = new HashSet<>();
                                    roles.add(role);
                                    return createUser(username, password, roles);
                                })
                )
                .doOnError(e -> ErrorHandler.handleError("Error encontrando o creando usuario", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * Actualiza el token de seguridad de un usuario.
     *
     * @param userId el ID del usuario.
     * @param token el nuevo token de seguridad.
     * @return un {@link Mono} que emite los detalles del usuario actualizado.
     */
    public Mono<CustomUserDetailsDTO> updateUserToken(String userId, String token) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    user.setSecurityToken(token);
                    return userRepository.save(user)
                            .flatMap(updatedUser -> findByUsername(updatedUser.getUsername()));
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")))
                .doOnError(e -> ErrorHandler.handleError("Error actualizando el token del usuario", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * Busca un usuario por su ID.
     *
     * @param userId el ID del usuario a buscar.
     * @return un {@link Mono} que emite los detalles del usuario encontrado.
     */
    public Mono<CustomUserDetailsDTO> findById(String userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")))
                .flatMap(user -> roleService.findRolesByIds(user.getRoleIds())
                        .collectList()
                        .flatMap(roles -> {
                            if (roles.isEmpty()) {
                                return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Roles del usuario no encontrados o inactivos"));
                            }
                            Set<RoleDTO> roleDTOs = roles.stream()
                                    .map(role -> new RoleDTO(role.getId(), role.getRole(), role.getStatus()))
                                    .collect(Collectors.toSet());

                            return Mono.just(new CustomUserDetailsDTO(
                                    user.getId(),
                                    user.getUsername(),
                                    user.getPassword(),
                                    user.getStatus(),
                                    roleDTOs
                            ));
                        }))
                .doOnError(e -> ErrorHandler.handleError("Error buscando usuario por ID", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
