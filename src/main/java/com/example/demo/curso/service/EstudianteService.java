package com.example.demo.curso.service;

import com.example.demo.curso.model.Estudiante;
import com.example.demo.curso.dto.request.EstudianteRequest;
import com.example.demo.curso.dto.response.EstudianteResponse;
import java.util.List;

public interface EstudianteService extends CrudService<Estudiante, String> {
    EstudianteResponse crear(EstudianteRequest request);
    EstudianteResponse obtenerPorId(String id);
    List<EstudianteResponse> listarResponses();
}
