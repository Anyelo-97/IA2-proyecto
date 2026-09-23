package com.example.demo.curso.mapper;

import com.example.demo.curso.dto.request.CalificacionRequest;
import com.example.demo.curso.dto.response.CalificacionResponse;
import com.example.demo.curso.model.Calificacion;
import com.example.demo.curso.model.Estudiante;
import com.example.demo.curso.model.Recomendacion;
import org.springframework.stereotype.Component;

@Component
public class CalificacionMapper {

    public CalificacionResponse entityToDto(Calificacion calificacion) {
        if (calificacion == null) return null;
        return CalificacionResponse.builder()
                .id(calificacion.getId())
                .estudianteId(calificacion.getEstudiante() != null ? calificacion.getEstudiante().getId() : null)
                .recomendacionId(calificacion.getRecomendacion() != null ? calificacion.getRecomendacion().getId() : null)
                .puntuacion(calificacion.getPuntuacion())
                .comentario(calificacion.getComentario())
                .build();
    }

    public Calificacion requestToEntity(CalificacionRequest request) {
        if (request == null) return null;
        Calificacion calificacion = new Calificacion();
        calificacion.setId(request.getId());
        calificacion.setEstudiante(request.getEstudianteId() == null ? null : new Estudiante(request.getEstudianteId(), null, null, null, null));
        calificacion.setRecomendacion(request.getRecomendacionId() == null ? null : new Recomendacion());
        if (calificacion.getRecomendacion() != null) {
            calificacion.getRecomendacion().setId(request.getRecomendacionId());
        }
        calificacion.setPuntuacion(request.getPuntuacion());
        calificacion.setComentario(request.getComentario());
        return calificacion;
    }
}
