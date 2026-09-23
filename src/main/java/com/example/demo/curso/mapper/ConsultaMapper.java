package com.example.demo.curso.mapper;

import com.example.demo.curso.dto.request.ConsultaRequest;
import com.example.demo.curso.dto.response.ConsultaResponse;
import com.example.demo.curso.model.Consulta;
import com.example.demo.curso.model.Estudiante;
import org.springframework.stereotype.Component;

@Component
public class ConsultaMapper {

    public ConsultaResponse entityToDto(Consulta consulta) {
        if (consulta == null) return null;
        return ConsultaResponse.builder()
                .id(consulta.getId())
                .estudianteId(consulta.getEstudiante() != null ? consulta.getEstudiante().getId() : null)
                .pregunta(consulta.getPregunta())
                .fecha(consulta.getFecha())
                .estado(consulta.getEstado())
                .build();
    }

    public Consulta requestToEntity(ConsultaRequest request) {
        if (request == null) return null;
        return Consulta.builder()
                .id(request.getId())
                .estudiante(request.getEstudianteId() == null ? null :
                        new Estudiante(request.getEstudianteId(), null, null, null, null))
                .pregunta(request.getPregunta())
                .build();
    }
}
