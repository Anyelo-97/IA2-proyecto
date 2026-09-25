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
        String recId = fuente.getRecomendacion() != null ? fuente.getRecomendacion().getId() : null;
        String curId = fuente.getCurso() != null ? fuente.getCurso().getId() : null;
        String curNombre = fuente.getCurso() != null ? fuente.getCurso().getNombre() : null;
        String curDescripcion = fuente.getCurso() != null ? fuente.getCurso().getDescripcion() : null;
        String categoriaNombre = (fuente.getCurso() != null && fuente.getCurso().getCategoria() != null)
                ? fuente.getCurso().getCategoria().getNombre()
                : null;
        String nivelNombre = (fuente.getCurso() != null && fuente.getCurso().getNivel() != null)
                ? fuente.getCurso().getNivel().getNombre()
                : null;
        Integer duracion = fuente.getCurso() != null ? fuente.getCurso().getDuracion() : null;

        return new FuenteResponse(
                fuente.getId(),
                recId,
                curId,
                curNombre,
                curDescripcion,
                categoriaNombre,
                nivelNombre,
                duracion,
                fuente.getSimilitud()
        );
    }

    public Fuente requestToEntity(FuenteRequest request) {
        if (request == null) return null;
        Fuente fuente = new Fuente();
        fuente.setId(request.getId());
        if (request.getRecomendacionId() != null) {
            Recomendacion recomendacion = new Recomendacion();
            recomendacion.setId(request.getRecomendacionId());
            fuente.setRecomendacion(recomendacion);
        }
        if (request.getCursoId() != null) {
            Curso curso = new Curso();
            curso.setId(request.getCursoId());
            fuente.setCurso(curso);
        }
        fuente.setSimilitud(request.getSimilitud());
        return fuente;
    }
}
