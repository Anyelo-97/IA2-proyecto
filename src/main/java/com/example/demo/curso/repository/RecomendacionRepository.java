package com.example.demo.curso.repository;

import com.example.demo.curso.model.Recomendacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecomendacionRepository extends JpaRepository<Recomendacion, String> {
}
