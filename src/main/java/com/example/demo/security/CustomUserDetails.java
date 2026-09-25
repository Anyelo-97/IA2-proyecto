package com.example.demo.security;

import com.example.demo.curso.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getId() {
        return usuario != null ? usuario.getId() : null;
    }

    public String getRol() {
        return usuario != null ? usuario.getRol() : null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (usuario == null || usuario.getRol() == null) {
            return List.of();
        }
        String roleName = usuario.getRol().startsWith("ROLE_")
                ? usuario.getRol()
                : "ROLE_" + usuario.getRol();
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword() {
        return usuario != null ? usuario.getPassword() : null;
    }

    @Override
    public String getUsername() {
        return usuario != null ? usuario.getEmail() : null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
