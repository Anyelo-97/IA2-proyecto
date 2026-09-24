package com.example.demo.curso.service.impl;

import com.example.demo.curso.model.Usuario;
import com.example.demo.curso.repository.UsuarioRepository;
import com.example.demo.curso.service.UsuarioService;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl extends CrudServiceImpl<Usuario, String> implements UsuarioService {

    public UsuarioServiceImpl(UsuarioRepository repository) {
        super(repository);
    }
}
