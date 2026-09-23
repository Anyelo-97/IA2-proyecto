package com.example.demo.curso.service;

import com.example.demo.curso.model.EstudianteCurso;
import com.example.demo.curso.model.EstudianteCursoId;
import com.example.demo.curso.dto.request.EstudianteCursoRequest;
import com.example.demo.curso.dto.response.EstudianteCursoResponse;
import java.util.List;

public interface EstudianteCursoService extends CrudService<EstudianteCurso, EstudianteCursoId> {
    EstudianteCursoResponse crear(EstudianteCursoRequest request);
    EstudianteCursoResponse obtenerPorId(EstudianteCursoId id);
    List<EstudianteCursoResponse> listarResponses();
}
