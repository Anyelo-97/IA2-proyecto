package com.example.demo.curso.service;

import com.example.demo.curso.model.Fuente;
import com.example.demo.curso.dto.request.FuenteRequest;
import com.example.demo.curso.dto.response.FuenteResponse;
import java.util.List;

public interface FuenteService extends CrudService<Fuente, String> {
    FuenteResponse crear(FuenteRequest request);
    FuenteResponse obtenerPorId(String id);
    List<FuenteResponse> listarResponses();
}
