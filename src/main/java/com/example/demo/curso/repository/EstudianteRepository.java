package com.example.demo.curso.repository;

import com.example.demo.curso.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteRepository extends JpaRepository<Estudiante, String> {
}
