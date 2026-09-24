package com.example.demo.curso.service.impl;

import com.example.demo.curso.dto.request.CalificacionRequest;
import com.example.demo.curso.dto.response.CalificacionResponse;
import com.example.demo.curso.mapper.CalificacionMapper;
import com.example.demo.curso.model.Calificacion;
import com.example.demo.curso.repository.CalificacionRepository;
import com.example.demo.curso.service.CalificacionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalificacionServiceImpl extends CrudServiceImpl<Calificacion, String>
        implements CalificacionService {

    private final CalificacionRepository repository;
    private final CalificacionMapper mapper;

    public CalificacionServiceImpl(CalificacionRepository repository, CalificacionMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CalificacionResponse crear(CalificacionRequest request) {
        return mapper.entityToDto(guardar(mapper.requestToEntity(request)));
    }

    @Override
    public CalificacionResponse obtenerPorId(String id) {
        return buscarPorId(id).map(mapper::entityToDto).orElse(null);
    }

    @Override
    public List<CalificacionResponse> listarResponses() {
        return repository.findAll().stream().map(mapper::entityToDto).collect(Collectors.toList());
    }
}
