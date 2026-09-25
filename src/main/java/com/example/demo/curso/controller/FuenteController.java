package com.example.demo.curso.controller;

import com.example.demo.curso.dto.response.FuenteResponse;
import com.example.demo.curso.mapper.FuenteMapper;
import com.example.demo.curso.repository.FuenteRepository;
import com.example.demo.curso.service.FuenteService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/api/fuentes")
@RequiredArgsConstructor
public class FuenteController {

    private final FuenteService service;
    private final FuenteRepository repository;
    private final FuenteMapper mapper;

    @GetMapping
    public List<FuenteResponse> listar() {
        return service.listar().stream().map(mapper::entityToDto).toList();
    }

    @GetMapping("/recomendacion/{recomendacionId}")
    public ResponseEntity<List<FuenteResponse>> listarPorRecomendacion(@PathVariable String recomendacionId) {
        return ResponseEntity.ok(repository.findByRecomendacionId(recomendacionId).stream().map(mapper::entityToDto).toList());
    }
}
