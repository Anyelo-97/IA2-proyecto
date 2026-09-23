package com.example.demo.curso.service;

import com.example.demo.curso.model.Recomendacion;
import com.example.demo.curso.dto.request.RecomendacionRequest;
import com.example.demo.curso.dto.response.RecomendacionResponse;
import java.util.List;

public interface RecomendacionService extends CrudService<Recomendacion, String> {
    RecomendacionResponse crear(RecomendacionRequest request);
    RecomendacionResponse obtenerPorId(String id);
    List<RecomendacionResponse> listarResponses();
}
