package com.example.demo.curso.service.impl;

import com.example.demo.curso.dto.request.CursoRequest;
import com.example.demo.curso.dto.response.CursoResponse;
import com.example.demo.curso.mapper.CursoMapper;
import com.example.demo.curso.model.Categoria;
import com.example.demo.curso.model.Curso;
import com.example.demo.curso.model.NivelDificultad;
import com.example.demo.curso.repository.CategoriaRepository;
import com.example.demo.curso.repository.CursoRepository;
import com.example.demo.curso.repository.NivelDificultadRepository;
import com.example.demo.curso.service.CursoService;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.n8n.N8nSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;
    private final CategoriaRepository categoriaRepository;
    private final NivelDificultadRepository nivelRepository;
    private final CursoMapper cursoMapper;
    private final N8nSyncService n8nSyncService;

    @Override
    @Transactional
    public CursoResponse crearCurso(CursoRequest request) {
        log.info("Creando nuevo curso: {}", request.getNombre());

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", request.getCategoriaId()));

        NivelDificultad nivel = nivelRepository.findById(request.getNivelId())
                .orElseThrow(() -> new ResourceNotFoundException("Nivel de dificultad", request.getNivelId()));

        if (request.getDuracion() <= 0) {
            throw new BusinessRuleException("La duración del curso debe ser mayor que cero.");
        }

        // Generar UUID si no se proporciona uno explícito
        String id = request.getId();
        if (id == null || id.trim().isEmpty()) {
            id = UUID.randomUUID().toString();
        } else if (cursoRepository.existsById(id)) {
            throw new BusinessRuleException(String.format("Ya existe un curso con el id '%s'.", id));
        }

        Curso curso = new Curso();
        curso.setId(id);
        curso.setNombre(request.getNombre().trim());
        curso.setDescripcion(request.getDescripcion().trim());
        curso.setCategoria(categoria);
        curso.setNivel(nivel);
        curso.setDuracion(request.getDuracion());
        if (request.getModalidad() != null && !request.getModalidad().isBlank()) {
            curso.setModalidad(request.getModalidad().trim());
        }
        if (request.getPrecio() != null) {
            curso.setPrecio(request.getPrecio());
        }
        curso.setEstado(request.getEstado() == null || request.getEstado());
        Curso guardado = cursoRepository.save(curso);
        CursoResponse response = cursoMapper.entityToDto(guardado);
        n8nSyncService.sincronizarCurso(response, "CREAR");
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public CursoResponse obtenerCursoPorId(String id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso", id));
        return cursoMapper.entityToDto(curso);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponse> listarCursos(Boolean estado) {
        List<Curso> cursos = (estado != null)
                ? cursoRepository.findByEstado(estado)
                : cursoRepository.findAll();

        return cursos.stream()
                .map(cursoMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CursoResponse actualizarCurso(String id, CursoRequest request) {
        log.info("Actualizando curso con ID: {}", id);

        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso", id));

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", request.getCategoriaId()));

        NivelDificultad nivel = nivelRepository.findById(request.getNivelId())
                .orElseThrow(() -> new ResourceNotFoundException("Nivel de dificultad", request.getNivelId()));

        if (request.getDuracion() <= 0) {
            throw new BusinessRuleException("La duración del curso debe ser mayor que cero.");
        }

        curso.setNombre(request.getNombre().trim());
        curso.setDescripcion(request.getDescripcion().trim());
        curso.setCategoria(categoria);
        curso.setNivel(nivel);
        curso.setDuracion(request.getDuracion());
        if (request.getModalidad() != null && !request.getModalidad().isBlank()) {
            curso.setModalidad(request.getModalidad().trim());
        }
        if (request.getPrecio() != null) {
            curso.setPrecio(request.getPrecio());
        }

        if (request.getEstado() != null) {
            curso.setEstado(request.getEstado());
        }

        Curso actualizado = cursoRepository.save(curso);
        CursoResponse response = cursoMapper.entityToDto(actualizado);
        n8nSyncService.sincronizarCurso(response, "ACTUALIZAR");
        return response;
    }

    @Override
    @Transactional
    public void desactivarCurso(String id) {
        log.info("Desactivando curso (soft delete) con ID: {}", id);
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso", id));

        // Desactivación lógica (soft delete)
        curso.setEstado(false);
        cursoRepository.save(curso);
        n8nSyncService.sincronizarCurso(cursoMapper.entityToDto(curso), "DESACTIVAR");
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponse> obtenerCatalogo(String categoriaId, String nivelId) {
        List<Curso> cursos = cursoRepository.findCatalogoActivo(categoriaId, nivelId);
        return cursos.stream()
                .map(cursoMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
