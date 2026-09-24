package com.example.demo.curso.service;

import com.example.demo.curso.dto.request.CalificacionRequest;
import com.example.demo.curso.dto.response.CalificacionResponse;
import com.example.demo.curso.mapper.CalificacionMapper;
import com.example.demo.curso.model.Calificacion;
import com.example.demo.curso.model.Estudiante;
import com.example.demo.curso.model.Recomendacion;
import com.example.demo.curso.repository.CalificacionRepository;
import com.example.demo.curso.repository.EstudianteRepository;
import com.example.demo.curso.repository.RecomendacionRepository;
import com.example.demo.curso.service.impl.CalificacionServiceImpl;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalificacionServiceImplTest {

    @Mock
    private CalificacionRepository calificacionRepository;

    @Mock
    private RecomendacionRepository recomendacionRepository;

    @Mock
    private EstudianteRepository estudianteRepository;

    @Spy
    private CalificacionMapper calificacionMapper;

    @InjectMocks
    private CalificacionServiceImpl calificacionService;

    private CalificacionRequest request;
    private Recomendacion recomendacion;
    private Estudiante estudiante;

    @BeforeEach
    void setUp() {
        request = new CalificacionRequest(null, "est-1", "rec-1", 5, "Excelente");
        recomendacion = new Recomendacion();
        recomendacion.setId("rec-1");

        estudiante = new Estudiante();
        estudiante.setId("est-1");
    }

    @Test
    void calificar_exitoso() {
        when(recomendacionRepository.findById("rec-1")).thenReturn(Optional.of(recomendacion));
        when(calificacionRepository.existsByRecomendacion_Id("rec-1")).thenReturn(false);
        when(estudianteRepository.findById("est-1")).thenReturn(Optional.of(estudiante));
        when(calificacionRepository.save(any(Calificacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CalificacionResponse response = calificacionService.calificar(request);

        assertNotNull(response);
        assertEquals("rec-1", response.getRecomendacionId());
        assertEquals("est-1", response.getEstudianteId());
        assertEquals(5, response.getPuntuacion());
        assertEquals("Excelente", response.getComentario());

        verify(calificacionRepository).save(any(Calificacion.class));
    }

    @Test
    void calificar_recomendacionNoExiste_lanzaResourceNotFoundException() {
        when(recomendacionRepository.findById("rec-1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> calificacionService.calificar(request));
        verify(calificacionRepository, never()).save(any());
    }

    @Test
    void calificar_yaCalificada_lanzaBusinessRuleException() {
        when(recomendacionRepository.findById("rec-1")).thenReturn(Optional.of(recomendacion));
        when(calificacionRepository.existsByRecomendacion_Id("rec-1")).thenReturn(true);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> calificacionService.calificar(request));
        assertEquals("Esta recomendación ya fue calificada", ex.getMessage());
        verify(calificacionRepository, never()).save(any());
    }

    @Test
    void calificar_estudianteNoExiste_lanzaResourceNotFoundException() {
        when(recomendacionRepository.findById("rec-1")).thenReturn(Optional.of(recomendacion));
        when(calificacionRepository.existsByRecomendacion_Id("rec-1")).thenReturn(false);
        when(estudianteRepository.findById("est-1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> calificacionService.calificar(request));
        verify(calificacionRepository, never()).save(any());
    }

    @Test
    void obtenerPorRecomendacion_exitoso() {
        Calificacion calificacion = new Calificacion("cal-1", estudiante, recomendacion, 4, "Bueno");
        when(calificacionRepository.findByRecomendacion_Id("rec-1")).thenReturn(Optional.of(calificacion));

        CalificacionResponse response = calificacionService.obtenerPorRecomendacion("rec-1");

        assertNotNull(response);
        assertEquals("cal-1", response.getId());
        assertEquals("rec-1", response.getRecomendacionId());
        assertEquals("est-1", response.getEstudianteId());
        assertEquals(4, response.getPuntuacion());
        assertEquals("Bueno", response.getComentario());
    }

    @Test
    void obtenerPorRecomendacion_noEncontrada_lanzaResourceNotFoundException() {
        when(calificacionRepository.findByRecomendacion_Id("rec-1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> calificacionService.obtenerPorRecomendacion("rec-1"));
    }
}
