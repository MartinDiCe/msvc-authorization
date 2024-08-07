package com.diceprojects.msvclogin.services;

import com.diceprojects.msvclogin.persistences.models.entities.Role;
import com.diceprojects.msvclogin.persistences.models.enums.EntityStatus;
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
     * @param roleName el nombre del rol a buscar
     * @return un Mono que emite el rol encontrado, o vacío si no se encuentra ningún rol
     */
    Mono<Role> findByRoleName(String roleName);

    /**
     * Encuentra un rol por su nombre o crea un nuevo rol si no existe.
     *
     * @param roleName el nombre del rol a buscar o crear
     * @param description la descripción del rol a crear si no existe
     * @return un Mono que emite el rol encontrado o creado
     */
    Mono<Role> findOrCreateRole(String roleName, String description);

    /**
     * Crea un nuevo rol.
     *
     * @param roleName el nombre del rol a crear
     * @param description la descripción del rol a crear
     * @return un Mono que emite el rol creado
     */
    Mono<Role> createRole(String roleName, String description);

    /**
     * Encuentra roles por sus IDs.
     *
     * @param roleIds el conjunto de IDs de roles a buscar
     * @return un Flux que emite los roles encontrados
     */
    Flux<Role> findRolesByIds(Set<String> roleIds);

    /**
     * Actualizar un rol.
     *
     * @param roleName el nuevo nombre del rol
     * @param description la nueva descripción del rol
     * @return un Mono que emite el rol creado
     */
    Mono<Role> updateRole(String roleId, String roleName, String description);

    /**
     * Cambia el estado de un rol.
     *
     * @param roleId el ID del rol a actualizar
     * @param status el nuevo estado del rol (activo/inactivo)
     * @return un Mono que emite el rol actualizado
     */
    Mono<Object> changeRoleStatus(String roleId, EntityStatus status);

    /**
     * Lista todos los roles.
     *
     * @return un Flux que emite todos los roles
     */
    Flux<Role> listRoles();

}
