package com.example.demo.curso.service.impl;

import com.example.demo.curso.dto.request.AdministradorRequest;
import com.example.demo.curso.dto.response.AdministradorResponse;
import com.example.demo.curso.mapper.AdministradorMapper;
import com.example.demo.curso.model.Administrador;
import com.example.demo.curso.repository.AdministradorRepository;
import com.example.demo.curso.service.AdministradorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministradorServiceImpl extends CrudServiceImpl<Administrador, String>
        implements AdministradorService {

    private final AdministradorRepository repository;
    private final AdministradorMapper mapper;

    public AdministradorServiceImpl(AdministradorRepository repository, AdministradorMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public AdministradorResponse crear(AdministradorRequest request) {
        return mapper.entityToDto(guardar(mapper.requestToEntity(request)));
    }

    @Override
    public AdministradorResponse obtenerPorId(String id) {
        return buscarPorId(id).map(mapper::entityToDto).orElse(null);
    }

    @Override
    public List<AdministradorResponse> listarResponses() {
        return repository.findAll().stream().map(mapper::entityToDto).collect(Collectors.toList());
    }
}
