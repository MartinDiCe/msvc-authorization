package com.diceprojects.msvclogin.persistences.models.entities;

import com.diceprojects.msvclogin.security.Authority;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Entidad de usuario que implementa {@link UserDetails} para la integración con Spring Security.
 */
@Document(collection = "users")
@Data
public class User implements UserDetails {
    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    private String password;
    private boolean active;
    private boolean deleted = false;
    private Date deleteDate;
    private Date createDate;
    private Date updateDate;
    private String securityToken;
    private boolean forcePasswordChange;
    private Set<String> roleIds = new HashSet<>();

    /**
     * Obtiene las autoridades concedidas al usuario.
     *
     * @return una colección de autoridades concedidas
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleIds.stream()
                .map(Authority::new)
                .collect(Collectors.toSet());
    }

    /**
     * Indica si la cuenta del usuario no ha expirado.
     *
     * @return true si la cuenta no ha expirado, false en caso contrario
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta del usuario no está bloqueada.
     *
     * @return true si la cuenta no está bloqueada, false en caso contrario
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica si las credenciales del usuario no han expirado.
     *
     * @return true si las credenciales no han expirado, false en caso contrario
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta del usuario está habilitada.
     *
     * @return true si la cuenta está habilitada, false en caso contrario
     */
    @Override
    public boolean isEnabled() {
        return active;
    }

    /**
     * Establece el token de seguridad para el usuario.
     *
     * @param token el token de seguridad
     */
    public void setToken(String token) {
        this.securityToken = token;
    }
}
