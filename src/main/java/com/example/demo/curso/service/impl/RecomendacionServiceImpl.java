package com.example.demo.curso.service.impl;

import com.example.demo.curso.dto.request.RecomendacionRequest;
import com.example.demo.curso.dto.response.RecomendacionResponse;
import com.example.demo.curso.mapper.RecomendacionMapper;
import com.example.demo.curso.model.Recomendacion;
import com.example.demo.curso.repository.RecomendacionRepository;
import com.example.demo.curso.service.RecomendacionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecomendacionServiceImpl extends CrudServiceImpl<Recomendacion, String>
        implements RecomendacionService {

    private final RecomendacionRepository repository;
    private final RecomendacionMapper mapper;

    public RecomendacionServiceImpl(RecomendacionRepository repository, RecomendacionMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public RecomendacionResponse crear(RecomendacionRequest request) {
        return mapper.entityToDto(guardar(mapper.requestToEntity(request)));
    }

    @Override
    public RecomendacionResponse obtenerPorId(String id) {
        return buscarPorId(id).map(mapper::entityToDto).orElse(null);
    }

    @Override
    public List<RecomendacionResponse> listarResponses() {
        return repository.findAll().stream().map(mapper::entityToDto).collect(Collectors.toList());
    }
}
