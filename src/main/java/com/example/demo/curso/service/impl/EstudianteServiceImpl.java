package com.example.demo.curso.service.impl;

import com.example.demo.curso.model.Estudiante;
import com.example.demo.curso.repository.EstudianteRepository;
import com.example.demo.curso.service.EstudianteService;
import org.springframework.stereotype.Service;

@Service
public class EstudianteServiceImpl extends CrudServiceImpl<Estudiante, String> implements EstudianteService {

    public EstudianteServiceImpl(EstudianteRepository repository) {
        super(repository);
    }
}
