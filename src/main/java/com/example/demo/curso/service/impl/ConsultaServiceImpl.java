package com.example.demo.curso.service.impl;

import com.example.demo.curso.dto.request.ConsultaRequest;
import com.example.demo.curso.dto.response.ConsultaResponse;
import com.example.demo.curso.mapper.ConsultaMapper;
import com.example.demo.curso.model.Consulta;
import com.example.demo.curso.repository.ConsultaRepository;
import com.example.demo.curso.service.ConsultaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultaServiceImpl extends CrudServiceImpl<Consulta, String> implements ConsultaService {

    private final ConsultaRepository repository;
    private final ConsultaMapper mapper;

    public ConsultaServiceImpl(ConsultaRepository repository, ConsultaMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ConsultaResponse crear(ConsultaRequest request) {
        return mapper.entityToDto(guardar(mapper.requestToEntity(request)));
    }

    @Override
    public ConsultaResponse obtenerPorId(String id) {
        return buscarPorId(id).map(mapper::entityToDto).orElse(null);
    }

    @Override
    public List<ConsultaResponse> listarResponses() {
        return repository.findAll().stream().map(mapper::entityToDto).collect(Collectors.toList());
    }
}
