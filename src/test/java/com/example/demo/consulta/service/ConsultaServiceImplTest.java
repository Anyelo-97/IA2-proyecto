package com.example.demo.consulta.service;

import com.example.demo.curso.dto.request.ConsultaRequest;
import com.example.demo.curso.dto.response.ConsultaConResultadoResponse;
import com.example.demo.curso.dto.response.ConsultaResponse;
import com.example.demo.curso.dto.response.HistorialResponse;
import com.example.demo.curso.mapper.ConsultaMapper;
import com.example.demo.curso.mapper.FuenteMapper;
import com.example.demo.curso.model.Consulta;
import com.example.demo.curso.model.Curso;
import com.example.demo.curso.model.Estudiante;
import com.example.demo.curso.model.Fuente;
import com.example.demo.curso.model.Recomendacion;
import com.example.demo.curso.repository.ConsultaRepository;
import com.example.demo.curso.repository.CursoRepository;
import com.example.demo.curso.repository.EstudianteRepository;
import com.example.demo.curso.repository.FuenteRepository;
import com.example.demo.curso.repository.RecomendacionRepository;
import com.example.demo.curso.service.impl.ConsultaServiceImpl;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.n8n.N8nQueryResponse;
import com.example.demo.n8n.N8nQueryService;
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

    @Mock
    private RecomendacionRepository recomendacionRepository;

    @Mock
    private FuenteRepository fuenteRepository;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private EstudianteRepository estudianteRepository;

    @Spy
    private FuenteMapper fuenteMapper;

    @InjectMocks
    private ConsultaServiceImpl consultaService;

    @Test
    void testCrearConsultaExitoso() {
        ConsultaRequest request = new ConsultaRequest("est-123", "¿Cómo inicio en ciencia de datos?");

        Estudiante estudiante = new Estudiante();
        estudiante.setId("est-123");
        estudiante.setNombre("Juan");
        estudiante.setNivelExperiencia("Principiante");
        estudiante.setAreaInteres("Ciencia de Datos");

        when(estudianteRepository.findById("est-123")).thenReturn(Optional.of(estudiante));
        when(consultaRepository.save(any(Consulta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        N8nQueryResponse n8nResponse = new N8nQueryResponse();
        n8nResponse.setConsultaId("c-1");
        n8nResponse.setRespuesta("Te recomiendo el curso de Python");
        n8nResponse.setEstado("Respondida");

        N8nQueryResponse.FuenteN8n fuenteN8n = new N8nQueryResponse.FuenteN8n();
        fuenteN8n.setCursoId("cur-1");
        fuenteN8n.setSimilitud(0.95);
        n8nResponse.setFuentes(List.of(fuenteN8n));

        when(n8nQueryService.consultarRAG(any(), eq("¿Cómo inicio en ciencia de datos?"), eq("Principiante"), eq("Ciencia de Datos")))
                .thenReturn(n8nResponse);

        Curso curso = new Curso();
        curso.setId("cur-1");
        curso.setNombre("Python para Data Science");
        curso.setDescripcion("Curso básico de Python");

        when(cursoRepository.findById("cur-1")).thenReturn(Optional.of(curso));
        when(recomendacionRepository.save(any(Recomendacion.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(fuenteRepository.save(any(Fuente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ConsultaConResultadoResponse response = consultaService.crearConsulta(request);

        assertNotNull(response);
        assertNotNull(response.getConsultaId());
        assertEquals("¿Cómo inicio en ciencia de datos?", response.getPregunta());
        assertEquals("Respondida", response.getEstado());
        assertEquals("Te recomiendo el curso de Python", response.getRespuesta());
        assertNotNull(response.getFuentes());
        assertEquals(1, response.getFuentes().size());
        assertEquals("cur-1", response.getFuentes().get(0).getCursoId());
        assertEquals("Python para Data Science", response.getFuentes().get(0).getCursoNombre());
        assertEquals("Curso básico de Python", response.getFuentes().get(0).getCursoDescripcion());
        assertEquals(0.95, response.getFuentes().get(0).getSimilitud());

        verify(consultaRepository, times(2)).save(any(Consulta.class));
        verify(recomendacionRepository, times(1)).save(any(Recomendacion.class));
        verify(fuenteRepository, times(1)).save(any(Fuente.class));
    }

    @Test
    void testCrearConsultaSinResultados() {
        ConsultaRequest request = new ConsultaRequest("est-123", "¿Pregunta sin catálogo?");

        Estudiante estudiante = new Estudiante();
        estudiante.setId("est-123");
        estudiante.setNombre("Juan");
        estudiante.setNivelExperiencia("Avanzado");
        estudiante.setAreaInteres("Física Cuántica");

        when(estudianteRepository.findById("est-123")).thenReturn(Optional.of(estudiante));
        when(consultaRepository.save(any(Consulta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        N8nQueryResponse n8nResponse = new N8nQueryResponse();
        n8nResponse.setRespuesta("No se encontraron cursos afines.");
        n8nResponse.setEstado("Sin resultados");
        n8nResponse.setFuentes(Collections.emptyList());

        when(n8nQueryService.consultarRAG(any(), any(), any(), any())).thenReturn(n8nResponse);
        when(recomendacionRepository.save(any(Recomendacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ConsultaConResultadoResponse response = consultaService.crearConsulta(request);

        assertNotNull(response);
        assertEquals("Sin resultados", response.getEstado());
        assertEquals("No se encontraron cursos afines.", response.getRespuesta());
        assertTrue(response.getFuentes().isEmpty());

        verify(fuenteRepository, never()).save(any(Fuente.class));
    }

    @Test
    void testCrearConsultaErrorN8n() {
        ConsultaRequest request = new ConsultaRequest("est-123", "¿Pregunta con error?");

        Estudiante estudiante = new Estudiante();
        estudiante.setId("est-123");
        estudiante.setNombre("Juan");
        estudiante.setNivelExperiencia("Principiante");
        estudiante.setAreaInteres("Web");

        when(estudianteRepository.findById("est-123")).thenReturn(Optional.of(estudiante));
        when(consultaRepository.save(any(Consulta.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(n8nQueryService.consultarRAG(any(), any(), any(), any()))
                .thenThrow(new RuntimeException("Conexión rechazada"));

        ConsultaConResultadoResponse response = consultaService.crearConsulta(request);

        assertNotNull(response);
        assertEquals("Error", response.getEstado());
        assertTrue(response.getFuentes().isEmpty());

        verify(consultaRepository, times(2)).save(any(Consulta.class));
    }

    @Test
    void testCrearConsultaEstudianteNoEncontrado() {
        ConsultaRequest request = new ConsultaRequest("no-existe", "¿Pregunta?");

        when(estudianteRepository.findById("no-existe")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> consultaService.crearConsulta(request));

        assertTrue(ex.getMessage().contains("Estudiante no encontrado con ID: no-existe"));
        verify(consultaRepository, never()).save(any(Consulta.class));
    }

    @Test
    void testCrearConsultaPreguntaVaciaLanzaExcepcion() {
        ConsultaRequest request = new ConsultaRequest("est-123", "   ");

        assertThrows(BusinessRuleException.class, () -> consultaService.crearConsulta(request));
        verify(consultaRepository, never()).save(any(Consulta.class));
    }

    @Test
    void testObtenerPorIdExitoso() {
        Consulta consulta = new Consulta(
                "c-1",
                "est-1",
                "Pregunta",
                LocalDateTime.now(),
                "Pendiente"
        );

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
        Consulta consulta = new Consulta(
                "c-1",
                "est-1",
                "Pregunta",
                LocalDateTime.now(),
                "Respondida"
        );

        Recomendacion recomendacion = new Recomendacion(
                "r-1",
                "c-1",
                "Contenido recomendacion",
                LocalDateTime.now(),
                "Respondida"
        );

        Curso curso = new Curso();
        curso.setId("cur-1");
        curso.setNombre("Curso Test");

        Fuente fuente = new Fuente("f-1", recomendacion, curso, 0.9);

        when(consultaRepository.findByEstudianteIdOrderByFechaDesc("est-1"))
                .thenReturn(List.of(consulta));
        when(recomendacionRepository.findByConsultaId("c-1"))
                .thenReturn(Optional.of(recomendacion));
        when(fuenteRepository.findByRecomendacionId("r-1"))
                .thenReturn(List.of(fuente));

        List<HistorialResponse> historial = consultaService.listarHistorial("est-1");

        assertNotNull(historial);
        assertEquals(1, historial.size());
        assertEquals("c-1", historial.get(0).getConsultaId());
        assertEquals("Pregunta", historial.get(0).getPregunta());
        assertEquals("Contenido recomendacion", historial.get(0).getResumen());
        assertEquals(1, historial.get(0).getCursosRecomendados().size());
        assertEquals("Curso Test", historial.get(0).getCursosRecomendados().get(0));
    }
}
