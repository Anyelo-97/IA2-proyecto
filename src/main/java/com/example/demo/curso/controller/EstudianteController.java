package com.example.demo.curso.controller;

import com.example.demo.curso.dto.request.EstudianteRequest;
import com.example.demo.curso.dto.response.EstudianteResponse;
import com.example.demo.curso.mapper.EstudianteMapper;
import com.example.demo.curso.service.EstudianteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final EstudianteService service;
    private final EstudianteMapper mapper;

    @GetMapping
    public ResponseEntity<List<EstudianteResponse>> listar() {
        return ResponseEntity.ok(service.listar().stream().map(mapper::entityToDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteResponse> buscar(@PathVariable String id) {
        return service.buscarPorId(id).map(mapper::entityToDto)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EstudianteResponse> crear(@Valid @RequestBody EstudianteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.entityToDto(service.guardar(mapper.requestToEntity(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteResponse> actualizar(@PathVariable String id, @Valid @RequestBody EstudianteRequest request) {
        if (service.buscarPorId(id).isEmpty()) return ResponseEntity.notFound().build();
        request.setId(id);
        return ResponseEntity.ok(mapper.entityToDto(service.guardar(mapper.requestToEntity(request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        if (service.buscarPorId(id).isEmpty()) return ResponseEntity.notFound().build();
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
