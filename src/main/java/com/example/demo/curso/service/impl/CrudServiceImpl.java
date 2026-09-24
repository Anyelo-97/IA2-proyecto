package com.example.demo.curso.service.impl;

import com.example.demo.curso.service.CrudService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class CrudServiceImpl<T, ID> implements CrudService<T, ID> {

    private final JpaRepository<T, ID> repository;

    protected CrudServiceImpl(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public T guardar(T entidad) {
        return repository.save(entidad);
    }

    @Override
    public Optional<T> buscarPorId(ID id) {
        return repository.findById(id);
    }

    @Override
    public List<T> listar() {
        return repository.findAll();
    }

    @Override
    public void eliminar(ID id) {
        repository.deleteById(id);
    }
}
