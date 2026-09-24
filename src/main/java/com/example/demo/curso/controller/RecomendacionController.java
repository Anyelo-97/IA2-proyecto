package com.example.demo.curso.controller;

import com.example.demo.curso.dto.request.RecomendacionRequest;
import com.example.demo.curso.dto.response.RecomendacionResponse;
import com.example.demo.curso.mapper.RecomendacionMapper;
import com.example.demo.curso.service.RecomendacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recomendaciones")
@RequiredArgsConstructor
public class RecomendacionController {
    private final RecomendacionService service;
    private final RecomendacionMapper mapper;

    @GetMapping
    public List<RecomendacionResponse> listar() { return service.listar().stream().map(mapper::entityToDto).toList(); }

    @GetMapping("/{id}")
    public ResponseEntity<RecomendacionResponse> buscar(@PathVariable String id) {
        return service.buscarPorId(id).map(mapper::entityToDto).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RecomendacionResponse> crear(@Valid @RequestBody RecomendacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.entityToDto(service.guardar(mapper.requestToEntity(request))));
    }
}
