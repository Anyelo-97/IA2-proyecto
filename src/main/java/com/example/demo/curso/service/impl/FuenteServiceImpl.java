package com.example.demo.curso.service.impl;

import com.example.demo.curso.model.Fuente;
import com.example.demo.curso.repository.FuenteRepository;
import com.example.demo.curso.service.FuenteService;
import org.springframework.stereotype.Service;

@Service
public class FuenteServiceImpl extends CrudServiceImpl<Fuente, String> implements FuenteService {

    public FuenteServiceImpl(FuenteRepository repository) {
        super(repository);
    }
}
