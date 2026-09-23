package com.example.demo.curso.mapper;

import com.example.demo.curso.dto.request.NivelDificultadRequest;
import com.example.demo.curso.dto.response.NivelDificultadResponse;
import com.example.demo.curso.model.NivelDificultad;
import org.springframework.stereotype.Component;

@Component
public class NivelDificultadMapper {

    public NivelDificultadResponse entityToDto(NivelDificultad nivel) {
        if (nivel == null) return null;
        return NivelDificultadResponse.builder()
                .id(nivel.getId())
                .nombre(nivel.getNombre())
                .build();
    }

    public NivelDificultad requestToEntity(NivelDificultadRequest request) {
        if (request == null) return null;
        return NivelDificultad.builder()
                .id(request.getId())
                .nombre(request.getNombre())
                .build();
    }
}
