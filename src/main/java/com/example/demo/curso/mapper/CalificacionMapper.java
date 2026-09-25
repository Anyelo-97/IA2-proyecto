package com.example.demo.curso.mapper;

import com.example.demo.curso.dto.request.CalificacionRequest;
import com.example.demo.curso.dto.response.CalificacionResponse;
import com.example.demo.curso.model.Calificacion;
import com.example.demo.curso.model.Recomendacion;
import com.example.demo.curso.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class CalificacionMapper {

    public CalificacionResponse entityToDto(Calificacion calificacion) {
        if (calificacion == null) return null;
        String estudianteId = calificacion.getEstudiante() != null ? calificacion.getEstudiante().getId() : null;
        String recomendacionId = calificacion.getRecomendacion() != null ? calificacion.getRecomendacion().getId() : null;
        return new CalificacionResponse(
                calificacion.getId(),
                estudianteId,
                recomendacionId,
                calificacion.getPuntuacion(),
                calificacion.getComentario()
        );
    }

    public Calificacion requestToEntity(CalificacionRequest request) {
        if (request == null) return null;
        Calificacion calificacion = new Calificacion();
        calificacion.setId(request.getId());
        calificacion.setEstudiante(request.getEstudianteId() == null ? null : new Usuario(request.getEstudianteId(), null, null, null));
        if (request.getRecomendacionId() != null) {
            Recomendacion recomendacion = new Recomendacion();
            recomendacion.setId(request.getRecomendacionId());
            calificacion.setRecomendacion(recomendacion);
        }
        calificacion.setPuntuacion(request.getPuntuacion());
        calificacion.setComentario(request.getComentario());
        return calificacion;
    }
}
