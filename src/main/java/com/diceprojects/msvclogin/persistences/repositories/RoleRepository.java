package com.diceprojects.msvclogin.persistences.repositories;

import com.diceprojects.msvclogin.persistences.models.entities.Role;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
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

}

