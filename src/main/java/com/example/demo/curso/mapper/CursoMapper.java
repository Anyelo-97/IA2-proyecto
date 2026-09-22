package com.example.demo.curso.mapper;

import com.example.demo.curso.dto.response.CursoResponse;
import com.example.demo.curso.model.Curso;
import org.springframework.stereotype.Component;

@Component
public class CursoMapper {

    public CursoResponse entityToDto(Curso curso) {
        if (curso == null) return null;
        return CursoResponse.builder()
                .id(curso.getId())
                .nombre(curso.getNombre())
                .descripcion(curso.getDescripcion())
                .categoriaId(curso.getCategoria() != null ? curso.getCategoria().getId() : null)
                .categoriaNombre(curso.getCategoria() != null ? curso.getCategoria().getNombre() : null)
                .nivelId(curso.getNivel() != null ? curso.getNivel().getId() : null)
                .nivelNombre(curso.getNivel() != null ? curso.getNivel().getNombre() : null)
                .duracion(curso.getDuracion())
                .estado(curso.getEstado())
                .build();
    }
}
