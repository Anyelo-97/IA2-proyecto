package com.example.demo.curso.mapper;

import com.example.demo.curso.dto.request.FuenteRequest;
import com.example.demo.curso.dto.response.FuenteResponse;
import com.example.demo.curso.model.Curso;
import com.example.demo.curso.model.Fuente;
import com.example.demo.curso.model.Recomendacion;
import org.springframework.stereotype.Component;

@Component
public class FuenteMapper {

    public FuenteResponse entityToDto(Fuente fuente) {
        if (fuente == null) return null;
        return FuenteResponse.builder()
                .id(fuente.getId())
                .recomendacionId(fuente.getRecomendacion() != null ? fuente.getRecomendacion().getId() : null)
                .cursoId(fuente.getCurso() != null ? fuente.getCurso().getId() : null)
                .similitud(fuente.getSimilitud())
                .build();
    }

    public Fuente requestToEntity(FuenteRequest request) {
        if (request == null) return null;
        Fuente fuente = new Fuente();
        if (request.getId() != null) {
            fuente.setId(request.getId());
        }
        if (request.getRecomendacionId() != null) {
            Recomendacion recomendacion = new Recomendacion();
            recomendacion.setId(request.getRecomendacionId());
            fuente.setRecomendacion(recomendacion);
        }
        if (request.getCursoId() != null) {
            fuente.setCurso(Curso.builder().id(request.getCursoId()).build());
        }
        fuente.setSimilitud(request.getSimilitud());
        return fuente;
    }
}
