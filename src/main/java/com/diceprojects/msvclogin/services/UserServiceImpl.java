package com.diceprojects.msvclogin.services;

import com.diceprojects.msvclogin.exceptions.ErrorHandler;
import com.diceprojects.msvclogin.persistences.models.entities.Role;
import com.diceprojects.msvclogin.persistences.models.entities.User;
import com.diceprojects.msvclogin.persistences.models.mappers.UserMapper;
import com.diceprojects.msvclogin.persistences.repositories.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementación de la interfaz UserService que proporciona servicios de gestión de usuarios.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService, UserMapper userMapper, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Encuentra un usuario por su nombre de usuario y devuelve sus detalles.
     *
     * @param username el nombre de usuario del usuario a buscar
     * @return un Mono que emite los detalles del usuario encontrado, o un error si el usuario no es encontrado
     */
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .flatMap(user -> roleService.findRolesByIds(user.getRoleIds())
                        .collectList()
                        .map(roles -> userMapper.mapToUserDetails(user, new HashSet<>(roles))))
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Usuario no encontrado")))
                .doOnError(e -> ErrorHandler.handleError("Error encontrando usuario por nombre de usuario", e, HttpStatus.NOT_FOUND));
    }

    /**
     * Crea un nuevo usuario con el nombre de usuario, contraseña y roles especificados.
     *
     * @param username el nombre de usuario del nuevo usuario
     * @param password la contraseña del nuevo usuario
     * @param roles los roles asignados al nuevo usuario
     * @return un Mono que emite los detalles del usuario creado, o un error si la creación falla
     */
    public Mono<UserDetails> createUser(String username, String password, Set<Role> roles) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setActive(true);
        user.setDeleted(false);
        user.setCreateDate(new Date());
        user.setForcePasswordChange(true);
        user.setRoleIds(roles.stream().map(Role::getId).collect(Collectors.toSet()));
        return userRepository.save(user)
                .flatMap(savedUser -> roleService.findRolesByIds(savedUser.getRoleIds())
                        .collectList()
                        .map(savedRoles -> userMapper.mapToUserDetails(savedUser, new HashSet<>(savedRoles))))
                .doOnError(e -> ErrorHandler.handleError("Error creando usuario", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * Encuentra un usuario por su nombre de usuario o crea un nuevo usuario si no se encuentra.
     *
     * @param username el nombre de usuario del usuario a encontrar o crear
     * @param password la contraseña del nuevo usuario si se crea
     * @param roleName el rol asignado al nuevo usuario si se crea
     * @return un Mono que emite los detalles del usuario encontrado o creado, o un error si la creación falla
     */
    public Mono<UserDetails> findOrCreateUser(String username, String password, String roleName) {
        return userRepository.findByUsername(username)
                .flatMap(user -> roleService.findRolesByIds(user.getRoleIds())
                        .collectList()
                        .map(roles -> userMapper.mapToUserDetails(user, new HashSet<>(roles))))
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
}
