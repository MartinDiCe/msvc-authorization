package com.diceprojects.msvcusers.persistences.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Reactivo de MongoDB para la entidad {@link TaxDetails}.
 * Este repositorio facilita la implementación de operaciones CRUD reactivas para manejar
 * los detalles de impuestos asociados con cálculos salariales.
 *
 * Al extender {@link ReactiveMongoRepository}, este repositorio hereda métodos para operaciones
 * reactivas como guardar, encontrar, eliminar y actualizar entidades {@link TaxDetails},
 * proporcionando una interfaz reactiva para interactuar con la base de datos MongoDB.
 *
 * Además de las operaciones CRUD, se pueden definir métodos de consulta personalizados
 * para satisfacer requisitos específicos de la aplicación, como la búsqueda de detalles de impuestos
 * por nombre, rango de montos, entre otros.
 */
/*@Repository
public interface TaxDetailsRepository extends ReactiveMongoRepository<TaxDetails, String> {
}*/
