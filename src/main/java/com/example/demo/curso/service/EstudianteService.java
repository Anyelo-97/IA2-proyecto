package com.example.demo.curso.service;

import com.example.demo.curso.dto.request.EstudianteRequest;
import com.example.demo.curso.model.Estudiante;

public interface EstudianteService extends CrudService<Estudiante, String> {
    Estudiante registrar(EstudianteRequest request);
    Estudiante actualizar(String id, EstudianteRequest request);
}
