package com.example.demo.curso.controller;

import com.example.demo.curso.dto.response.EstadisticasResponse;
import com.example.demo.curso.service.EstadisticaService;
import com.example.demo.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EstadisticaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EstadisticaService estadisticaService;

    @InjectMocks
    private EstadisticaController estadisticaController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(estadisticaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void obtenerEstadisticas_exitoso_retorna200() throws Exception {
        EstadisticasResponse response = new EstadisticasResponse(20L, 15L, 5L, 4.25, "Java Microservicios", 6L);

        when(estadisticaService.obtenerEstadisticas()).thenReturn(response);

        mockMvc.perform(get("/api/estadisticas")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalConsultas").value(20))
                .andExpect(jsonPath("$.consultasRespondidas").value(15))
                .andExpect(jsonPath("$.consultasSinResultados").value(5))
                .andExpect(jsonPath("$.promedioCalificaciones").value(4.25))
                .andExpect(jsonPath("$.cursoMasRecomendado").value("Java Microservicios"))
                .andExpect(jsonPath("$.vecesRecomendado").value(6));
    }
}
