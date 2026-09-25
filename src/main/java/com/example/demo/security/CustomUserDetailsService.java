package com.example.demo.security;

import com.example.demo.curso.model.Usuario;
import com.example.demo.curso.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        if (identifier == null || identifier.isBlank()) {
            throw new UsernameNotFoundException("Identificador vacio");
        }
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(identifier.trim())
                .or(() -> usuarioRepository.findById(identifier.trim()))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con: " + identifier));
        return new CustomUserDetails(usuario);
    }
}
