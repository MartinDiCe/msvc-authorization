package com.diceprojects.msvcauthorization.services;

import com.diceprojects.msvcauthorization.persistences.models.dtos.CreateRoleDTO;
import com.diceprojects.msvcauthorization.persistences.models.entities.Role;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * Interfaz que proporciona servicios de gestión de roles.
 */
public interface RoleService {

    /**
     * Encuentra un rol por su nombre.
     *
     * @param roleName el nombre del rol a buscar.
     * @return un {@link Mono} que emite el rol encontrado, o un error si no se encuentra ningún rol.
     */
    Mono<Role> findByRoleName(String roleName);

    /**
     * Encuentra un rol por su nombre o crea un nuevo rol si no existe.
     *
     * @param roleName    el nombre del rol a buscar o crear.
     * @param description la descripción del rol a crear si no existe.
     * @return un {@link Mono} que emite el rol encontrado o creado, o un error si la creación falla.
     */
    Mono<Role> findOrCreateRole(String roleName, String description);

    /**
     * Crea un nuevo rol.
     *
     * @param createRoleDTO el DTO que contiene la información del rol a crear.
     * @return un {@link Mono} que emite el rol creado, o un error si la creación falla.
     */
    Mono<Role> createRole(CreateRoleDTO createRoleDTO);

    /**
     * Encuentra roles por sus IDs.
     *
     * @param roleIds el conjunto de IDs de roles a buscar.
     * @return un {@link Flux} que emite los roles encontrados, o un error si no se encuentran roles.
     */
    Flux<Role> findRolesByIds(Set<String> roleIds);

    /**
     * Actualiza un rol existente.
     *
     * @param roleId      el ID del rol a actualizar.
     * @param roleName    el nuevo nombre del rol.
     * @param description la nueva descripción del rol.
     * @return un {@link Mono} que emite el rol actualizado.
     */
    Mono<Role> updateRole(String roleId, String roleName, String description);

    /**
     * Cambia el estado de un rol.
     *
     * @param roleId el ID del rol a actualizar.
     * @param status el nuevo estado del rol (activo/inactivo).
     * @return un {@link Mono} que emite el rol actualizado o un mensaje si el estado ya es el mismo.
     */
    Mono<Object> changeRoleStatus(String roleId, String status);

    /**
     * Lista todos los roles.
     *
     * @return un {@link Flux} que emite todos los roles.
     */
    Flux<Role> listRoles();

    /**
     * Obtiene el rol por defecto para los usuarios.
     *
     * @return un {@link Mono} que emite el rol por defecto si existe, de lo contrario, emite un error 404.
     */
    Mono<Role> getDefaultUserRole();

}