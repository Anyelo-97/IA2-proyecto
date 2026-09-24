package com.example.demo.curso.repository;

import com.example.demo.curso.model.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuenteRepository extends JpaRepository<Fuente, String> {
    List<Fuente> findByRecomendacionId(String recomendacionId);
}
