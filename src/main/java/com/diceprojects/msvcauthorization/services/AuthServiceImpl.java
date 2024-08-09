package com.diceprojects.msvclogin.services;

import com.diceprojects.msvclogin.exceptions.ErrorHandler;
import com.diceprojects.msvclogin.persistences.models.dto.AuthResponse;
import com.diceprojects.msvclogin.persistences.models.dto.LoginRequest;
import com.diceprojects.msvclogin.persistences.repositories.UserRepository;
import com.diceprojects.msvclogin.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Implementación de la interfaz AuthService que proporciona servicios de autenticación.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ReactiveAuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Autentica a un usuario basado en la solicitud de inicio de sesión proporcionada.
     *
     * @param loginRequest la solicitud de inicio de sesión que contiene el nombre de usuario y la contraseña
     * @return un Mono que emite la respuesta de autenticación que contiene el token JWT y la fecha de expiración
     */
    @Override
    public Mono<AuthResponse> authenticate(LoginRequest loginRequest) {
        return userRepository.findByUsername(loginRequest.getUsername())
                .flatMap(user -> {
                    if (loginRequest.getPassword() == null) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña no puede ser nula"));
                    }
                    if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                        Authentication auth = new UsernamePasswordAuthenticationToken(
                                user.getUsername(),
                                loginRequest.getPassword(),
                                user.getAuthorities()
                        );
                        return authenticationManager.authenticate(auth)
                                .publishOn(Schedulers.boundedElastic())
                                .flatMap(authentication -> {
                                    ReactiveSecurityContextHolder.getContext().subscribe(context -> context.setAuthentication(authentication));
                                    return jwtUtil.generateToken(authentication)
                                            .map(token -> new AuthResponse(user.getUsername(), token, jwtUtil.getExpiryDateFromToken(token)));
                                });
                    } else {
                        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));
                    }
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")))
                .doOnError(e -> ErrorHandler.handleError("Error de autenticación", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
