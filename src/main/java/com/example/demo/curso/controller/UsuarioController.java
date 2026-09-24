package com.example.demo.curso.controller;

import com.example.demo.curso.dto.request.UsuarioRequest;
import com.example.demo.curso.dto.response.UsuarioResponse;
import com.example.demo.curso.mapper.UsuarioMapper;
import com.example.demo.curso.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService service;
    private final UsuarioMapper mapper;

    @GetMapping
    public List<UsuarioResponse> listar() { return service.listar().stream().map(mapper::entityToDto).toList(); }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscar(@PathVariable String id) {
        return service.buscarPorId(id).map(mapper::entityToDto).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.entityToDto(service.guardar(mapper.requestToEntity(request))));
    }
}
