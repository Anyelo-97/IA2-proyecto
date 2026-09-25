package com.example.demo.curso.controller;

import com.example.demo.curso.dto.response.RecomendacionResponse;
import com.example.demo.curso.mapper.RecomendacionMapper;
import com.example.demo.curso.service.RecomendacionService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/api/recomendaciones")
@RequiredArgsConstructor
public class RecomendacionController {
    private final RecomendacionService service;
    private final RecomendacionMapper mapper;

    @GetMapping
    public List<RecomendacionResponse> listar() {
        return service.listar().stream().map(mapper::entityToDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecomendacionResponse> buscar(@PathVariable String id) {
        return service.buscarPorId(id).map(mapper::entityToDto).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
