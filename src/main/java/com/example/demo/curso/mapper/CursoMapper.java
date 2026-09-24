package com.example.demo.curso.mapper;

import com.example.demo.curso.dto.response.CursoResponse;
import com.example.demo.curso.model.Curso;
import org.springframework.stereotype.Component;

@Component
public class CursoMapper {

    public CursoResponse entityToDto(Curso curso) {
        if (curso == null) return null;
        return new CursoResponse(
                curso.getId(),
                curso.getNombre(),
                curso.getDescripcion(),
                curso.getCategoria() != null ? curso.getCategoria().getId() : null,
                curso.getCategoria() != null ? curso.getCategoria().getNombre() : null,
                curso.getNivel() != null ? curso.getNivel().getId() : null,
                curso.getNivel() != null ? curso.getNivel().getNombre() : null,
                curso.getDuracion(),
                curso.getEstado()
        );
    }
}
