package com.example.demo.curso.repository;

import com.example.demo.curso.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, String> {

    List<Consulta> findByEstudianteIdOrderByFechaDesc(String estudianteId);

    long countByEstado(String estado);
}
