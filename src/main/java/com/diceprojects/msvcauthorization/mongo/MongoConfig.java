package com.diceprojects.msvcauthorization.mongo;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * Configuración de MongoDB para el proyecto.
 * Esta clase configura los repositorios MongoDB para que operen en modo reactivo,
 * permitiendo a la aplicación interactuar de manera no bloqueante con MongoDB.
 *
 * @Configuration indica que esta clase contiene configuración de beans para el contexto de aplicación de Spring.
 * @EnableReactiveMongoRepositories habilita el soporte para repositorios MongoDB reactivos en la aplicación.
 * Esto incluye la detección automática de interfaces de repositorio en el paquete especificado.
 */
@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.diceprojects.msvcauthorization.persistences.repositories")
public class MongoConfig {

}
