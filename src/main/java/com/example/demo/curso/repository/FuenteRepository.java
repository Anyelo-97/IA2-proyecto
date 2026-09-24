package com.example.demo.curso.repository;

import com.example.demo.curso.model.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuenteRepository extends JpaRepository<Fuente, String> {
}
