package com.example.demo.curso.repository;

import com.example.demo.curso.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, String> {
}
