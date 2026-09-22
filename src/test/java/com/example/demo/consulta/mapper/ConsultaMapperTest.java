package com.example.demo.consulta.mapper;

import com.example.demo.curso.dto.response.ConsultaResponse;
import com.example.demo.curso.dto.response.HistorialResponse;
import com.example.demo.curso.mapper.ConsultaMapper;
import com.example.demo.curso.model.Consulta;
import com.example.demo.curso.model.Curso;
import com.example.demo.curso.model.Fuente;
import com.example.demo.curso.model.Recomendacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConsultaMapperTest {

    private ConsultaMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ConsultaMapper();
    }

    @Test
    void testEntityToDto() {
        LocalDateTime now = LocalDateTime.now();
        Consulta consulta = Consulta.builder()
                .id("c-1")
                .estudianteId("e-1")
                .pregunta("¿Cómo aprender Spring Boot?")
                .fecha(now)
                .estado("Pendiente")
                .build();

        ConsultaResponse dto = mapper.entityToDto(consulta);

        assertNotNull(dto);
        assertEquals("c-1", dto.getId());
        assertEquals("e-1", dto.getEstudianteId());
        assertEquals("¿Cómo aprender Spring Boot?", dto.getPregunta());
        assertEquals(now, dto.getFecha());
        assertEquals("Pendiente", dto.getEstado());
    }

    @Test
    void testEntityToDtoNull() {
        assertNull(mapper.entityToDto(null));
    }

    @Test
    void testToHistorialWithNullRecomendacionAndFuentes() {
        LocalDateTime now = LocalDateTime.now();
        Consulta consulta = Consulta.builder()
                .id("c-1")
                .estudianteId("e-1")
                .pregunta("¿Pregunta test?")
                .fecha(now)
                .estado("Pendiente")
                .build();

        HistorialResponse historial = mapper.toHistorial(consulta, null, null);

        assertNotNull(historial);
        assertEquals("c-1", historial.getConsultaId());
        assertEquals("¿Pregunta test?", historial.getPregunta());
        assertEquals(now, historial.getFecha());
        assertEquals("Pendiente", historial.getEstado());
        assertNull(historial.getResumen());
        assertTrue(historial.getCursosRecomendados().isEmpty());
    }

    @Test
    void testToHistorialWithTruncatedResumenAndCursos() {
        LocalDateTime now = LocalDateTime.now();
        Consulta consulta = Consulta.builder()
                .id("c-2")
                .estudianteId("e-2")
                .pregunta("¿Ruta para IA?")
                .fecha(now)
                .estado("Respondida")
                .build();

        String longContent = "A".repeat(250);
        Recomendacion recomendacion = Recomendacion.builder()
                .id("r-1")
                .consultaId("c-2")
                .contenido(longContent)
                .fecha(now)
                .estado("Respondida")
                .build();

        Curso curso1 = Curso.builder().id("cur-1").nombre("Curso Python").build();
        Curso curso2 = Curso.builder().id("cur-2").nombre("Curso Machine Learning").build();

        Fuente fuente1 = Fuente.builder().id("f-1").curso(curso1).similitud(0.95).build();
        Fuente fuente2 = Fuente.builder().id("f-2").curso(curso2).similitud(0.88).build();

        HistorialResponse historial = mapper.toHistorial(consulta, recomendacion, List.of(fuente1, fuente2));

        assertNotNull(historial);
        assertEquals("c-2", historial.getConsultaId());
        assertEquals(200, historial.getResumen().length());
        assertEquals(2, historial.getCursosRecomendados().size());
        assertTrue(historial.getCursosRecomendados().contains("Curso Python"));
        assertTrue(historial.getCursosRecomendados().contains("Curso Machine Learning"));
    }
}
