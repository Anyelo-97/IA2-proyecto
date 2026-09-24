package com.example.demo.curso.service.impl;

import com.example.demo.curso.dto.request.ConsultaRequest;
import com.example.demo.curso.dto.response.ConsultaConResultadoResponse;
import com.example.demo.curso.dto.response.ConsultaResponse;
import com.example.demo.curso.dto.response.FuenteResponse;
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
import com.example.demo.curso.service.ConsultaService;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.n8n.N8nQueryResponse;
import com.example.demo.n8n.N8nQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultaServiceImpl implements ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final ConsultaMapper consultaMapper;
    private final N8nQueryService n8nQueryService;
    private final RecomendacionRepository recomendacionRepository;
    private final FuenteRepository fuenteRepository;
    private final CursoRepository cursoRepository;
    private final EstudianteRepository estudianteRepository;
    private FuenteMapper fuenteMapper = new FuenteMapper();

    @Autowired
    public void setFuenteMapper(FuenteMapper fuenteMapper) {
        if (fuenteMapper != null) {
            this.fuenteMapper = fuenteMapper;
        }
    }

    @Override
    @Transactional
    public ConsultaConResultadoResponse crearConsulta(ConsultaRequest request) {
        log.info("Creando consulta para estudiante: {}", request.getEstudianteId());

        if (request.getPregunta() == null || request.getPregunta().trim().isEmpty()) {
            throw new BusinessRuleException("La pregunta no puede estar vacía.");
        }

        Estudiante estudiante = estudianteRepository.findById(request.getEstudianteId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + request.getEstudianteId()));

        Consulta consulta = new Consulta();
        consulta.setId(UUID.randomUUID().toString());
        consulta.setEstudianteId(estudiante.getId());
        consulta.setPregunta(request.getPregunta().trim());
        consulta.setFecha(LocalDateTime.now());
        consulta.setEstado("Pendiente");

        consulta = consultaRepository.save(consulta);
        log.info("Consulta creada exitosamente con ID: {}", consulta.getId());

        try {
            N8nQueryResponse response = n8nQueryService.consultarRAG(
                    consulta.getId(),
                    consulta.getPregunta(),
                    estudiante.getNivelExperiencia(),
                    estudiante.getAreaInteres()
            );

            if (response == null
                    || "Sin resultados".equalsIgnoreCase(response.getEstado())
                    || response.getFuentes() == null
                    || response.getFuentes().isEmpty()) {

                Recomendacion recomendacion = new Recomendacion();
                recomendacion.setId(UUID.randomUUID().toString());
                recomendacion.setConsultaId(consulta.getId());
                recomendacion.setContenido(response != null && response.getRespuesta() != null ? response.getRespuesta() : "");
                recomendacion.setFecha(LocalDateTime.now());
                recomendacion.setEstado("Sin resultados");
                recomendacionRepository.save(recomendacion);

                consulta.setEstado("Sin resultados");
                consultaRepository.save(consulta);

                ConsultaConResultadoResponse res = new ConsultaConResultadoResponse();
                res.setConsultaId(consulta.getId());
                res.setRecomendacionId(recomendacion.getId());
                res.setPregunta(consulta.getPregunta());
                res.setEstado("Sin resultados");
                res.setRespuesta(recomendacion.getContenido());
                res.setFuentes(Collections.emptyList());
                return res;
            }

            String contenido = response.getRespuesta() != null ? response.getRespuesta() : "";
            if (contenido.length() > 2000) {
                contenido = contenido.substring(0, 1997) + "...";
            }

            Recomendacion recomendacion = new Recomendacion();
            recomendacion.setId(UUID.randomUUID().toString());
            recomendacion.setConsultaId(consulta.getId());
            recomendacion.setContenido(contenido);
            recomendacion.setFecha(LocalDateTime.now());
            recomendacion.setEstado("Respondida");
            Recomendacion recomendacionGuardada = recomendacionRepository.save(recomendacion);

            List<FuenteResponse> fuentesResponse = new ArrayList<>();
            for (N8nQueryResponse.FuenteN8n fuenteN8n : response.getFuentes()) {
                Optional<Curso> cursoOpt = cursoRepository.findById(fuenteN8n.getCursoId());
                if (cursoOpt.isPresent()) {
                    Curso curso = cursoOpt.get();
                    Fuente fuente = new Fuente();
                    fuente.setId(UUID.randomUUID().toString());
                    fuente.setRecomendacion(recomendacionGuardada);
                    fuente.setCurso(curso);
                    fuente.setSimilitud(fuenteN8n.getSimilitud());
                    Fuente fuenteGuardada = fuenteRepository.save(fuente);
                    fuentesResponse.add(fuenteMapper.entityToDto(fuenteGuardada));
                }
            }

            consulta.setEstado("Respondida");
            consultaRepository.save(consulta);

            ConsultaConResultadoResponse res = new ConsultaConResultadoResponse();
            res.setConsultaId(consulta.getId());
            res.setRecomendacionId(recomendacionGuardada.getId());
            res.setPregunta(consulta.getPregunta());
            res.setEstado("Respondida");
            res.setRespuesta(recomendacionGuardada.getContenido());
            res.setFuentes(fuentesResponse);
            return res;

        } catch (Exception e) {
            log.error("Error al procesar consulta con n8n para consulta ID: {}", consulta.getId(), e);
            consulta.setEstado("Error");
            consultaRepository.save(consulta);

            ConsultaConResultadoResponse errorRes = new ConsultaConResultadoResponse();
            errorRes.setConsultaId(consulta.getId());
            errorRes.setPregunta(consulta.getPregunta());
            errorRes.setEstado("Error");
            errorRes.setRespuesta("Error al procesar la recomendación con el servicio de IA.");
            errorRes.setFuentes(Collections.emptyList());
            return errorRes;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ConsultaResponse obtenerPorId(String id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta", id));
        return consultaMapper.entityToDto(consulta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialResponse> listarHistorial(String estudianteId) {
        log.info("Listando historial para estudiante: {}", estudianteId);

        List<Consulta> consultas = consultaRepository.findByEstudianteIdOrderByFechaDesc(estudianteId);

        return consultas.stream()
                .map(consulta -> {
                    Recomendacion recomendacion = recomendacionRepository.findByConsultaId(consulta.getId())
                            .orElse(null);
                    List<Fuente> fuentes = Collections.emptyList();
                    if (recomendacion != null) {
                        fuentes = fuenteRepository.findByRecomendacionId(recomendacion.getId());
                    }
                    return consultaMapper.toHistorial(consulta, recomendacion, fuentes);
                })
                .collect(Collectors.toList());
    }
}
