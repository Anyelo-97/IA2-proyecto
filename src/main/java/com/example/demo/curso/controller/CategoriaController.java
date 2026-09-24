package com.example.demo.curso.controller;

import com.example.demo.curso.dto.request.CategoriaRequest;
import com.example.demo.curso.dto.response.CategoriaResponse;
import com.example.demo.curso.mapper.CategoriaMapper;
import com.example.demo.curso.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;
    private final CategoriaMapper mapper;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listar() {
        return ResponseEntity.ok(service.listar().stream().map(mapper::entityToDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscar(@PathVariable String id) {
        return service.buscarPorId(id).map(mapper::entityToDto)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody CategoriaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.entityToDto(service.guardar(mapper.requestToEntity(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizar(@PathVariable String id, @Valid @RequestBody CategoriaRequest request) {
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
