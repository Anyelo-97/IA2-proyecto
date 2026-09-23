package com.example.demo.curso.service;

import com.example.demo.curso.model.Categoria;
import com.example.demo.curso.dto.request.CategoriaRequest;
import com.example.demo.curso.dto.response.CategoriaResponse;
import java.util.List;

public interface CategoriaService extends CrudService<Categoria, String> {
    CategoriaResponse crear(CategoriaRequest request);
    CategoriaResponse obtenerPorId(String id);
    List<CategoriaResponse> listarResponses();
}
