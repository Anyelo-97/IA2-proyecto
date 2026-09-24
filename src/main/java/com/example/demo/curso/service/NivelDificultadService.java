package com.example.demo.curso.service;

import com.example.demo.curso.model.NivelDificultad;
import com.example.demo.curso.dto.request.NivelDificultadRequest;
import com.example.demo.curso.dto.response.NivelDificultadResponse;
import java.util.List;

public interface NivelDificultadService extends CrudService<NivelDificultad, String> {
    NivelDificultadResponse crear(NivelDificultadRequest request);
    NivelDificultadResponse obtenerPorId(String id);
    List<NivelDificultadResponse> listarResponses();
}
