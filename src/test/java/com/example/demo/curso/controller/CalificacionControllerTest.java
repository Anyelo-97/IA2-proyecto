package com.example.demo.curso.controller;

import com.example.demo.curso.dto.request.CalificacionRequest;
import com.example.demo.curso.dto.response.CalificacionResponse;
import com.example.demo.curso.service.CalificacionService;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.exception.GlobalExceptionHandler;
import com.example.demo.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CalificacionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CalificacionService calificacionService;

    @InjectMocks
    private CalificacionController calificacionController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(calificacionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void calificar_exitoso_retorna201() throws Exception {
        CalificacionRequest request = new CalificacionRequest(null, "est-1", "rec-1", 5, "Excelente");
        CalificacionResponse response = new CalificacionResponse("cal-1", "est-1", "rec-1", 5, "Excelente");

        when(calificacionService.calificar(any(CalificacionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/calificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("cal-1"))
                .andExpect(jsonPath("$.estudianteId").value("est-1"))
                .andExpect(jsonPath("$.recomendacionId").value("rec-1"))
                .andExpect(jsonPath("$.puntuacion").value(5))
                .andExpect(jsonPath("$.comentario").value("Excelente"));
    }

    @Test
    void calificar_datosInvalidos_retorna400() throws Exception {
        // Puntuacion fuera de rango (1-5), campos requeridos nulos
        CalificacionRequest request = new CalificacionRequest(null, null, "", 10, null);

        mockMvc.perform(post("/api/calificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"));
    }

    @Test
    void calificar_yaCalificada_retorna400() throws Exception {
        CalificacionRequest request = new CalificacionRequest(null, "est-1", "rec-1", 5, "Excelente");

        when(calificacionService.calificar(any(CalificacionRequest.class)))
                .thenThrow(new BusinessRuleException("Esta recomendación ya fue calificada"));

        mockMvc.perform(post("/api/calificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BUSINESS_RULE_VIOLATION"))
                .andExpect(jsonPath("$.message").value("Esta recomendación ya fue calificada"));
    }

    @Test
    void obtenerPorRecomendacion_exitoso_retorna200() throws Exception {
        CalificacionResponse response = new CalificacionResponse("cal-1", "est-1", "rec-1", 5, "Excelente");

        when(calificacionService.obtenerPorRecomendacion("rec-1")).thenReturn(response);

        mockMvc.perform(get("/api/calificaciones/rec-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cal-1"))
                .andExpect(jsonPath("$.recomendacionId").value("rec-1"))
                .andExpect(jsonPath("$.puntuacion").value(5));
    }

    @Test
    void obtenerPorRecomendacion_noEncontrada_retorna404() throws Exception {
        when(calificacionService.obtenerPorRecomendacion("rec-999"))
                .thenThrow(new ResourceNotFoundException("Calificación", "rec-999"));

        mockMvc.perform(get("/api/calificaciones/rec-999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));
    }
}
