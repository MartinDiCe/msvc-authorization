package com.diceprojects.msvclogin.services;

import com.diceprojects.msvclogin.exceptions.ErrorHandler;
import com.diceprojects.msvclogin.exceptions.RoleStatusException;
import com.diceprojects.msvclogin.persistences.models.entities.Role;
import com.diceprojects.msvclogin.persistences.models.enums.EntityStatus;
import com.diceprojects.msvclogin.persistences.repositories.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.management.relation.RoleNotFoundException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;

/**
 * Implementación de la interfaz {@link RoleService} que proporciona servicios de gestión de roles.
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Encuentra un rol por su nombre.
     *
     * @param roleName el nombre del rol a buscar
     * @return un Mono que emite el rol encontrado, o un error si no se encuentra ningún rol
     */
    @Override
    public Mono<Role> findByRoleName(String roleName) {
        return roleRepository.findByRole(roleName)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado")));
    }

    /**
     * Encuentra un rol por su nombre o crea un nuevo rol si no existe.
     *
     * @param roleName    el nombre del rol a buscar o crear
     * @param description la descripción del rol a crear si no existe
     * @return un Mono que emite el rol encontrado o creado, o un error si la creación falla
     */
    @Override
    public Mono<Role> findOrCreateRole(String roleName, String description) {
        return findByRoleName(roleName)
                .onErrorResume(e -> {
                    if (e instanceof ResponseStatusException && ((ResponseStatusException) e).getStatusCode() == HttpStatus.NOT_FOUND) {
                        return createRole(roleName, description);
                    }
                    return Mono.error(e);
                });
    }

    /**
     * Crea un nuevo rol.
     *
     * @param roleName    el nombre del rol a crear
     * @param description la descripción del rol a crear
     * @return un Mono que emite el rol creado, o un error si la creación falla
     */
    public Mono<Role> createRole(String roleName, String description) {
        Role role = new Role();
        role.setRole(roleName);
        role.setDescription(description);
        role.setCreateDate(ZonedDateTime.now(ZoneId.systemDefault()).toLocalDateTime());
        role.setDeleted(false);
        role.setStatus(EntityStatus.Active);
        return roleRepository.save(role)
                .doOnError(e -> ErrorHandler.handleError("Error creando el rol", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * Encuentra roles por sus IDs.
     *
     * @param roleIds el conjunto de IDs de roles a buscar
     * @return un Flux que emite los roles encontrados, o un error si no se encuentran roles
     */
    @Override
    public Flux<Role> findRolesByIds(Set<String> roleIds) {
        return roleRepository.findAllById(roleIds)
                .switchIfEmpty(Flux.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Roles no encontrados")))
                .doOnError(e -> ErrorHandler.handleError("Error encontrando roles por IDs", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * Actualiza un rol existente.
     *
     * @param roleId el ID del rol a actualizar
     * @param roleName el nuevo nombre del rol
     * @param description la nueva descripción del rol
     * @return un Mono que emite el rol actualizado
     */
    @Override
    public Mono<Role> updateRole(String roleId, String roleName, String description) {
        return roleRepository.findById(roleId)
                .flatMap(existingRole -> {
                    existingRole.setRole(roleName);
                    existingRole.setDescription(description);
                    existingRole.setUpdateDate(ZonedDateTime.now(ZoneId.systemDefault()).toLocalDateTime());
                    return roleRepository.save(existingRole);
                })
                .switchIfEmpty(Mono.error(new RoleNotFoundException("Role not found with id " + roleId)))
                .doOnError(e -> ErrorHandler.handleError("Error actualizando el rol", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * Cambia el estado de un rol.
     *
     * @param roleId el ID del rol a actualizar
     * @param status el nuevo estado del rol (activo/inactivo)
     * @return un Mono que emite el rol actualizado o un mensaje si el estado ya es el mismo
     */
    @Override
    public Mono<Object> changeRoleStatus(String roleId, EntityStatus status) {
        return roleRepository.findById(roleId)
                .flatMap(existingRole -> {
                    if (existingRole.getStatus() == status) {
                        return Mono.just("El estado actual ya es " + status);
                    }
                    existingRole.setStatus(status);
                    existingRole.setUpdateDate(ZonedDateTime.now(ZoneId.systemDefault()).toLocalDateTime());
                    existingRole.setStatus(status);
                    return roleRepository.save(existingRole).cast(Object.class);
                })
                .switchIfEmpty(Mono.error(new RoleNotFoundException("Role not found with id " + roleId)))
                .doOnError(e -> ErrorHandler.handleError("Error cambiando el estado del rol", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * Lista todos los roles.
     *
     * @return un Flux que emite todos los roles
     */
    @Override
    public Flux<Role> listRoles() {
        return roleRepository.findAll()
                .doOnError(e -> ErrorHandler.handleError("Error listando los roles", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
