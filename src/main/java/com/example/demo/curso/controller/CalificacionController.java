package com.example.demo.curso.controller;

import com.example.demo.curso.dto.request.CalificacionRequest;
import com.example.demo.curso.dto.response.CalificacionResponse;
import com.example.demo.curso.mapper.CalificacionMapper;
import com.example.demo.curso.service.CalificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calificaciones")
@RequiredArgsConstructor
public class CalificacionController {
    private final CalificacionService service;
    private final CalificacionMapper mapper;

    @GetMapping
    public List<CalificacionResponse> listar() { return service.listar().stream().map(mapper::entityToDto).toList(); }

    @PostMapping
    public ResponseEntity<CalificacionResponse> crear(@Valid @RequestBody CalificacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.entityToDto(service.guardar(mapper.requestToEntity(request))));
    }
}
