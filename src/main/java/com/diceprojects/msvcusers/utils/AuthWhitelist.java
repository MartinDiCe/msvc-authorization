package com.diceprojects.msvcusers.utils;

/**
 * Clase utilitaria que proporciona una lista de rutas permitidas sin autenticación.
 */
public class AuthWhitelist {

    /**
     * Lista de rutas permitidas sin necesidad de autenticación.
     */
    public static final String[] AUTH_WHITELIST = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/api/auth/**"
    };
}

