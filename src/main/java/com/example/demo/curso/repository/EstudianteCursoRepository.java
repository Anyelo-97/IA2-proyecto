package com.example.demo.curso.repository;

import com.example.demo.curso.model.EstudianteCurso;
import com.example.demo.curso.model.EstudianteCursoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteCursoRepository extends JpaRepository<EstudianteCurso, EstudianteCursoId> {
}
