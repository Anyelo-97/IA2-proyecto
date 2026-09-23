package com.example.demo.curso.service.impl;

import com.example.demo.curso.model.Categoria;
import com.example.demo.curso.repository.CategoriaRepository;
import com.example.demo.curso.service.CategoriaService;
import org.springframework.stereotype.Service;

@Service
public class CategoriaServiceImpl extends CrudServiceImpl<Categoria, String> implements CategoriaService {

    public CategoriaServiceImpl(CategoriaRepository repository) {
        super(repository);
    }
}
