package com.example.demo.curso.mapper;

import com.example.demo.curso.dto.request.EstudianteRequest;
import com.example.demo.curso.dto.response.EstudianteResponse;
import com.example.demo.curso.model.Estudiante;
import org.springframework.stereotype.Component;

@Component
public class EstudianteMapper {

    public EstudianteResponse entityToDto(Estudiante estudiante) {
        if (estudiante == null) return null;
        return new EstudianteResponse(
                estudiante.getId(),
                estudiante.getNombre(),
                estudiante.getNivelExperiencia(),
                estudiante.getAreaInteres()
        );
    }

    public Estudiante requestToEntity(EstudianteRequest request) {
        if (request == null) return null;
        return new Estudiante(
                request.getId(),
                request.getNombre(),
                request.getNivelExperiencia(),
                request.getAreaInteres(),
                null
        );
    }
}
