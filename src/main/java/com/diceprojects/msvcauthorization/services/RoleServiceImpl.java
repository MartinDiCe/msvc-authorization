package com.diceprojects.msvcauthorization.services;

import com.diceprojects.msvcauthorization.exceptions.ErrorHandler;
import com.diceprojects.msvcauthorization.persistences.models.entities.Role;
import com.diceprojects.msvcauthorization.persistences.repositories.RoleRepository;
import com.diceprojects.msvcauthorization.utils.EntityStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.management.relation.RoleNotFoundException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

/**
 * Implementación de la interfaz {@link RoleService} que proporciona servicios de gestión de roles.
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final EntityStatusService entityStatusService;

    /**
     * Constructor para inyectar las dependencias necesarias.
     *
     * @param roleRepository      el repositorio para gestionar roles.
     * @param entityStatusService el servicio para manejar el estado activo de las entidades.
     */
    public RoleServiceImpl(RoleRepository roleRepository, EntityStatusService entityStatusService) {
        this.roleRepository = roleRepository;
        this.entityStatusService = entityStatusService;
    }

    /**
     * Encuentra un rol por su nombre.
     *
     * @param roleName el nombre del rol a buscar.
     * @return un {@link Mono} que emite el rol encontrado, o un error si no se encuentra ningún rol.
     */
    @Override
    public Mono<Role> findByRoleName(String roleName) {
        return roleRepository.findByRoleIgnoreCase(roleName)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found")));
    }

    /**
     * Encuentra un rol por su nombre o crea un nuevo rol si no existe.
     *
     * @param roleName    el nombre del rol a buscar o crear.
     * @param description la descripción del rol a crear si no existe.
     * @return un {@link Mono} que emite el rol encontrado o creado, o un error si la creación falla.
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
     * @param roleName    el nombre del rol a crear.
     * @param description la descripción del rol a crear.
     * @return un {@link Mono} que emite el rol creado, o un error si la creación falla.
     */
    public Mono<Role> createRole(String roleName, String description) {
        return entityStatusService.obtenerEstadoActivo()
                .flatMap(activeStatus -> {
                    Role role = new Role();
                    role.setRole(roleName);
                    role.setDescription(description);
                    role.setCreateDate(ZonedDateTime.now(ZoneId.systemDefault()).toLocalDateTime());
                    role.setDeleted(false);
                    role.setStatus(activeStatus);

                    return roleRepository.save(role)
                            .doOnError(e -> ErrorHandler.handleError("Error creando el rol", e, HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    /**
     * Encuentra roles por sus IDs.
     *
     * @param roleIds el conjunto de IDs de roles a buscar.
     * @return un {@link Flux} que emite los roles encontrados, o un error si no se encuentran roles.
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
     * @param roleId el ID del rol a actualizar.
     * @param roleName el nuevo nombre del rol.
     * @param description la nueva descripción del rol.
     * @return un {@link Mono} que emite el rol actualizado.
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
     * @param roleId el ID del rol a actualizar.
     * @param status el nuevo estado del rol (activo/inactivo).
     * @return un {@link Mono} que emite el rol actualizado o un mensaje si el estado ya es el mismo.
     */
    @Override
    public Mono<Object> changeRoleStatus(String roleId, String status) {
        return entityStatusService.obtenerEstadoActivo()
                .flatMap(activeStatus -> {
                    if (!activeStatus.equalsIgnoreCase(status)) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado proporcionado no es válido"));
                    }

                    return roleRepository.findById(roleId)
                            .flatMap(existingRole -> {
                                if (existingRole.getStatus().equals(status)) {
                                    return Mono.just("El estado actual ya es " + status);
                                }
                                existingRole.setStatus(status);
                                existingRole.setUpdateDate(ZonedDateTime.now(ZoneId.systemDefault()).toLocalDateTime());
                                return roleRepository.save(existingRole).cast(Object.class);
                            })
                            .switchIfEmpty(Mono.error(new RoleNotFoundException("Role not found with id " + roleId)))
                            .doOnError(e -> ErrorHandler.handleError("Error cambiando el estado del rol", e, HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    /**
     * Lista todos los roles.
     *
     * @return un {@link Flux} que emite todos los roles.
     */
    @Override
    public Flux<Role> listRoles() {
        return roleRepository.findAll()
                .doOnError(e -> ErrorHandler.handleError("Error listando los roles", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
