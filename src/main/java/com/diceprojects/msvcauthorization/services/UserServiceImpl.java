package com.diceprojects.msvcauthorization.services;

import com.diceprojects.msvcauthorization.exceptions.ErrorHandler;
import com.diceprojects.msvcauthorization.persistences.models.entities.User;
import com.diceprojects.msvcauthorization.persistences.models.mappers.UserMapper;
import com.diceprojects.msvcauthorization.persistences.repositories.UserRepository;
import com.diceprojects.msvcauthorization.persistences.models.dtos.CustomUserDetailsDTO;
import com.diceprojects.msvcauthorization.utils.EntityStatusService;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

/**
 * Implementación de la interfaz {@link UserService} que proporciona servicios de gestión de usuarios.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final EntityStatusService entityStatusService;
    private final UserMapper userMapper;

    /**
     * Constructor para inyectar las dependencias necesarias.
     *
     * @param userRepository     el repositorio para gestionar usuarios.
     * @param roleService        el servicio para gestionar roles.
     * @param entityStatusService el servicio para manejar el estado activo de las entidades.
     * @param userMapper el mapper para transformar entidades de usuario a DTOs.
     */
    public UserServiceImpl(UserRepository userRepository, RoleService roleService,
                           EntityStatusService entityStatusService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.entityStatusService = entityStatusService;
        this.userMapper = userMapper;
    }

    /**
     * {@inheritDoc}
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
                            .flatMap(roles -> Mono.just(userMapper.mapToUserDetails(user, new HashSet<>(roles))));
                })
                .switchIfEmpty(Mono.empty())
                .doOnError(e -> ErrorHandler.handleError("Error encontrando usuario por nombre de usuario", e, HttpStatus.NOT_FOUND));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<CustomUserDetailsDTO> create(String username, String password) {
        return entityStatusService.obtenerEstadoActivo()
                .flatMap(activeStatus -> {
                    User user = userMapper.createNewUser(username, password, activeStatus);
                    return userRepository.save(user)
                            .map(userMapper::mapToUserDetails);
                })
                .doOnError(e -> ErrorHandler.handleError("Error creating user", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<CustomUserDetailsDTO> registerUser(String username, String password) {
        return userRepository.findByUsername(username)
                .flatMap(existingUser -> Mono.just(userMapper.mapToUserDetails(existingUser, Set.of())))
                .switchIfEmpty(
                        this.create(username, password)
                                .flatMap(newUserDetails -> roleService.getDefaultUserRole()
                                        .flatMap(defaultRole -> {
                                            if (!defaultRole.getStatus().equalsIgnoreCase("Active")) {
                                                return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rol por defecto no está activo"));
                                            }

                                            return this.assignRoleToUser(username, defaultRole.getId())
                                                    .then(Mono.just(newUserDetails));
                                        }))
                )
                .doOnError(e -> ErrorHandler.handleError("Error creando usuario", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<CustomUserDetailsDTO> findOrCreateUser(String username, String password, String roleName) {
        return findByUsername(username)
                .switchIfEmpty(
                        this.registerUser(username, password)
                                .flatMap(newUserDetails ->
                                        roleService.findByRoleName(roleName)
                                                .flatMap(role -> this.assignRoleToUser(username, role.getId())
                                                        .then(Mono.just(newUserDetails)))
                                )
                )
                .doOnError(e -> ErrorHandler.handleError("Error encontrando o creando usuario", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
    public Mono<CustomUserDetailsDTO> findById(String userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")))
                .flatMap(user -> roleService.findRolesByIds(user.getRoleIds())
                        .collectList()
                        .flatMap(roles -> {
                            if (roles.isEmpty()) {
                                return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Roles del usuario no encontrados o inactivos"));
                            }
                            return Mono.just(userMapper.mapToUserDetails(user, new HashSet<>(roles)));
                        }))
                .doOnError(e -> ErrorHandler.handleError("Error buscando usuario por ID", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<CustomUserDetailsDTO> assignRoleToUser(String username, String roleId) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")))
                .flatMap(user -> roleService.findRolesByIds(Set.of(roleId))
                        .single()  // Esperamos un solo rol
                        .flatMap(role -> {
                            if (user.getRoleIds().contains(role.getId())) {
                                return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "El rol ya está asignado al usuario."));
                            }

                            user.getRoleIds().add(role.getId());

                            return userRepository.save(user)
                                    .flatMap(savedUser -> Mono.just(userMapper.mapToUserDetails(savedUser, Set.of(role))));
                        })
                )
                .doOnError(e -> ErrorHandler.handleError("Error asignando rol al usuario", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
