package com.example.demo.curso.service.impl;

import com.example.demo.curso.dto.request.EstudianteRequest;
import com.example.demo.curso.dto.response.EstudianteResponse;
import com.example.demo.curso.mapper.EstudianteMapper;
import com.example.demo.curso.model.Estudiante;
import com.example.demo.curso.repository.EstudianteRepository;
import com.example.demo.curso.service.EstudianteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstudianteServiceImpl extends CrudServiceImpl<Estudiante, String> implements EstudianteService {

    private final EstudianteRepository repository;
    private final EstudianteMapper mapper;

    public EstudianteServiceImpl(EstudianteRepository repository, EstudianteMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public EstudianteResponse crear(EstudianteRequest request) {
        return mapper.entityToDto(guardar(mapper.requestToEntity(request)));
    }

    @Override
    public EstudianteResponse obtenerPorId(String id) {
        return buscarPorId(id).map(mapper::entityToDto).orElse(null);
    }

    @Override
    public List<EstudianteResponse> listarResponses() {
        return repository.findAll().stream().map(mapper::entityToDto).collect(Collectors.toList());
    }
}
