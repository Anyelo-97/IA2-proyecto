package com.example.demo.curso.service;

import com.example.demo.curso.model.Calificacion;
import com.example.demo.curso.dto.request.CalificacionRequest;
import com.example.demo.curso.dto.response.CalificacionResponse;
import java.util.List;

public interface CalificacionService extends CrudService<Calificacion, String> {
    CalificacionResponse crear(CalificacionRequest request);
    CalificacionResponse obtenerPorId(String id);
    List<CalificacionResponse> listarResponses();
}
