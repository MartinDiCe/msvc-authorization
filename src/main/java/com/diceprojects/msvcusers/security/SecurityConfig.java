package com.diceprojects.msvcusers.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

/**
 * Configuración de seguridad de Spring Security para la aplicación WebFlux.
 * Habilita la seguridad web reactiva y configura un filtro personalizado para JWT.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final ReactiveUserDetailsService userDetailsService;

    /**
     * Constructor que inyecta el servicio de detalles del usuario.
     *
     * @param userDetailsService Servicio que carga los datos del usuario por nombre de usuario.
     */
    public SecurityConfig(ReactiveUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Define la cadena de filtros de seguridad para configurar las reglas de autorización.
     *
     * @param http Configuración de seguridad HTTP.
     * @return La cadena de filtros de seguridad configurada.
     * @throws Exception Si ocurre un error durante la configuración.
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/public/**").permitAll() // Permite el acceso no autenticado a rutas públicas.
                        .anyExchange().authenticated()          // Requiere autenticación para todas las demás rutas.
                )
                .addFilterAt(jwtAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION) // Agrega el filtro personalizado de JWT.
                .csrf(csrf -> csrf.disable()); // Deshabilita CSRF para API REST.

        return http.build();
    }

    /**
     * Crea un filtro de autenticación que usa JWT para la gestión de sesiones.
     *
     * @return El filtro de autenticación personalizado.
     */
    private AuthenticationWebFilter jwtAuthenticationFilter() {
        ReactiveAuthenticationManager authManager = authentication -> userDetailsService.findByUsername(authentication.getName())
                .map(userDetails -> new UsernamePasswordAuthenticationToken(
                        userDetails, userDetails.getPassword(), userDetails.getAuthorities()));

        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authManager);
        authenticationWebFilter.setServerAuthenticationConverter(new ServerHttpBearerAuthenticationConverter());
        return authenticationWebFilter;
    }
}

