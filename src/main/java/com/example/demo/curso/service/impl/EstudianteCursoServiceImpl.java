package com.example.demo.curso.service.impl;

import com.example.demo.curso.model.EstudianteCurso;
import com.example.demo.curso.model.EstudianteCursoId;
import com.example.demo.curso.repository.EstudianteCursoRepository;
import com.example.demo.curso.service.EstudianteCursoService;
import org.springframework.stereotype.Service;

@Service
public class EstudianteCursoServiceImpl extends CrudServiceImpl<EstudianteCurso, EstudianteCursoId>
        implements EstudianteCursoService {

    public EstudianteCursoServiceImpl(EstudianteCursoRepository repository) {
        super(repository);
    }
}
