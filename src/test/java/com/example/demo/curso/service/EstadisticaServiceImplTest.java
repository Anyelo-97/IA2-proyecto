package com.example.demo.curso.service;

import com.example.demo.curso.dto.response.EstadisticasResponse;
import com.example.demo.curso.repository.CalificacionRepository;
import com.example.demo.curso.repository.ConsultaRepository;
import com.example.demo.curso.repository.FuenteRepository;
import com.example.demo.curso.service.impl.EstadisticaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstadisticaServiceImplTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private CalificacionRepository calificacionRepository;

    @Mock
    private FuenteRepository fuenteRepository;

    @InjectMocks
    private EstadisticaServiceImpl estadisticaService;

    @Test
    void obtenerEstadisticas_conDatos_calculaMetricasCorrectamente() {
        when(consultaRepository.count()).thenReturn(15L);
        when(consultaRepository.countByEstado("Respondida")).thenReturn(10L);
        when(consultaRepository.countByEstado("Sin resultados")).thenReturn(5L);
        when(calificacionRepository.promedioCalificaciones()).thenReturn(4.567);

        List<Object[]> topCursos = Collections.singletonList(new Object[]{"Spring Boot Avanzado", 8L});
        when(fuenteRepository.findCursoMasRecomendado(PageRequest.of(0, 1))).thenReturn(topCursos);

        EstadisticasResponse response = estadisticaService.obtenerEstadisticas();

        assertNotNull(response);
        assertEquals(15L, response.getTotalConsultas());
        assertEquals(10L, response.getConsultasRespondidas());
        assertEquals(5L, response.getConsultasSinResultados());
        assertEquals(4.57, response.getPromedioCalificaciones());
        assertEquals("Spring Boot Avanzado", response.getCursoMasRecomendado());
        assertEquals(8L, response.getVecesRecomendado());

        verify(consultaRepository).count();
        verify(consultaRepository).countByEstado("Respondida");
        verify(consultaRepository).countByEstado("Sin resultados");
        verify(calificacionRepository).promedioCalificaciones();
        verify(fuenteRepository).findCursoMasRecomendado(PageRequest.of(0, 1));
    }

    @Test
    void obtenerEstadisticas_sinDatos_retornaValoresPorDefecto() {
        when(consultaRepository.count()).thenReturn(0L);
        when(consultaRepository.countByEstado("Respondida")).thenReturn(0L);
        when(consultaRepository.countByEstado("Sin resultados")).thenReturn(0L);
        when(calificacionRepository.promedioCalificaciones()).thenReturn(null);
        when(fuenteRepository.findCursoMasRecomendado(PageRequest.of(0, 1))).thenReturn(Collections.emptyList());

        EstadisticasResponse response = estadisticaService.obtenerEstadisticas();

        assertNotNull(response);
        assertEquals(0L, response.getTotalConsultas());
        assertEquals(0L, response.getConsultasRespondidas());
        assertEquals(0L, response.getConsultasSinResultados());
        assertEquals(0.0, response.getPromedioCalificaciones());
        assertEquals("Ninguno", response.getCursoMasRecomendado());
        assertEquals(0L, response.getVecesRecomendado());

        verify(consultaRepository).count();
        verify(consultaRepository).countByEstado("Respondida");
        verify(consultaRepository).countByEstado("Sin resultados");
        verify(calificacionRepository).promedioCalificaciones();
        verify(fuenteRepository).findCursoMasRecomendado(PageRequest.of(0, 1));
    }
}
