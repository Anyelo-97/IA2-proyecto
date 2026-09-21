package com.example.demo.curso.repository;

import com.example.demo.curso.model.NivelDificultad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NivelDificultadRepository extends JpaRepository<NivelDificultad, String> {
    Optional<NivelDificultad> findByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
}
