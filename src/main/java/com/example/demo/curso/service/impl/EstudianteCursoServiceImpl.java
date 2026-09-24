package com.example.demo.curso.service.impl;

import com.example.demo.curso.dto.request.EstudianteCursoRequest;
import com.example.demo.curso.dto.response.EstudianteCursoResponse;
import com.example.demo.curso.mapper.EstudianteCursoMapper;
import com.example.demo.curso.model.EstudianteCurso;
import com.example.demo.curso.model.EstudianteCursoId;
import com.example.demo.curso.repository.EstudianteCursoRepository;
import com.example.demo.curso.service.EstudianteCursoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstudianteCursoServiceImpl extends CrudServiceImpl<EstudianteCurso, EstudianteCursoId>
        implements EstudianteCursoService {

    private final EstudianteCursoRepository repository;
    private final EstudianteCursoMapper mapper;

    public EstudianteCursoServiceImpl(EstudianteCursoRepository repository, EstudianteCursoMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public EstudianteCursoResponse crear(EstudianteCursoRequest request) {
        return mapper.entityToDto(guardar(mapper.requestToEntity(request)));
    }

    @Override
    public EstudianteCursoResponse obtenerPorId(EstudianteCursoId id) {
        return buscarPorId(id).map(mapper::entityToDto).orElse(null);
    }

    @Override
    public List<EstudianteCursoResponse> listarResponses() {
        return repository.findAll().stream().map(mapper::entityToDto).collect(Collectors.toList());
    }
}
