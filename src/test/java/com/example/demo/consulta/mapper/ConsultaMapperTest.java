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
        Consulta consulta = new Consulta(
                "c-1",
                "e-1",
                "¿Cómo aprender Spring Boot?",
                now,
                "Pendiente"
        );

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
        Consulta consulta = new Consulta(
                "c-1",
                "e-1",
                "¿Pregunta test?",
                now,
                "Pendiente"
        );

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
        Consulta consulta = new Consulta(
                "c-2",
                "e-2",
                "¿Ruta para IA?",
                now,
                "Respondida"
        );

        String longContent = "A".repeat(250);
        Recomendacion recomendacion = new Recomendacion(
                "r-1",
                "c-2",
                longContent,
                now,
                "Respondida"
        );

        Curso curso1 = new Curso();
        curso1.setId("cur-1");
        curso1.setNombre("Curso Python");

        Curso curso2 = new Curso();
        curso2.setId("cur-2");
        curso2.setNombre("Curso Machine Learning");

        Fuente fuente1 = new Fuente();
        fuente1.setId("f-1");
        fuente1.setCurso(curso1);
        fuente1.setSimilitud(0.95);

        Fuente fuente2 = new Fuente();
        fuente2.setId("f-2");
        fuente2.setCurso(curso2);
        fuente2.setSimilitud(0.88);

        HistorialResponse historial = mapper.toHistorial(consulta, recomendacion, List.of(fuente1, fuente2));

        assertNotNull(historial);
        assertEquals("c-2", historial.getConsultaId());
        assertEquals(200, historial.getResumen().length());
        assertEquals(2, historial.getCursosRecomendados().size());
        assertTrue(historial.getCursosRecomendados().contains("Curso Python"));
        assertTrue(historial.getCursosRecomendados().contains("Curso Machine Learning"));
    }
}
