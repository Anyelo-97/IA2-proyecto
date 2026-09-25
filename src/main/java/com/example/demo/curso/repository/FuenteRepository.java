package com.example.demo.curso.repository;

import com.example.demo.curso.model.Fuente;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuenteRepository extends JpaRepository<Fuente, String> {
    List<Fuente> findByRecomendacionId(String recomendacionId);

    @Query("SELECT f.curso.nombre, " +
            "COUNT(f) as total FROM Fuente f GROUP BY f.curso.id, f.curso.nombre ORDER BY total DESC")
    List<Object[]> findCursoMasRecomendado(Pageable pageable);
}
