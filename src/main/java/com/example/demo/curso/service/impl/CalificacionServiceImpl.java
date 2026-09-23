package com.example.demo.curso.service.impl;

import com.example.demo.curso.model.Calificacion;
import com.example.demo.curso.repository.CalificacionRepository;
import com.example.demo.curso.service.CalificacionService;
import org.springframework.stereotype.Service;

@Service
public class CalificacionServiceImpl extends CrudServiceImpl<Calificacion, String>
        implements CalificacionService {

    public CalificacionServiceImpl(CalificacionRepository repository) {
        super(repository);
    }
}
