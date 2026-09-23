package com.example.demo.curso.mapper;

import com.example.demo.curso.dto.request.UsuarioRequest;
import com.example.demo.curso.dto.response.UsuarioResponse;
import com.example.demo.curso.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioResponse entityToDto(Usuario usuario) {
        if (usuario == null) return null;
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .build();
    }

    public Usuario requestToEntity(UsuarioRequest request) {
        if (request == null) return null;
        return new Usuario(request.getId(), request.getEmail(), request.getPassword(), request.getRol());
    }
}
