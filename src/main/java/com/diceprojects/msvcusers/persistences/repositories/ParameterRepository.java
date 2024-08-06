package com.diceprojects.msvcusers.persistences.repositories;

import com.diceprojects.msvcusers.persistences.models.entities.Parameter;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * Repositorio reactivo para la entidad {@link Parameter}.
 */
public interface ParameterRepository extends ReactiveMongoRepository<Parameter, String> {

    /**
     * Encuentra parámetros por su nombre.
     *
     * @param parameterName el nombre del parámetro a buscar
     * @return un {@link Flux} que emite los parámetros encontrados
     */
    Flux<Parameter> findByParameterName(String parameterName);
}
