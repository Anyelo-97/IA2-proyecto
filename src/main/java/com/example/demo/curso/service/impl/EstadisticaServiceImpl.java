package com.example.demo.curso.service.impl;

import com.example.demo.curso.dto.response.EstadisticasResponse;
import com.example.demo.curso.repository.CalificacionRepository;
import com.example.demo.curso.repository.ConsultaRepository;
import com.example.demo.curso.repository.FuenteRepository;
import com.example.demo.curso.service.EstadisticaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstadisticaServiceImpl implements EstadisticaService {

    private final ConsultaRepository consultaRepository;
    private final CalificacionRepository calificacionRepository;
    private final FuenteRepository fuenteRepository;

    @Override
    @Transactional(readOnly = true)
    public EstadisticasResponse obtenerEstadisticas() {
        log.info("Calculando metricas para el dashboard de estadisticas");

        long totalConsultas = consultaRepository.count();
        long consultasRespondidas = consultaRepository.countByEstado("Respondida");
        long consultasSinResultados = consultaRepository.countByEstado("Sin resultados");

        Double rawAvg = calificacionRepository.promedioCalificaciones();
        double promedio = (rawAvg != null) ? Math.round(rawAvg * 100.0) / 100.0 : 0.0;

        List<Object[]> topCursos = fuenteRepository.findCursoMasRecomendado(PageRequest.of(0, 1));
        String cursoMasRecomendado = "Ninguno";
        long vecesRecomendado = 0L;
        if (!topCursos.isEmpty()) {
            Object[] row = topCursos.get(0);
            cursoMasRecomendado = (String) row[0];
            vecesRecomendado = ((Number) row[1]).longValue();
        }

        return new EstadisticasResponse(
                totalConsultas,
                consultasRespondidas,
                consultasSinResultados,
                promedio,
                cursoMasRecomendado,
                vecesRecomendado
        );
    }
}
