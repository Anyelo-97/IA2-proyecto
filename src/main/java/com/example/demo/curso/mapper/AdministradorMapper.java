package com.example.demo.curso.mapper;

import com.example.demo.curso.dto.request.AdministradorRequest;
import com.example.demo.curso.dto.response.AdministradorResponse;
import com.example.demo.curso.model.Administrador;
import org.springframework.stereotype.Component;

@Component
public class AdministradorMapper {

    public AdministradorResponse entityToDto(Administrador administrador) {
        if (administrador == null) return null;
        return new AdministradorResponse(
                administrador.getId(),
                administrador.getNombre()
        );
    }

    public Administrador requestToEntity(AdministradorRequest request) {
        if (request == null) return null;
        return new Administrador(request.getId(), request.getNombre(), null);
    }
}
