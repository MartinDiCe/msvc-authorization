package com.diceprojects.msvclogin.controllers;

import com.diceprojects.msvclogin.persistences.models.entities.Role;
import com.diceprojects.msvclogin.services.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Controlador para manejar las solicitudes relacionadas con los roles.
 */
@RestController
@RequestMapping("/roles")
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
    @GetMapping("/{roleName}")
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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Role> create(@RequestParam String roleName, @RequestParam(required = false, defaultValue = "") String description) {
        return roleService.createRole(roleName, description);
    }
}
