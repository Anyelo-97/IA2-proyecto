package com.example.demo.curso.service.impl;

import com.example.demo.curso.dto.request.UsuarioRequest;
import com.example.demo.curso.dto.response.UsuarioResponse;
import com.example.demo.curso.mapper.UsuarioMapper;
import com.example.demo.curso.model.Usuario;
import com.example.demo.curso.repository.UsuarioRepository;
import com.example.demo.curso.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl extends CrudServiceImpl<Usuario, String> implements UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;

    public UsuarioServiceImpl(UsuarioRepository repository, UsuarioMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UsuarioResponse crear(UsuarioRequest request) {
        return mapper.entityToDto(guardar(mapper.requestToEntity(request)));
    }

    @Override
    public UsuarioResponse obtenerPorId(String id) {
        return buscarPorId(id).map(mapper::entityToDto).orElse(null);
    }

    @Override
    public List<UsuarioResponse> listarResponses() {
        return repository.findAll().stream().map(mapper::entityToDto).collect(Collectors.toList());
    }
}
