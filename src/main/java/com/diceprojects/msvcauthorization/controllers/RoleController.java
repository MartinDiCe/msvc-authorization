package com.diceprojects.msvcauthorization.controllers;

import com.diceprojects.msvcauthorization.persistences.models.entities.Role;
import com.diceprojects.msvcauthorization.services.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controlador para manejar las solicitudes relacionadas con los roles.
 */
@RestController
@RequestMapping("/api/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Obtiene un rol por su nombre.
     *
     * @param roleName el nombre del rol a buscar
     * @return un {@link Mono} que emite el rol encontrado
     */
    @GetMapping("/getRoleByName/{roleName}")
    public Mono<Role> getRoleByName(@PathVariable String roleName) {
        return roleService.findByRoleName(roleName);
    }

    /**
     * Crea un nuevo rol.
     *
     * @param roleName    el nombre del rol a crear
     * @param description la descripción del rol (opcional)
     * @return un {@link Mono} que emite el rol creado
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Role> create(@RequestParam String roleName, @RequestParam(required = false, defaultValue = "") String description) {
        return roleService.createRole(roleName, description);
    }

    /**
     * Actualiza un rol existente.
     *
     * @param roleId el ID del rol a actualizar
     * @param roleName el nuevo nombre del rol
     * @param description la nueva descripción del rol, opcional
     * @return un Mono que emite el rol actualizado
     */
    @PutMapping("/update/{roleId}")
    public Mono<Role> update(@PathVariable String roleId, @RequestParam String roleName, @RequestParam(required = false, defaultValue = "") String description) {
        return roleService.updateRole(roleId, roleName, description);
    }

    /**
     * Cambia el estado de un rol.
     *
     * @param roleId el ID del rol a actualizar
     * @param status el nuevo estado del rol (activo/inactivo)
     * @return un Mono que emite el rol actualizado o un mensaje si el estado ya es el mismo
     */
    @PutMapping("/changeStatus/{roleId}")
    public Mono<Object> changeStatus(@PathVariable String roleId, @RequestParam String status) {
        return roleService.changeRoleStatus(roleId, status);
    }

    /**
     * Lista todos los roles.
     *
     * @return un Flux que emite todos los roles
     */
    @GetMapping("/listRoles")
    public Flux<Role> listRoles() {
        return roleService.listRoles();
    }

}
