package com.example.demo.curso.service.impl;

import com.example.demo.curso.dto.request.NivelDificultadRequest;
import com.example.demo.curso.dto.response.NivelDificultadResponse;
import com.example.demo.curso.mapper.NivelDificultadMapper;
import com.example.demo.curso.model.NivelDificultad;
import com.example.demo.curso.repository.NivelDificultadRepository;
import com.example.demo.curso.service.NivelDificultadService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NivelDificultadServiceImpl
        extends CrudServiceImpl<NivelDificultad, String>
        implements NivelDificultadService {

    private final NivelDificultadRepository repository;
    private final NivelDificultadMapper mapper;

    public NivelDificultadServiceImpl(NivelDificultadRepository repository, NivelDificultadMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public NivelDificultadResponse crear(NivelDificultadRequest request) {
        return mapper.entityToDto(guardar(mapper.requestToEntity(request)));
    }

    @Override
    public NivelDificultadResponse obtenerPorId(String id) {
        return buscarPorId(id).map(mapper::entityToDto).orElse(null);
    }

    @Override
    public List<NivelDificultadResponse> listarResponses() {
        return repository.findAll().stream().map(mapper::entityToDto).collect(Collectors.toList());
    }
}
