package com.example.demo.curso.mapper;

import com.example.demo.curso.dto.request.ConsultaRequest;
import com.example.demo.curso.dto.response.ConsultaResponse;
import com.example.demo.curso.dto.response.FuenteResponse;
import com.example.demo.curso.dto.response.HistorialResponse;
import com.example.demo.curso.model.Consulta;
import com.example.demo.curso.model.Curso;
import com.example.demo.curso.model.Fuente;
import com.example.demo.curso.model.Recomendacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class ConsultaMapper {

    private final FuenteMapper fuenteMapper;

    public ConsultaMapper() {
        this(new FuenteMapper());
    }

    @Autowired
    public ConsultaMapper(FuenteMapper fuenteMapper) {
        this.fuenteMapper = fuenteMapper != null ? fuenteMapper : new FuenteMapper();
    }

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

        String resumen = construirResumen(recomendacion);
        List<String> cursosRecomendados = extraerNombresCursos(fuentes);
        HistorialResponse.RecomendacionHistorialDTO recDto = construirRecomendacionDto(recomendacion, fuentes);

        return new HistorialResponse(
                consulta.getId(),
                consulta.getPregunta(),
                consulta.getFecha(),
                consulta.getEstado(),
                resumen,
                cursosRecomendados,
                recDto
        );
    }

    public Consulta requestToEntity(ConsultaRequest request) {
        if (request == null) {
            return null;
        }
        Consulta consulta = new Consulta();
        consulta.setEstudianteId(request.getEstudianteId());
        consulta.setPregunta(request.getPregunta());
        consulta.setFecha(LocalDateTime.now());
        consulta.setEstado("Pendiente");
        return consulta;
    }

    private String construirResumen(Recomendacion recomendacion) {
        if (recomendacion == null || recomendacion.getContenido() == null) {
            return null;
        }
        String contenido = recomendacion.getContenido();
        return contenido.length() > 200 ? contenido.substring(0, 200) : contenido;
    }

    private List<String> extraerNombresCursos(List<Fuente> fuentes) {
        if (fuentes == null || fuentes.isEmpty()) {
            return Collections.emptyList();
        }
        return fuentes.stream()
                .filter(Objects::nonNull)
                .map(Fuente::getCurso)
                .filter(Objects::nonNull)
                .map(Curso::getNombre)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private HistorialResponse.RecomendacionHistorialDTO construirRecomendacionDto(
            Recomendacion recomendacion, List<Fuente> fuentes) {

        if (recomendacion == null) {
            return null;
        }

        List<FuenteResponse> fuentesDto = (fuentes == null || fuentes.isEmpty())
                ? Collections.emptyList()
                : fuentes.stream()
                        .filter(Objects::nonNull)
                        .map(fuenteMapper::entityToDto)
                        .toList();

        return new HistorialResponse.RecomendacionHistorialDTO(
                recomendacion.getId(),
                recomendacion.getContenido(),
                recomendacion.getEstado(),
                fuentesDto,
                null
        );
    }
}
