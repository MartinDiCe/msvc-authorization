package com.diceprojects.msvcusers.security;

import com.diceprojects.msvcusers.exceptions.ErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;

import static com.diceprojects.msvcusers.utils.AuthWhitelist.AUTH_WHITELIST;

/**
 * Configuración de seguridad para la aplicación.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final ReactiveUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public SecurityConfig(@Lazy ReactiveUserDetailsService userDetailsService, @Lazy JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Configura la cadena de filtros de seguridad web.
     *
     * @param http el objeto ServerHttpSecurity para configurar la seguridad web
     * @return la cadena de filtros de seguridad web configurada
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        try {
            return http
                    .authorizeExchange(exchanges -> exchanges
                            .pathMatchers(AUTH_WHITELIST).permitAll()
                            .anyExchange().authenticated()
                    )
                    .addFilterAt(jwtAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                    .csrf(ServerHttpSecurity.CsrfSpec::disable)
                    .build();
        } catch (Exception e) {
            ErrorHandler.handleError("Error configuring SecurityWebFilterChain", e, HttpStatus.INTERNAL_SERVER_ERROR);
            return null;
        }
    }

    /**
     * Crea y configura el filtro de autenticación JWT.
     *
     * @return el filtro de autenticación JWT configurado
     */
    @Bean
    public AuthenticationWebFilter jwtAuthenticationFilter() {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(reactiveAuthenticationManager());
        authenticationWebFilter.setServerAuthenticationConverter(jwtServerAuthenticationConverter());
        return authenticationWebFilter;
    }

    /**
     * Crea y configura el gestor de autenticación reactivo.
     *
     * @return el gestor de autenticación reactivo configurado
     */
    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authManager.setPasswordEncoder(passwordEncoder());
        return authManager;
    }

    /**
     * Crea y configura el convertidor de autenticación JWT.
     *
     * @return el convertidor de autenticación JWT configurado
     */
    @Bean
    public ServerAuthenticationConverter jwtServerAuthenticationConverter() {
        return new JwtServerAuthenticationConverter(jwtUtil);
    }

    /**
     * Crea y configura el codificador de contraseñas.
     *
     * @return el codificador de contraseñas configurado
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
