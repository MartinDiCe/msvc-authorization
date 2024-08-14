package com.diceprojects.msvcauthorization.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración para el objeto {@link ObjectMapper} utilizado en la aplicación.
 * <p>
 * Esta clase define un bean de {@link ObjectMapper} que se puede inyectar en otros componentes de Spring
 * para serializar y deserializar objetos JSON.
 */
@Configuration
public class ObjectMapperConfig {

    /**
     * Define un bean de {@link ObjectMapper}.
     * <p>
     * Este bean proporciona una instancia de {@link ObjectMapper} configurada, que se puede utilizar
     * para convertir objetos Java en JSON y viceversa.
     *
     * @return una instancia de {@link ObjectMapper}.
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}

