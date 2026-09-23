package com.example.demo.curso.mapper;

import com.example.demo.curso.dto.request.RecomendacionRequest;
import com.example.demo.curso.dto.response.RecomendacionResponse;
import com.example.demo.curso.model.Consulta;
import com.example.demo.curso.model.Recomendacion;
import org.springframework.stereotype.Component;

@Component
public class RecomendacionMapper {

    public RecomendacionResponse entityToDto(Recomendacion recomendacion) {
        if (recomendacion == null) return null;
        return RecomendacionResponse.builder()
                .id(recomendacion.getId())
                .consultaId(recomendacion.getConsulta() != null ? recomendacion.getConsulta().getId() : null)
                .contenido(recomendacion.getContenido())
                .fecha(recomendacion.getFecha())
                .estado(recomendacion.getEstado())
                .build();
    }

    public Recomendacion requestToEntity(RecomendacionRequest request) {
        if (request == null) return null;
        Recomendacion recomendacion = Recomendacion.builder()
                .id(request.getId())
                .contenido(request.getContenido())
                .build();
        if (request.getConsultaId() != null) {
            Consulta consulta = new Consulta();
            consulta.setId(request.getConsultaId());
            recomendacion.setConsulta(consulta);
        }
        return recomendacion;
    }
}
