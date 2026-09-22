package com.example.demo.curso.repository;

import com.example.demo.curso.model.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalificacionRepository extends JpaRepository<Calificacion, String> {
}
