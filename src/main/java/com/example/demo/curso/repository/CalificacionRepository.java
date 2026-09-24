package com.example.demo.curso.repository;

import com.example.demo.curso.model.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, String> {
    boolean existsByRecomendacion_Id(String recomendacionId);
    Optional<Calificacion> findByRecomendacion_Id(String recomendacionId);

    @Query("SELECT AVG(c.puntuacion) FROM Calificacion c")
    Double promedioCalificaciones();
}

