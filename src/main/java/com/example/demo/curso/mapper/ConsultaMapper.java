package com.example.demo.curso.mapper;

import com.example.demo.curso.dto.response.ConsultaResponse;
import com.example.demo.curso.dto.response.HistorialResponse;
import com.example.demo.curso.model.Consulta;
import com.example.demo.curso.model.Fuente;
import com.example.demo.curso.model.Recomendacion;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConsultaMapper {

    public ConsultaResponse entityToDto(Consulta consulta) {
        if (consulta == null) {
            return null;
        }
        return new ConsultaResponse(
                consulta.getId(),
                consulta.getEstudianteId(),
                consulta.getPregunta(),
                consulta.getFecha(),
                consulta.getEstado()
        );
    }

    public HistorialResponse toHistorial(Consulta consulta, Recomendacion recomendacion, List<Fuente> fuentes) {
        if (consulta == null) {
            return null;
        }

        String resumen = null;
        if (recomendacion != null && recomendacion.getContenido() != null) {
            String contenido = recomendacion.getContenido();
            resumen = contenido.length() > 200 ? contenido.substring(0, 200) : contenido;
        }

        List<String> cursosRecomendados = Collections.emptyList();
        if (fuentes != null && !fuentes.isEmpty()) {
            cursosRecomendados = fuentes.stream()
                    .filter(f -> f != null && f.getCurso() != null && f.getCurso().getNombre() != null)
                    .map(f -> f.getCurso().getNombre())
                    .distinct()
                    .collect(Collectors.toList());
        }

        return new HistorialResponse(
                consulta.getId(),
                consulta.getPregunta(),
                consulta.getFecha(),
                consulta.getEstado(),
                resumen,
                cursosRecomendados
        );
    }
}
