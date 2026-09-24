package com.example.demo.curso.repository;

import com.example.demo.curso.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministradorRepository extends JpaRepository<Administrador, String> {
}
