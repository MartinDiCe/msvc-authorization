package com.diceprojects.msvcusers.persistences.repositories;

import com.diceprojects.msvcusers.persistences.models.entities.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * Repositorio para manejar operaciones CRUD para la entidad {@link User} en MongoDB.
 * Extiende {@link ReactiveMongoRepository} para proporcionar operaciones de base de datos reactivas.
 *
 * @author ReactiveMongoRepository permite la utilización de operaciones CRUD reactivas en MongoDB,
 * ofreciendo mejor rendimiento y manejo de concurrencia que las operaciones bloqueantes tradicionales.
 */
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    /**
     * Encuentra un usuario por su nombre de usuario.
     *
     * @param username El nombre de usuario del usuario a buscar.
     * @return Un {@link Mono} que, al ser suscrito, proporciona el usuario encontrado o un {@link Mono#empty()} si no se encuentra ninguno.
     */
    Mono<User> findByUsername(String username);
}
