package com.diceprojects.msvcauthorization.initialization;

import com.diceprojects.msvcauthorization.exceptions.ErrorHandler;
import com.diceprojects.msvcauthorization.persistences.models.dtos.CustomUserDetailsDTO;
import com.diceprojects.msvcauthorization.persistences.models.entities.Role;
import com.diceprojects.msvcauthorization.services.RoleService;
import com.diceprojects.msvcauthorization.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Data initializer to create default roles and users during application startup.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;

    public DataInitializer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) {
        createRoleIfNotFound("ADMIN", "Administrator role, full access").
                then(createRoleIfNotFound("USER", "Standard user role, limited access"))
                .flatMap(role -> createUserIfNotFound("admin", "password", role.getRole()))
                .subscribe(
                        result -> System.out.println("Initialization completed successfully"),
                        error -> ErrorHandler.handleError("Error initializing data", error, HttpStatus.INTERNAL_SERVER_ERROR)
                );
    }

    /**
     * Creates a role if it does not exist.
     *
     * @param roleName    the name of the role to create
     * @param description the description of the role to create
     * @return a Mono emitting the created or found role, or an error if creation fails
     */
    private Mono<Role> createRoleIfNotFound(String roleName, String description) {
        return roleService.findOrCreateRole(roleName, description)
                .doOnError(e -> ErrorHandler.handleError("Error creating role", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * Creates a user if it does not exist.
     *
     * @param username the username of the user to create
     * @param password the password of the user to create
     * @param roleName the role name to assign to the new user
     * @return a Mono emitting the created or found user details, or an error if creation fails
     */
    private Mono<CustomUserDetailsDTO> createUserIfNotFound(String username, String password, String roleName) {
        return userService.findOrCreateUser(username, password, roleName)
                .doOnError(e -> ErrorHandler.handleError("Error creating user default", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
