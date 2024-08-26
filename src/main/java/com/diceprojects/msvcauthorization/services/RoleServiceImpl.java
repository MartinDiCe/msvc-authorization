package com.diceprojects.msvcauthorization.services;

import com.diceprojects.msvcauthorization.exceptions.ErrorHandler;
import com.diceprojects.msvcauthorization.persistences.models.dtos.CreateRoleDTO;
import com.diceprojects.msvcauthorization.persistences.models.entities.Role;
import com.diceprojects.msvcauthorization.persistences.models.mappers.RoleMapper;
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
    private final RoleMapper roleMapper;

    /**
     * Constructor para inyectar las dependencias necesarias.
     *
     * @param roleRepository      el repositorio para gestionar roles.
     * @param entityStatusService el servicio para manejar el estado activo de las entidades.
     */
    public RoleServiceImpl(RoleRepository roleRepository, EntityStatusService entityStatusService, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.entityStatusService = entityStatusService;
        this.roleMapper = roleMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Role> findByRoleName(String roleName) {
        return roleRepository.findByRoleIgnoreCase(roleName)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found")));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Role> findOrCreateRole(String roleName, String description) {
        return findByRoleName(roleName)
                .switchIfEmpty(
                        createRole(new CreateRoleDTO(roleName, description))
                )
                .onErrorResume(e -> {
                    if (e instanceof ResponseStatusException && ((ResponseStatusException) e).getStatusCode() == HttpStatus.NOT_FOUND) {
                        return createRole(new CreateRoleDTO(roleName, description));
                    }
                    return Mono.error(e);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Role> createRole(CreateRoleDTO createRoleDTO) {
        return entityStatusService.obtenerEstadoActivo()
                .flatMap(activeStatus -> {
                    Role role = roleMapper.mapToRole(createRoleDTO, activeStatus);
                    return roleRepository.save(role)
                            .doOnError(e -> ErrorHandler.handleError("Error creando el rol", e, HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Role> findRolesByIds(Set<String> roleIds) {
        return roleRepository.findAllById(roleIds)
                .switchIfEmpty(Flux.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Roles no encontrados")))
                .doOnError(e -> ErrorHandler.handleError("Error encontrando roles por IDs", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public Flux<Role> listRoles() {
        return roleRepository.findAll()
                .doOnError(e -> ErrorHandler.handleError("Error listando los roles", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Role> getDefaultUserRole() {
        return roleRepository.findByRole("USER")
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol USER no encontrado")));
    }



}
