package com.diceprojects.msvcauthorization.clients;

import com.diceprojects.msvcauthorization.persistences.models.dtos.UserDetailsDTO;
import io.netty.handler.logging.LogLevel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

/**
 * Cliente para comunicarse con el microservicio de msvc-authentication.
 */
@Component
public class AuthenticationClient {

    private final WebClient webClient;

    /**
     * Constructor de AuthenticationClient.
     *
     * @param authenticationServiceUrl la URL base del servicio de autenticación, inyectada desde el archivo de configuración.
     */
    public AuthenticationClient(@Value("${msvc.authentication.url}") String authenticationServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(authenticationServiceUrl)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .compress(true)
                                .wiretap("reactor.netty.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
                ))
                .build();
    }

    /**
     * Obtiene los detalles de un usuario por su nombre de usuario desde el microservicio de msvc-authentication.
     *
     * @param username el nombre de usuario.
     * @return un {@link Mono} que emite los detalles del usuario encontrado.
     */
    public Mono<UserDetailsDTO> getUserByUsername(String username) {
        return webClient.get()
                .uri("/api/user/{username}", username)
                .retrieve()
                .bodyToMono(UserDetailsDTO.class)
                .onErrorResume(e -> Mono.error(new RuntimeException("Error al recuperar los detalles del usuario", e)));
    }

    /**
     * Valida un token de usuario en el microservicio de msvc-authentication.
     *
     * @param token el token JWT del usuario.
     * @return un {@link Mono} que emite los detalles del usuario si el token es válido.
     */
    public Mono<UserDetailsDTO> validateToken(String token) {
        return webClient.get()
                .uri("/api/auth/validate")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(UserDetailsDTO.class)
                .onErrorResume(e -> Mono.error(new RuntimeException("Error al validar el token", e)));
    }
}
