package com.example.demo.curso.service.impl;

import com.example.demo.curso.model.Recomendacion;
import com.example.demo.curso.repository.RecomendacionRepository;
import com.example.demo.curso.service.RecomendacionService;
import org.springframework.stereotype.Service;

@Service
public class RecomendacionServiceImpl extends CrudServiceImpl<Recomendacion, String>
        implements RecomendacionService {

    public RecomendacionServiceImpl(RecomendacionRepository repository) {
        super(repository);
    }
}
