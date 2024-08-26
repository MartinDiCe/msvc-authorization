package com.diceprojects.msvcauthorization.persistences.repositories;

import com.diceprojects.msvcauthorization.persistences.models.entities.Role;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio para operaciones de la entidad {@link Role} en MongoDB.
 * Extiende {@link ReactiveMongoRepository} para soporte reactivo con MongoDB.
 */
public interface RoleRepository extends ReactiveMongoRepository<Role, String> {

    /**
     * Busca un {@link Role} por su atributo 'role'.
     * Este método utiliza funcionalidades reactivas para la consulta de datos.
     *
     * @param role el nombre del rol que se desea buscar.
     * @return un {@link Mono} que contiene el {@link Role} encontrado, o vacío si no se encuentra ninguno.
     */
    Mono<Role> findByRole(String role);

    /**
     * Busca un rol por su nombre, ignorando mayúsculas y minúsculas.
     *
     * @param roleName el nombre del rol a buscar, ignorando mayúsculas y minúsculas.
     * @return un {@link Mono} que emite el rol encontrado, o vacío si no se encuentra ningún rol con ese nombre.
     */
    Mono<Role> findByRoleIgnoreCase(String roleName);

}

