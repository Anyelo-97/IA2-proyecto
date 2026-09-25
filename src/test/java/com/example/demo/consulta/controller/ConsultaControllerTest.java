package com.example.demo.consulta.controller;

import com.example.demo.curso.controller.ConsultaController;
import com.example.demo.curso.dto.request.ConsultaRequest;
import com.example.demo.curso.dto.response.ConsultaConResultadoResponse;
import com.example.demo.curso.dto.response.ConsultaResponse;
import com.example.demo.curso.dto.response.HistorialResponse;
import com.example.demo.curso.service.ConsultaService;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ConsultaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ConsultaService consultaService;

    @InjectMocks
    private ConsultaController consultaController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(consultaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testCrearConsultaEndpoint() throws Exception {
        ConsultaRequest request = new ConsultaRequest("est-1", "¿Cómo funciona el RAG?");

        ConsultaConResultadoResponse response = new ConsultaConResultadoResponse(
                "c-1",
                "¿Cómo funciona el RAG?",
                "Pendiente",
                null,
                Collections.emptyList()
        );

        when(consultaService.crearConsulta(any(ConsultaRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.consultaId").value("c-1"))
                .andExpect(jsonPath("$.pregunta").value("¿Cómo funciona el RAG?"))
                .andExpect(jsonPath("$.estado").value("Pendiente"))
                .andExpect(jsonPath("$.respuesta").doesNotExist())
                .andExpect(jsonPath("$.fuentes").isArray());
    }

    @Test
    void testCrearConsultaValidacionFalla() throws Exception {
        ConsultaRequest invalidRequest = new ConsultaRequest("", "");

        mockMvc.perform(post("/api/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCrearConsultaConIdEstudianteVacioFalla() throws Exception {
        ConsultaRequest invalidRequest = new ConsultaRequest("   ", "¿Cómo funciona el RAG?");

        mockMvc.perform(post("/api/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.estudianteId").value("El ID del estudiante no puede estar vacío"));
    }

    @Test
    void testObtenerPorIdEndpoint() throws Exception {
        ConsultaResponse response = new ConsultaResponse(
                "c-1",
                "est-1",
                "¿Cómo aprender Java?",
                LocalDateTime.now(),
                "Pendiente"
        );

        when(consultaService.obtenerPorId("c-1")).thenReturn(response);

        mockMvc.perform(get("/api/consultas/c-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("c-1"))
                .andExpect(jsonPath("$.estudianteId").value("est-1"));
    }

    @Test
    void testObtenerPorIdNoEncontrado() throws Exception {
        when(consultaService.obtenerPorId("no-existe"))
                .thenThrow(new ResourceNotFoundException("Consulta", "no-existe"));

        mockMvc.perform(get("/api/consultas/no-existe"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testListarHistorialEndpoint() throws Exception {
        HistorialResponse item = new HistorialResponse(
                "c-1",
                "¿Pregunta?",
                LocalDateTime.now(),
                "Pendiente",
                null,
                Collections.emptyList()
        );

        when(consultaService.listarHistorial("est-1")).thenReturn(List.of(item));

        mockMvc.perform(get("/api/estudiantes/est-1/historial"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].consultaId").value("c-1"))
                .andExpect(jsonPath("$[0].pregunta").value("¿Pregunta?"));
    }
}
