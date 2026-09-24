package com.example.demo.curso.repository;

import com.example.demo.curso.model.Recomendacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecomendacionRepository extends JpaRepository<Recomendacion, String> {
    Optional<Recomendacion> findByConsultaId(String consultaId);
}
