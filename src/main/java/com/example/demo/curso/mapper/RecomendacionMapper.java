package com.example.demo.curso.mapper;

import com.example.demo.curso.dto.request.RecomendacionRequest;
import com.example.demo.curso.dto.response.RecomendacionResponse;
import com.example.demo.curso.model.Recomendacion;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RecomendacionMapper {

    public RecomendacionResponse entityToDto(Recomendacion recomendacion) {
        if (recomendacion == null) return null;
        return new RecomendacionResponse(
                recomendacion.getId(),
                recomendacion.getConsultaId(),
                recomendacion.getContenido(),
                recomendacion.getFecha(),
                recomendacion.getEstado()
        );
    }

    public Recomendacion requestToEntity(RecomendacionRequest request) {
        if (request == null) return null;
        Recomendacion recomendacion = new Recomendacion();
        recomendacion.setId(request.getId());
        recomendacion.setConsultaId(request.getConsultaId());
        recomendacion.setContenido(request.getContenido());
        recomendacion.setFecha(LocalDateTime.now());
        recomendacion.setEstado("Respondida");
        return recomendacion;
    }
}
