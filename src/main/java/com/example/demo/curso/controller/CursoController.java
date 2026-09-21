package com.example.demo.curso.controller;


import com.example.demo.curso.model.Curso;
import com.example.demo.curso.repository.CategoriaRepository;
import com.example.demo.curso.repository.CursoRepository;
import com.example.demo.curso.repository.NivelDificultadRepository;
import com.example.demo.curso.service.CursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CursoController {

    private final CursoService cursoService;

    @GetMapping
    public List<Curso> listar() {}

    @PostMapping




}
