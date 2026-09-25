package com.example.demo.curso.service;

import com.example.demo.curso.dto.request.CalificacionRequest;
import com.example.demo.curso.dto.response.CalificacionResponse;

import java.util.List;

public interface CalificacionService {

    CalificacionResponse calificar(CalificacionRequest request);

    CalificacionResponse obtenerPorRecomendacion(String recomendacionId);

    List<CalificacionResponse> listar();
}
