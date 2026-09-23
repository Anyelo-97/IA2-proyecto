package com.example.demo.curso.service.impl;

import com.example.demo.curso.model.NivelDificultad;
import com.example.demo.curso.repository.NivelDificultadRepository;
import com.example.demo.curso.service.NivelDificultadService;
import org.springframework.stereotype.Service;

@Service
public class NivelDificultadServiceImpl
        extends CrudServiceImpl<NivelDificultad, String>
        implements NivelDificultadService {

    public NivelDificultadServiceImpl(NivelDificultadRepository repository) {
        super(repository);
    }
}
