package com.diceprojects.msvcauthorization.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración de seguridad para la aplicación.
 * <p>
 * Esta clase define la configuración de seguridad, incluyendo el bean de {@link PasswordEncoder}
 * que se utiliza para cifrar las contraseñas de los usuarios en el sistema.
 */
@Configuration
public class SecurityConfig {

    /**
     * Define un bean de {@link PasswordEncoder} que utiliza el algoritmo BCrypt.
     * <p>
     * Este bean proporciona una instancia de {@link BCryptPasswordEncoder}, que se utiliza
     * para cifrar las contraseñas de los usuarios antes de almacenarlas en la base de datos.
     *
     * @return una instancia de {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

