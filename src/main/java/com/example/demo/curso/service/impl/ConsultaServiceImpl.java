package com.example.demo.curso.service.impl;

import com.example.demo.curso.dto.request.ConsultaRequest;
import com.example.demo.curso.dto.response.ConsultaConResultadoResponse;
import com.example.demo.curso.dto.response.ConsultaResponse;
import com.example.demo.curso.dto.response.HistorialResponse;
import com.example.demo.curso.mapper.ConsultaMapper;
import com.example.demo.curso.model.Consulta;
import com.example.demo.curso.repository.ConsultaRepository;
import com.example.demo.curso.service.ConsultaService;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.n8n.N8nQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultaServiceImpl implements ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final ConsultaMapper consultaMapper;
    private final N8nQueryService n8nQueryService;
    // Injected in commit 6: RecomendacionService recomendacionService

    @Override
    @Transactional
    public ConsultaConResultadoResponse crearConsulta(ConsultaRequest request) {
        log.info("Creando consulta para estudiante: {}", request.getEstudianteId());

        // a) Validate pregunta is not blank (Jakarta already does this, but double check)
        if (request.getPregunta() == null || request.getPregunta().trim().isEmpty()) {
            throw new BusinessRuleException("La pregunta no puede estar vacía.");
        }

        // b) // TODO: validate estudianteId exists when Estudiante entity is available

        // c) Create Consulta with id=UUID, estado="Pendiente", fecha=LocalDateTime.now()
        Consulta consulta = Consulta.builder()
                .id(UUID.randomUUID().toString())
                .estudianteId(request.getEstudianteId().trim())
                .pregunta(request.getPregunta().trim())
                .fecha(LocalDateTime.now())
                .estado("Pendiente")
                .build();

        // d) Save to DB
        Consulta guardada = consultaRepository.save(consulta);
        log.info("Consulta creada exitosamente con ID: {}", guardada.getId());

        // e) Return ConsultaConResultadoResponse with estado="Pendiente", null respuesta, empty fuentes
        // (the n8n call + recommendation saving will be wired in commit 6 when RecomendacionService exists)
        return ConsultaConResultadoResponse.builder()
                .consultaId(guardada.getId())
                .pregunta(guardada.getPregunta())
                .estado(guardada.getEstado())
                .respuesta(null)
                .fuentes(Collections.emptyList())
                .build();
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

        // a) Find all consultas for the estudiante, ordered by fecha desc
        List<Consulta> consultas = consultaRepository.findByEstudianteIdOrderByFechaDesc(estudianteId);

        // b) For each consulta, find its recomendacion and fuentes (if they exist)
        // c) Map to HistorialResponse using ConsultaMapper.toHistorial()
        // d) Return the list
        return consultas.stream()
                .map(consulta -> {
                    // TODO: When RecomendacionService is available in commit 6, fetch recomendacion and fuentes
                    return consultaMapper.toHistorial(consulta, null, Collections.emptyList());
                })
                .collect(Collectors.toList());
    }
}
