package com.example.demo.curso.service.impl;

import com.example.demo.curso.dto.request.FuenteRequest;
import com.example.demo.curso.dto.response.FuenteResponse;
import com.example.demo.curso.mapper.FuenteMapper;
import com.example.demo.curso.model.Fuente;
import com.example.demo.curso.repository.FuenteRepository;
import com.example.demo.curso.service.FuenteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FuenteServiceImpl extends CrudServiceImpl<Fuente, String> implements FuenteService {

    private final FuenteRepository repository;
    private final FuenteMapper mapper;

    public FuenteServiceImpl(FuenteRepository repository, FuenteMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public FuenteResponse crear(FuenteRequest request) {
        return mapper.entityToDto(guardar(mapper.requestToEntity(request)));
    }

    @Override
    public FuenteResponse obtenerPorId(String id) {
        return buscarPorId(id).map(mapper::entityToDto).orElse(null);
    }

    @Override
    public List<FuenteResponse> listarResponses() {
        return repository.findAll().stream().map(mapper::entityToDto).collect(Collectors.toList());
    }
}
