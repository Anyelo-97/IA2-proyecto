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

    @Override
    @Transactional
    public ConsultaConResultadoResponse crearConsulta(ConsultaRequest request) {
        log.info("Creando consulta para estudiante: {}", request.getEstudianteId());

        if (request.getPregunta() == null || request.getPregunta().trim().isEmpty()) {
            throw new BusinessRuleException("La pregunta no puede estar vacía.");
        }

        // TODO: Validar que estudianteId exista cuando la entidad Estudiante esté disponible
        Consulta consulta = new Consulta();
        consulta.setId(UUID.randomUUID().toString());
        consulta.setEstudianteId(request.getEstudianteId().trim());
        consulta.setPregunta(request.getPregunta().trim());
        consulta.setFecha(LocalDateTime.now());
        consulta.setEstado("Pendiente");

        Consulta guardada = consultaRepository.save(consulta);
        log.info("Consulta creada exitosamente con ID: {}", guardada.getId());

        // TODO: Integrar llamada a n8n y persistencia de recomendación cuando el servicio esté disponible
        ConsultaConResultadoResponse res = new ConsultaConResultadoResponse();
        res.setConsultaId(guardada.getId());
        res.setPregunta(guardada.getPregunta());
        res.setEstado(guardada.getEstado());
        res.setRespuesta(null);
        res.setFuentes(Collections.emptyList());
        return res;
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
                    // TODO: Obtener recomendación y fuentes asociadas cuando RecomendacionService esté disponible
                    return consultaMapper.toHistorial(consulta, null, Collections.emptyList());
                })
                .collect(Collectors.toList());
    }
}
