package com.example.demo.curso.service;

import com.example.demo.curso.dto.request.CursoRequest;
import com.example.demo.curso.dto.response.CursoResponse;

import java.util.List;

public interface CursoService {

    CursoResponse crearCurso(CursoRequest request);

    CursoResponse obtenerCursoPorId(String id);

    List<CursoResponse> listarCursos(Boolean estado);

    CursoResponse actualizarCurso(String id, CursoRequest request);

    void desactivarCurso(String id);

    List<CursoResponse> obtenerCatalogo(String categoriaId, String nivelId);
}
