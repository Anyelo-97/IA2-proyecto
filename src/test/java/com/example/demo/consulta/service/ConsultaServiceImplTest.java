package com.example.demo.consulta.service;

import com.example.demo.curso.dto.request.ConsultaRequest;
import com.example.demo.curso.dto.response.ConsultaConResultadoResponse;
import com.example.demo.curso.dto.response.ConsultaResponse;
import com.example.demo.curso.dto.response.HistorialResponse;
import com.example.demo.curso.mapper.ConsultaMapper;
import com.example.demo.curso.model.Consulta;
import com.example.demo.curso.repository.ConsultaRepository;
import com.example.demo.curso.service.impl.ConsultaServiceImpl;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.n8n.N8nQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceImplTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Spy
    private ConsultaMapper consultaMapper;

    @Mock
    private N8nQueryService n8nQueryService;

    @InjectMocks
    private ConsultaServiceImpl consultaService;

    @Test
    void testCrearConsultaExitoso() {
        ConsultaRequest request = ConsultaRequest.builder()
                .estudianteId("est-123")
                .pregunta("¿Cómo inicio en ciencia de datos?")
                .build();

        when(consultaRepository.save(any(Consulta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ConsultaConResultadoResponse response = consultaService.crearConsulta(request);

        assertNotNull(response);
        assertNotNull(response.getConsultaId());
        assertEquals("¿Cómo inicio en ciencia de datos?", response.getPregunta());
        assertEquals("Pendiente", response.getEstado());
        assertNull(response.getRespuesta());
        assertNotNull(response.getFuentes());
        assertTrue(response.getFuentes().isEmpty());

        verify(consultaRepository, times(1)).save(any(Consulta.class));
    }

    @Test
    void testCrearConsultaPreguntaVaciaLanzaExcepcion() {
        ConsultaRequest request = ConsultaRequest.builder()
                .estudianteId("est-123")
                .pregunta("   ")
                .build();

        assertThrows(BusinessRuleException.class, () -> consultaService.crearConsulta(request));
        verify(consultaRepository, never()).save(any(Consulta.class));
    }

    @Test
    void testObtenerPorIdExitoso() {
        Consulta consulta = Consulta.builder()
                .id("c-1")
                .estudianteId("est-1")
                .pregunta("Pregunta")
                .fecha(LocalDateTime.now())
                .estado("Pendiente")
                .build();

        when(consultaRepository.findById("c-1")).thenReturn(Optional.of(consulta));

        ConsultaResponse response = consultaService.obtenerPorId("c-1");

        assertNotNull(response);
        assertEquals("c-1", response.getId());
        assertEquals("est-1", response.getEstudianteId());
    }

    @Test
    void testObtenerPorIdNoEncontrado() {
        when(consultaRepository.findById("no-existe")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> consultaService.obtenerPorId("no-existe"));
    }

    @Test
    void testListarHistorial() {
        Consulta consulta = Consulta.builder()
                .id("c-1")
                .estudianteId("est-1")
                .pregunta("Pregunta")
                .fecha(LocalDateTime.now())
                .estado("Pendiente")
                .build();

        when(consultaRepository.findByEstudianteIdOrderByFechaDesc("est-1"))
                .thenReturn(List.of(consulta));

        List<HistorialResponse> historial = consultaService.listarHistorial("est-1");

        assertNotNull(historial);
        assertEquals(1, historial.size());
        assertEquals("c-1", historial.get(0).getConsultaId());
        assertEquals("Pregunta", historial.get(0).getPregunta());
    }
}
