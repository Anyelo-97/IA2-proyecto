package com.example.demo.curso.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, ID> {

    T guardar(T entidad);

    Optional<T> buscarPorId(ID id);

    List<T> listar();

    void eliminar(ID id);
}
