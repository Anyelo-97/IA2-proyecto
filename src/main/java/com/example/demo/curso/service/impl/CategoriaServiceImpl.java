package com.example.demo.curso.service.impl;

import com.example.demo.curso.dto.request.CategoriaRequest;
import com.example.demo.curso.dto.response.CategoriaResponse;
import com.example.demo.curso.mapper.CategoriaMapper;
import com.example.demo.curso.model.Categoria;
import com.example.demo.curso.repository.CategoriaRepository;
import com.example.demo.curso.service.CategoriaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaServiceImpl extends CrudServiceImpl<Categoria, String> implements CategoriaService {

    private final CategoriaRepository repository;
    private final CategoriaMapper mapper;

    public CategoriaServiceImpl(CategoriaRepository repository, CategoriaMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CategoriaResponse crear(CategoriaRequest request) {
        return mapper.entityToDto(guardar(mapper.requestToEntity(request)));
    }

    @Override
    public CategoriaResponse obtenerPorId(String id) {
        return buscarPorId(id).map(mapper::entityToDto).orElse(null);
    }

    @Override
    public List<CategoriaResponse> listarResponses() {
        return repository.findAll().stream().map(mapper::entityToDto).collect(Collectors.toList());
    }
}
