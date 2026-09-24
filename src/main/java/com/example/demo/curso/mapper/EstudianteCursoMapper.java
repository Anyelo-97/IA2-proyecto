package com.example.demo.curso.mapper;

import com.example.demo.curso.dto.request.EstudianteCursoRequest;
import com.example.demo.curso.dto.response.EstudianteCursoResponse;
import com.example.demo.curso.model.Curso;
import com.example.demo.curso.model.Estudiante;
import com.example.demo.curso.model.EstudianteCurso;
import com.example.demo.curso.model.EstudianteCursoId;
import org.springframework.stereotype.Component;

@Component
public class EstudianteCursoMapper {

    public EstudianteCursoResponse entityToDto(EstudianteCurso relacion) {
        if (relacion == null) return null;
        return EstudianteCursoResponse.builder()
                .estudianteId(relacion.getEstudiante() != null ? relacion.getEstudiante().getId() :
                        relacion.getId() != null ? relacion.getId().getEstudianteId() : null)
                .cursoId(relacion.getCurso() != null ? relacion.getCurso().getId() :
                        relacion.getId() != null ? relacion.getId().getCursoId() : null)
                .build();
    }

    public EstudianteCurso requestToEntity(EstudianteCursoRequest request) {
        if (request == null) return null;
        Estudiante estudiante = new Estudiante(request.getEstudianteId(), null, null, null, null);
        Curso curso = Curso.builder().id(request.getCursoId()).build();
        return new EstudianteCurso(
                new EstudianteCursoId(request.getEstudianteId(), request.getCursoId()),
                estudiante,
                curso
        );
    }
}
