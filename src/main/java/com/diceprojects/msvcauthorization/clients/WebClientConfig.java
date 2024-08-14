package com.diceprojects.msvcauthorization.clients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuración para el cliente WebClient utilizado en la aplicación.
 * <p>
 * Esta clase define un bean de {@link WebClient.Builder} que se puede inyectar en otros componentes de Spring
 * para construir instancias de {@link WebClient}, lo que permite realizar solicitudes HTTP de manera reactiva.
 */
@Configuration
public class WebClientConfig {

    /**
     * Define un bean de {@link WebClient.Builder}.
     * <p>
     * Este bean proporciona un {@link WebClient.Builder} configurado, que se puede utilizar para construir
     * instancias de {@link WebClient} en diferentes partes de la aplicación.
     *
     * @return una instancia de {@link WebClient.Builder}.
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}

