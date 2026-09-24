package com.example.demo.curso.controller;

import com.example.demo.curso.dto.request.NivelDificultadRequest;
import com.example.demo.curso.dto.response.NivelDificultadResponse;
import com.example.demo.curso.mapper.NivelDificultadMapper;
import com.example.demo.curso.service.NivelDificultadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/niveles")
@RequiredArgsConstructor
public class NivelDificultadController {

    private final NivelDificultadService service;
    private final NivelDificultadMapper mapper;

    @GetMapping
    public ResponseEntity<List<NivelDificultadResponse>> listar() {
        return ResponseEntity.ok(service.listar().stream().map(mapper::entityToDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NivelDificultadResponse> buscar(@PathVariable String id) {
        return service.buscarPorId(id).map(mapper::entityToDto)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NivelDificultadResponse> crear(@Valid @RequestBody NivelDificultadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.entityToDto(service.guardar(mapper.requestToEntity(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NivelDificultadResponse> actualizar(@PathVariable String id, @Valid @RequestBody NivelDificultadRequest request) {
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
