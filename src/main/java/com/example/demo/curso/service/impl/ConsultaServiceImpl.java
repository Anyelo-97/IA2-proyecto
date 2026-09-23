package com.example.demo.curso.service.impl;

import com.example.demo.curso.model.Consulta;
import com.example.demo.curso.repository.ConsultaRepository;
import com.example.demo.curso.service.ConsultaService;
import org.springframework.stereotype.Service;

@Service
public class ConsultaServiceImpl extends CrudServiceImpl<Consulta, String> implements ConsultaService {

    public ConsultaServiceImpl(ConsultaRepository repository) {
        super(repository);
    }
}
