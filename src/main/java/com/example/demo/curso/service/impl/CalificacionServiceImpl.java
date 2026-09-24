package com.example.demo.curso.service.impl;

import com.example.demo.curso.dto.request.CalificacionRequest;
import com.example.demo.curso.dto.response.CalificacionResponse;
import com.example.demo.curso.mapper.CalificacionMapper;
import com.example.demo.curso.model.Calificacion;
import com.example.demo.curso.model.Estudiante;
import com.example.demo.curso.model.Recomendacion;
import com.example.demo.curso.repository.CalificacionRepository;
import com.example.demo.curso.repository.EstudianteRepository;
import com.example.demo.curso.repository.RecomendacionRepository;
import com.example.demo.curso.service.CalificacionService;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalificacionServiceImpl implements CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final RecomendacionRepository recomendacionRepository;
    private final EstudianteRepository estudianteRepository;
    private final CalificacionMapper calificacionMapper;

    @Override
    @Transactional
    public CalificacionResponse calificar(CalificacionRequest request) {
        log.info("Calificando recomendación: {}", request.getRecomendacionId());

        Recomendacion recomendacion = recomendacionRepository.findById(request.getRecomendacionId())
                .orElseThrow(() -> new ResourceNotFoundException("Recomendación", request.getRecomendacionId()));

        if (calificacionRepository.existsByRecomendacion_Id(request.getRecomendacionId())) {
            throw new BusinessRuleException("Esta recomendación ya fue calificada");
        }

        Estudiante estudiante = estudianteRepository.findById(request.getEstudianteId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", request.getEstudianteId()));

        Calificacion calificacion = new Calificacion(
                UUID.randomUUID().toString(),
                estudiante,
                recomendacion,
                request.getPuntuacion(),
                request.getComentario()
        );

        Calificacion saved = calificacionRepository.save(calificacion);
        return calificacionMapper.entityToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CalificacionResponse obtenerPorRecomendacion(String recomendacionId) {
        log.info("Buscando calificación para la recomendación: {}", recomendacionId);

        Calificacion calificacion = calificacionRepository.findByRecomendacion_Id(recomendacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Calificación", recomendacionId));

        return calificacionMapper.entityToDto(calificacion);
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<CalificacionResponse> listar() {
        return calificacionRepository.findAll().stream()
                .map(calificacionMapper::entityToDto)
                .toList();
    }
}
