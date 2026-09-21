package com.example.demo.curso.repository;

import com.example.demo.curso.domain.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, String> {

    List<Curso> findByEstadoTrue();

    List<Curso> findByEstado(Boolean estado);

    Optional<Curso> findByIdAndEstadoTrue(String id);

    @Query("SELECT c FROM Curso c WHERE c.estado = true " +
           "AND (:categoriaId IS NULL OR :categoriaId = '' OR c.categoria.id = :categoriaId) " +
           "AND (:nivelId IS NULL OR :nivelId = '' OR c.nivel.id = :nivelId)")
    List<Curso> findCatalogoActivo(@Param("categoriaId") String categoriaId,
                                   @Param("nivelId") String nivelId);

    boolean existsByNombreIgnoreCase(String nombre);
}
