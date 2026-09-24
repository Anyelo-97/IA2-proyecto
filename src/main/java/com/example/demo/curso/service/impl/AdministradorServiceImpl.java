package com.example.demo.curso.service.impl;

import com.example.demo.curso.model.Administrador;
import com.example.demo.curso.repository.AdministradorRepository;
import com.example.demo.curso.service.AdministradorService;
import org.springframework.stereotype.Service;

@Service
public class AdministradorServiceImpl extends CrudServiceImpl<Administrador, String>
        implements AdministradorService {

    public AdministradorServiceImpl(AdministradorRepository repository) {
        super(repository);
    }
}
