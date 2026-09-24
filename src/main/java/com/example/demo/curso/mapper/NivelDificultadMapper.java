package com.example.demo.curso.mapper;

import com.example.demo.curso.dto.request.NivelDificultadRequest;
import com.example.demo.curso.dto.response.NivelDificultadResponse;
import com.example.demo.curso.model.NivelDificultad;
import org.springframework.stereotype.Component;

@Component
public class NivelDificultadMapper {

    public NivelDificultadResponse entityToDto(NivelDificultad nivel) {
        if (nivel == null) return null;
        return new NivelDificultadResponse(
                nivel.getId(),
                nivel.getNombre()
        );
    }

    public NivelDificultad requestToEntity(NivelDificultadRequest request) {
        if (request == null) return null;
        return new NivelDificultad(
                request.getId(),
                request.getNombre()
        );
    }
}
